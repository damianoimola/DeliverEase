package com.madm.common_libs.server

import android.content.Context
import android.content.res.Resources
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import android.widget.Toast
import androidx.core.content.contentValuesOf
import androidx.room.Room
import com.github.kittinunf.fuel.core.isSuccessful
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.onError
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.madm.common_libs.R


class Server private constructor (context : Context) {
    var serverBaseUrl : String
    val context: Context


    companion object{
        private var s : Server? = null

        fun getInstance (context : Context) : Server{
            if(s == null)
                // the "applicationContext" should avoid the upper warning
                s = Server(context.applicationContext)

            return s as Server
        }
    }


    // MANAGE REQUEST'S KIND
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
        this.context = context
    }


    // BUILD POST REQUESTS
    inline fun <T> makePostRequest(objectToSend : T, kind : RequestKind, crossinline callbackFunction: (Boolean) -> Unit = { }) {
        val requestBody = Gson().toJson(objectToSend)

        println("BODY REQUEST: $requestBody")
        val serverCompleteUrl = serverBaseUrl + requestsMap[kind]

        serverCompleteUrl.httpPost().body(requestBody).responseString { _, response, result ->
            callbackFunction(response.isSuccessful)
            if(!response.isSuccessful){
                result.onError { println("Request failed: ${it.exception} - ${it.errorData} - ${it.response}") }
            }
        }
    }


    // BUILD GET REQUESTS
    inline fun <reified T> makeGetRequest(kind : RequestKind, crossinline callbackFunction: (T) -> Unit){
        val serverCompleteUrl = serverBaseUrl + requestsMap[kind]

        serverCompleteUrl.httpGet().responseString { _, response, result ->
            if (response.isSuccessful) {
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
            if (response.isSuccessful) {
                val data = result.get()
                val itemType = object : TypeToken<List<T>>() {}.type
                callbackFunction(Gson().fromJson(data, itemType))
            } else {
                result.onError { println("Request failed: ${it.exception}") }
            }
        }
    }
}