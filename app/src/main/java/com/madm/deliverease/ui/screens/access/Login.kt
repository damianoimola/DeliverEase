package com.madm.deliverease.ui.screens.access

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.madm.common_libs.model.User
import com.madm.common_libs.model.UserManager
import com.madm.deliverease.*
import com.madm.deliverease.R
import com.madm.deliverease.globalAllUsers
import com.madm.deliverease.ui.theme.largePadding
import com.madm.deliverease.ui.theme.mediumPadding
import com.madm.deliverease.globalUser
import com.madm.deliverease.ui.theme.CustomTheme
import com.madm.deliverease.ui.theme.gilroy
import com.madm.deliverease.ui.widgets.*

@Composable
fun LoginScreen(
    goToRiderHome: () -> Unit,
    goToAdminHome: () -> Unit,
){
    println("########### LOGIN") // TODO Ralisin: remove on deploy version
    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(mediumPadding)
            .clickable (
                indication = null,
                interactionSource = interactionSource,
                onClick = { focusManager.clearFocus() }
            )
            .background(CustomTheme.colors.background),
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
            color = Color(0xFFD8D8D8), // TODO Ralisin: set color with theme
            thickness = 1.dp,
            modifier = Modifier.padding(largePadding)
        )

        ThirdPartyLogin()
    }
}

@Composable
fun ClassicLogin(
    goToRiderHome: () -> Unit,
    goToAdminHome: () -> Unit
) {
    val isPlaying = rememberSaveable { mutableStateOf (false) }
    val username = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    val user: MutableState<User?> = remember { mutableStateOf<User?>(null) }

    if(user.value != null){
        globalUser = user.value
        when (globalUser!!.id) {
            "0" -> goToAdminHome()
            else -> goToRiderHome()
        }
    }

    directAccess(context, user, focusManager, isPlaying)

    if (isPlaying.value && !(isError)) {
        PizzaLoaderDialog(isPlaying = isPlaying)
    }

    Column(Modifier.padding(mediumPadding)) {
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

        if(isError)
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "Wrong Username or Password.\nPlease try again.",
                    style = TextStyle(
                        textAlign = TextAlign.Center,
                        color = Color.Red,
                        fontFamily = gilroy
                    )
                )
            }

        LoginButton(
            username = username,
            password = password,
            onClick = {
                focusManager.clearFocus()
                isError = false
                isPlaying.value = true
                val userManager: UserManager = UserManager(context)
                userManager.getUsers { list ->
                    user.value = list.firstOrNull { user ->
                        (user.email == username.value) && (user.password == password.value)
                    }
                    globalAllUsers = list
                    saveAccess(context, user.value)

                    // to show login animation
                    Thread.sleep(2500)
                    isError = (user.value == null)
                }
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

fun directAccess(
    context: Context,
    user: MutableState<User?>,
    focusManager: FocusManager,
    isPlaying: MutableState<Boolean>){
    val sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE)
    val usernameSaved = sharedPreferences.getString(EMAIL_FIELD, "")
    val passwordSaved = sharedPreferences.getString(PASSWORD_FIELD, "")

    if(usernameSaved != "" && passwordSaved != ""){
        focusManager.clearFocus()
        isPlaying.value = true
        val userManager: UserManager = UserManager(context)
        userManager.getUsers { list ->
            user.value = list.firstOrNull { user ->
                (user.email == usernameSaved) && (user.password == passwordSaved)
            }
            globalAllUsers = list

            // to show login animation
            Thread.sleep(2500)
        }
    }
}

fun saveAccess(
    context: Context,
    user: User?){
    val sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()

    editor.putString(EMAIL_FIELD, user?.email)
    editor.putString(PASSWORD_FIELD, user?.password)

    editor.apply()
}