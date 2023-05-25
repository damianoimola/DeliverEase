package com.madm.deliverease.ui.screens.access

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.madm.deliverease.R
import com.madm.deliverease.engineering.DummyComposeFunctionForCalendar
import com.madm.deliverease.engineering.DummyComposeFunctionForMessages
import com.madm.deliverease.ui.theme.largePadding
import com.madm.deliverease.ui.theme.mediumPadding
import com.madm.deliverease.ui.widgets.LoginButton
import com.madm.deliverease.ui.widgets.MyButton
import com.madm.deliverease.ui.widgets.MyOutlinedTextField

@Composable
fun LoginScreen(
    goToRiderHome: () -> Unit,
    goToAdminHome: () -> Unit,
){

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(mediumPadding),
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

        ClassicLogin(goToRiderHome, goToAdminHome)

        Divider(
            color = Color(0xFFD8D8D8),
            thickness = 1.dp,
            modifier = Modifier.padding(largePadding)
        )

        ThirdPartyLogin()
    }
}

@Composable
fun ClassicLogin(goToRiderHome: () -> Unit, goToAdminHome: () -> Unit) {
    val username = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val isError = remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

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
                /* TODO: dummy */
                if (username.value == "rider")
                    goToRiderHome()
                else if (username.value == "admin")
                    goToAdminHome()
                else
                    isError.value = true
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
            imgId = R.drawable.google
        )
    }
}