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
import com.github.kittinunf.fuel.httpDelete
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.onError
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.madm.common_libs.R
import com.madm.common_libs.network.NetworkConnection


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


    /**
     * MANAGE REQUEST'S KIND
     */
    enum class RequestKind { MESSAGES, USERS, CALENDAR }

    val requestsMap = mapOf<RequestKind, String>(
        RequestKind.MESSAGES to "/messages",
        RequestKind.CALENDAR to "/calendar",
        RequestKind.USERS to "/users",
    )

    /**
     * CONSTRUCTOR
     */
    init {
        this.serverBaseUrl = context.getString(R.string.base_server_url)
        this.context = context
    }


    /**
     * BUILD POST REQUESTS
     */
    inline fun <T> makePostRequest(objectToSend : T, kind : RequestKind, crossinline callbackFunction: (Boolean) -> Unit = { }): Boolean {

        if(!NetworkConnection.isUserOnline(context))
            return false

        val requestBody = Gson().toJson(objectToSend)

        val serverCompleteUrl = serverBaseUrl + requestsMap[kind]

        serverCompleteUrl.httpPost().body(requestBody).responseString { _, response, result ->
            callbackFunction(response.isSuccessful)
            if(!response.isSuccessful){
                result.onError { println("Request failed: ${it.exception} - ${it.errorData} - ${it.response}") }
            }
        }

        return true
    }


    /**
     * BUILD DELETE REQUESTS
     */
    inline fun <T> makeDeleteRequest(objectToSend : T, kind : RequestKind, crossinline callbackFunction: (Boolean) -> Unit = { }) : Boolean {

        if(!NetworkConnection.isUserOnline(context))
            return false

        val requestBody = Gson().toJson(objectToSend)
        val serverCompleteUrl = serverBaseUrl + requestsMap[kind]

        serverCompleteUrl.httpDelete().body(requestBody).responseString { _, response, result ->
            callbackFunction(response.isSuccessful)
            if(!response.isSuccessful){
                result.onError { println("Request failed: ${it.exception} - ${it.errorData} - ${it.response}") }
            }
        }

        return true
    }


    /**
     * BUILDS GET REQUESTS
     */
    inline fun <reified T> makeGetRequest(kind : RequestKind, crossinline callbackFunction: (T) -> Unit) : Boolean {

        if(!NetworkConnection.isUserOnline(context))
            return false

        val serverCompleteUrl = serverBaseUrl + requestsMap[kind]

        serverCompleteUrl.httpGet().responseString { _, response, result ->
            if (response.isSuccessful) {
                val data = result.get()
                callbackFunction(Gson().fromJson(data, T::class.java))
            } else {
                result.onError { println("Request failed: ${it.exception}") }
            }
        }

        return true
    }



    /**
     * BUILD GET REQUESTS FOR LISTS
     */
    inline fun <reified T> makeListsGetRequest(
        kind : RequestKind,
        crossinline callbackFunction: (List<T>) -> Unit
    ) : Boolean {

        if(!NetworkConnection.isUserOnline(context))
            return false

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

        return true
    }
}