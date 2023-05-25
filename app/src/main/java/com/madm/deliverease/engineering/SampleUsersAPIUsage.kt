package com.madm.deliverease.engineering

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.madm.common_libs.model.User
import com.madm.common_libs.model.UserManager


@Composable
fun DummyComposeFunctionForUsers(){
    val context = LocalContext.current
    var users: List<User> by remember { mutableStateOf(listOf()) }

    Button(
        onClick = {
            testHandleUsers(
                { list -> users = list },
                context
            )
        }
    ) {
        Text("CLICK ME")
    }

    ReceivedUsers(users)
}



fun testHandleUsers(
    receivedUserCallback: (List<User>) -> Unit,
    context: Context
) {
    val userManager : UserManager = UserManager(context)

    userManager.getUsers{ receivedUserCallback(it) }
}

@Composable
fun ReceivedUsers(users: List<User>) {
    Column() {
        users.forEach{
            Text(text = "Nome: ${it.name}, ID: ${it.id}")
        }
    }
}