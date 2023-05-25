package com.madm.common_libs.server

import android.content.Context
import android.content.res.Resources
import com.github.kittinunf.fuel.core.isSuccessful
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.onError
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.madm.common_libs.R


class Server (context : Context) {
    var serverBaseUrl : String

    // MANAGE KIND OF REQUESTS
    enum class RequestKind { MESSAGES, USERS, CALENDAR }

    val requestsMap = mapOf<RequestKind, String>(
        RequestKind.MESSAGES to "/messages",
        RequestKind.CALENDAR to "/calendar",
        RequestKind.USERS to "/users",
    )

    // CONSTRUCTOR
    init {
//        this.serverBaseUrl = getResources().getString(R.string.base_url);
//        this.serverBaseUrl = Resources.getSystem().getText(R.string.base_server_url).toString()
        this.serverBaseUrl = context.getString(R.string.base_server_url)
    }


    // BUILD POST REQUESTS
    fun <T> makePostRequest(objectToSend : T, kind : RequestKind) {
        val requestBody = Gson().toJson(objectToSend)
        val serverCompleteUrl = serverBaseUrl + requestsMap[kind]

        serverCompleteUrl.httpPost().body(requestBody).responseString { _, response, result ->
            if(!response.isSuccessful){
                result.onError { println("Request failed: ${it.exception}") }
            }
        }
    }


    // BUILD GET REQUESTS
    inline fun <reified T> makeGetRequest(kind : RequestKind, crossinline callbackFunction: (T) -> Unit){
        val serverCompleteUrl = serverBaseUrl + requestsMap[kind]

        serverCompleteUrl.httpGet().responseString { _, response, result ->
            if(response.isSuccessful){
                val data = result.get()
                callbackFunction(Gson().fromJson(data, T::class.java))
            } else {
                result.onError { println("Request failed: ${it.exception}") }
            }
        }
    }



    // BUILD GET REQUESTS FOR LISTS
    inline fun <reified T> makeListsGetRequest(
        kind : RequestKind,
        crossinline callbackFunction: (List<T>) -> Unit
    ) {
        val serverCompleteUrl = serverBaseUrl + requestsMap[kind]

        serverCompleteUrl.httpGet().responseString { _, response, result ->
            if(response.isSuccessful){
                val data = result.get()
                val itemType = object : TypeToken<List<T>>() {}.type
                callbackFunction(Gson().fromJson(data, itemType))
            } else {
                result.onError { println("Request failed: ${it.exception}") }
            }
        }
    }
}