package com.madm.deliverease

import com.madm.common_libs.model.User

var globalUser : User? = null
var globalAllUsers : MutableList<User> = mutableListOf()

const val SHARED_PREFERENCES_FILE = "accessFile"
const val EMAIL_FIELD = "EMAIL"
const val PASSWORD_FIELD = "PASSWORD"


fun cleanGlobalAppData(){
    globalUser = null
    globalAllUsers = mutableListOf()
}