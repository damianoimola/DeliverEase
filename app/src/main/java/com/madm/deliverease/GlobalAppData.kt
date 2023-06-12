package com.madm.deliverease

import com.madm.common_libs.model.User

var globalUser : User? = null
var globalAllUsers : MutableList<User> = mutableListOf()

const val SHARED_PREFERENCES_FILE = "accessFile"
const val EMAIL_FIELD = "EMAIL"
const val PASSWORD_FIELD = "PASSWORD"
const val STARTUP_LANGUAGE_FIELD = "STARTUP_LANG"

const val ADMIN_MIN_WEEK = "AD_MIN_WEEK"
const val ADMIN_MAX_WEEK = "AD_MAX_WEEk"
const val ADMIN_MIN_DAY = "AD_MIN_DAY"
const val ADMIN_MAX_DAY = "AD_MAX_DAY"

fun cleanGlobalAppData(){
    globalUser = null
    globalAllUsers = mutableListOf()
}