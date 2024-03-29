package com.madm.deliverease

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.madm.common_libs.model.User

var globalUser : User? = null
var globalAllUsers : MutableList<User> = mutableListOf()
var darkMode by mutableStateOf(true)

const val SHARED_PREFERENCES_FILE = "accessFile"
const val EMAIL_FIELD = "EMAIL"
const val PASSWORD_FIELD = "PASSWORD"
const val STARTUP_LANGUAGE_FIELD = "STARTUP_LANG"

const val ADMIN_MIN_WEEK = "AD_MIN_WEEK"
const val ADMIN_MAX_WEEK = "AD_MAX_WEEk"
const val ADMIN_MIN_DAY = "AD_MIN_DAY"
const val ADMIN_MAX_DAY = "AD_MAX_DAY"

const val SELECTED_THEME = "SELECTED_THEME"

fun cleanGlobalAppData(){
    globalUser = null
    globalAllUsers = mutableListOf()
}