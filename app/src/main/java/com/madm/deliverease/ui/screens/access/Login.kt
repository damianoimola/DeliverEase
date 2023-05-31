package com.madm.deliverease.ui.screens.access

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.madm.common_libs.model.User
import com.madm.common_libs.model.UserManager
import com.madm.deliverease.R
import com.madm.deliverease.globalAllUsers
import com.madm.deliverease.ui.theme.largePadding
import com.madm.deliverease.ui.theme.mediumPadding
import com.madm.deliverease.globalUser
import com.madm.deliverease.ui.widgets.*

@Composable
fun LoginScreen(
    goToRiderHome: () -> Unit,
    goToAdminHome: () -> Unit,
){
    val focusManager = LocalFocusManager.current
    var isPlaying = rememberSaveable { mutableStateOf (false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(mediumPadding)
            .clickable { focusManager.clearFocus() },
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_dark),
            contentDescription = "logo",
            modifier = Modifier
                .size((LocalConfiguration.current.screenWidthDp.dp), 100.dp)
                .padding(mediumPadding),
            contentScale = ContentScale.FillHeight
        )

        ClassicLogin(goToRiderHome, goToAdminHome, isPlaying)

        Divider(
            color = Color(0xFFD8D8D8),
            thickness = 1.dp,
            modifier = Modifier.padding(largePadding)
        )

        ThirdPartyLogin()
    }
}

@Composable
fun ClassicLogin(
    goToRiderHome: () -> Unit,
    goToAdminHome: () -> Unit,
    isPlaying: MutableState<Boolean>) {
    val username = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val isError = remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    var users: List<User> by remember { mutableStateOf(listOf()) }

    if(users.isNotEmpty() && users.count() == 1){       //todo: fix this scempio
        globalUser = users.first()
        when (globalUser!!.id) {
            "0" -> goToAdminHome()
            else -> goToRiderHome()
        }
    }

    if(isPlaying.value)
        PizzaLoaderDialog(isPlaying = isPlaying)

    Column {
        MyOutlinedTextField(
            field = username,
            isError = isError,
            label = stringResource(R.string.username),
            onDone = { focusManager.clearFocus() })
        MyOutlinedTextField(
            field = password,
            isError = isError,
            label = stringResource(R.string.password),
            onDone = { focusManager.clearFocus() })

        LoginButton(
            username = username,
            password = password,
            isError = isError,
            goToRiderHome = goToRiderHome,
            goToAdminHome = goToAdminHome,
            onClick = {
                focusManager.clearFocus()
                isPlaying.value = true
                val userManager: UserManager = UserManager(context)
                userManager.getUsers { list ->
                    users = list.filter { user -> (user.email == username.value) && (user.password == password.value) }
                    globalAllUsers = list
                }


                //                userManager.getUsers{ list ->
                //
                //                    val filteredList = list.filter { user -> (user.email == username.value) && (user.password == password.value)}
                //
                //                    println("############ LISTA $filteredList")
                //                    if (filteredList.count() == 1) {
                //
                //                        println("############ DETAILS ${filteredList.count()} ${filteredList.first().id}")
                //                        when (filteredList.first().id) {
                //                            "0" -> goToAdminHome()
                //                            else -> goToRiderHome()
                //                        }
                //                    }
                //                    else isError.value = true
                //                }
            }
        )
    }
}


@Preview
@Composable
fun ThirdPartyLogin(){
    Column {
        MyButton(
            onClick = {},
            text = stringResource(R.string.google_login),
            imgId = R.drawable.google
        )
        MyButton(
            onClick = {},
            text = stringResource(R.string.apple_login),
            imgId = R.drawable.apple
        )
    }
}