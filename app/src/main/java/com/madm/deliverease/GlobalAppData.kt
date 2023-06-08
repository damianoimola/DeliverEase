package com.madm.deliverease

import com.madm.common_libs.model.User

var globalUser : User? = null
var globalAllUsers : MutableList<User> = mutableListOf()


fun cleanGlobalAppData(){
    globalUser = null
    globalAllUsers = mutableListOf()
}