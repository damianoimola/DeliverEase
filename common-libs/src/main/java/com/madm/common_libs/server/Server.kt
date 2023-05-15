package com.madm.common_libs.server

import com.github.kittinunf.fuel.core.isServerError
import com.github.kittinunf.fuel.core.isSuccessful
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.onError
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.parcelize.IgnoredOnParcel
import java.io.File
import java.io.FileInputStream
import java.util.*


class Server () {
    var serverBaseUrl : String

    // MANAGE KIND OF REQUESTS
    enum class RequestKind { MESSAGES, CHANGES, USERS}

    val requestsMap = mapOf<RequestKind, String>(
        RequestKind.MESSAGES to "/message",
        RequestKind.CHANGES to "/changes",
        RequestKind.USERS to "/users",
    )

    // CONSTRUCTOR
    init {
        val file = File("java/com/madm/common_libs/server/server.properties")

        this.serverBaseUrl = ""
        val prop = Properties()
        FileInputStream(file).use { prop.load(it) }

        // Print all properties
        prop.stringPropertyNames()
            .associateWith { prop.getProperty(it) }
            .forEach {
                if (it.key == "SERVER_BASE_URL")
                    serverBaseUrl = it.value
            }
    }


    // BUILD POST REQUESTS
    fun <T> makePostRequest(objectToSend : T, kind : RequestKind) {
        val body = Gson().toJson(objectToSend)
        val serverCompleteUrl = serverBaseUrl + requestsMap[kind]

        CoroutineScope(Dispatchers.IO).launch {
            serverCompleteUrl.httpPost().body(body).responseString { _, response, result ->
                if(response.isSuccessful){
                    val data = result.get()
                    println(data)
                } else {
                    result.onError {
                        println("Request failed: ${it.exception}")
                    }
                }
            }
        }
    }


    // BUILD GET REQUESTS
    inline fun <reified T> makeGetRequest(kind : RequestKind): T? {
        val serverCompleteUrl = serverBaseUrl + requestsMap[kind]
        var returnObject : T? = null

        CoroutineScope(Dispatchers.IO).launch {
            serverCompleteUrl.httpGet().responseString { _, response, result ->
                if(response.isSuccessful){
                    val data = result.get()
                    returnObject = Gson().fromJson(data, T::class.java)
                } else {
                    result.onError {
                        println("Request failed: ${it.exception}")
                    }
                }
            }
        }

        return returnObject
    }

}