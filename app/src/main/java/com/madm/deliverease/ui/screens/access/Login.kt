package com.madm.deliverease.ui.screens.access

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.madm.common_libs.internal_storage_manager.deleteDraftDays
import com.madm.common_libs.internal_storage_manager.retrieveDraftCalendar
import com.madm.common_libs.internal_storage_manager.saveDraftCalendar
import com.madm.common_libs.model.Calendar
import com.madm.common_libs.model.User
import com.madm.common_libs.model.UserManager
import com.madm.common_libs.model.WorkDay
import com.madm.deliverease.*
import com.madm.deliverease.R
import com.madm.deliverease.globalAllUsers
import com.madm.deliverease.globalUser
import com.madm.deliverease.ui.theme.*
import com.madm.deliverease.ui.widgets.*
import java.util.*


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
            .clickable(
                indication = null,
                interactionSource = interactionSource,
                onClick = { focusManager.clearFocus() }
            )
            .background(CustomTheme.colors.background)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                //.size((LocalConfiguration.current.screenWidthDp.dp), 100.dp)
                .padding(mediumPadding)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.deliverease_icon),
                contentDescription = "logo",
                modifier = Modifier
                    .padding(mediumPadding)
                    .size(70.dp),
                tint = CustomTheme.colors.onBackground
            )
            Text(
                text = "DeliverEase",
                style = CustomTheme.typography.h1,
                color = CustomTheme.colors.onBackground
            )
        }

        ClassicLogin(goToRiderHome, goToAdminHome)

        Divider(
            color = CustomTheme.colors.onBackgroundVariant,
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
    val user: MutableState<User?> = remember { mutableStateOf(null) }

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

    Column(Modifier.padding(mediumPadding), horizontalAlignment = Alignment.CenterHorizontally) {
        MyOutlinedTextField(
            field = username,
            isError = isError,
            label = stringResource(R.string.username),
            onDone = { focusManager.clearFocus() })
        MyOutlinedTextField(
            field = password,
            isError = isError,
            label = stringResource(R.string.password),
            onDone = { focusManager.clearFocus() },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation()
        )

        if(isError) {
            Text(
                stringResource(R.string.wrong_username_password),
                style = CustomTheme.typography.body1,
                color = CustomTheme.colors.error
            )
            Text(
                stringResource(R.string.please_try_again),
                style = CustomTheme.typography.body1,
                color = CustomTheme.colors.error
            )
        }

        LoginButton(
            username = username,
            password = password,
            onClick = {
//                val w1 = WorkDay(listOf("1", "2"))
//                w1.workDayDate = Date(2023, 6, 28)
//                val w2 = WorkDay(listOf("1", "3"))
//                w2.workDayDate = Date(2023, 6, 27)
//                val w3 = WorkDay(listOf("2", "3"))
//                w3.workDayDate = Date(2023, 6, 26)
//                val w4 = WorkDay(listOf("3"))
//                w4.workDayDate = Date(2023, 6, 25)
//
//                val c = Calendar(listOf(w1, w2, w3, w4))
//
//                saveDraftCalendar(context, c)
//
//                println(retrieveDraftCalendar(context))
//
//                deleteDraftDays(context, listOf(w3))
//
//                println(retrieveDraftCalendar(context))

                focusManager.clearFocus()
                isError = false
                isPlaying.value = true
                val userManager = UserManager(context)
                val isOnline = userManager.getUsers { list ->
                    user.value = list.firstOrNull { user ->
                        (user.email == username.value) && (user.password == password.value)
                    }
                    globalAllUsers = list
                    saveAccess(context, user.value)

                    // to show login animation
                    Thread.sleep(2500)
                    isError = (user.value == null)
                }

                if(!isOnline) isPlaying.value = false
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
    isPlaying: MutableState<Boolean>
){
    val sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE)
    val usernameSaved = sharedPreferences.getString(EMAIL_FIELD, "")
    val passwordSaved = sharedPreferences.getString(PASSWORD_FIELD, "")

    if(usernameSaved != "" && passwordSaved != ""){
        focusManager.clearFocus()
        isPlaying.value = true
        val userManager = UserManager(context)
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
    user: User?
){
    val sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()

    editor.putString(EMAIL_FIELD, user?.email)
    editor.putString(PASSWORD_FIELD, user?.password)

    editor.apply()
}

