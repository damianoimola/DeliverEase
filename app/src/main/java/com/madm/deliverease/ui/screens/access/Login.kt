package com.madm.deliverease.ui.screens.access

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.madm.common_libs.model.User
import com.madm.common_libs.model.UserManager
import com.madm.deliverease.*
import com.madm.deliverease.R
import com.madm.deliverease.ui.theme.CustomTheme
import com.madm.deliverease.ui.theme.largePadding
import com.madm.deliverease.ui.theme.mediumPadding
import com.madm.deliverease.ui.widgets.LoginButton
import com.madm.deliverease.ui.widgets.MyButton
import com.madm.deliverease.ui.widgets.MyOutlinedTextField
import com.madm.deliverease.ui.widgets.PizzaLoaderDialog


/**
 * The screen appearing at the start of application to allow user to log in
 * @param goToAdminHome lambda to login as an admin and go to their homepage
 * @param goToRiderHome lambda to login as a rider and go to their homepage
 */
@Composable
fun LoginScreen(
    goToRiderHome: () -> Unit,
    goToAdminHome: () -> Unit,
){
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
            Icon( // deliverease icon
                imageVector = ImageVector.vectorResource(id = R.drawable.deliverease_icon),
                contentDescription = "logo",
                modifier = Modifier
                    .padding(mediumPadding)
                    .size(70.dp),
                tint = CustomTheme.colors.onBackground
            )
            Text( // deliverease title
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

/**
 * To login via email and password
 * @param goToAdminHome lambda to login as an admin and go to their homepage
 * @param goToRiderHome lambda to login as a rider and go to their homepage
 */
@Composable
fun ClassicLogin(
    goToRiderHome: () -> Unit,
    goToAdminHome: () -> Unit
) {
    val isPlaying = rememberSaveable { mutableStateOf (false) } // to manage pizza loader
    val username = rememberSaveable { mutableStateOf("") } // username of the user
    val password = rememberSaveable { mutableStateOf("") } // password of the user
    var isError by rememberSaveable { mutableStateOf(false) } // to show if login went wrong
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    val user: MutableState<User?> = rememberSaveable { mutableStateOf(null) }

    // when reconstructing the Classic login if the user is set goes to their homepage
    if(user.value != null){
        globalUser = user.value
        when (globalUser!!.id) {
            "0" -> goToAdminHome()
            else -> goToRiderHome()
        }
    }

    // if a user has previously logged in without logging out, they will access their profile directly
    directAccess(context, user, focusManager, isPlaying)

    // shows the pizza loader during the log in process
    if (isPlaying.value && !(isError)) {
        PizzaLoaderDialog(isPlaying = isPlaying)
    }

    Column(Modifier.padding(mediumPadding), horizontalAlignment = Alignment.CenterHorizontally) {
        MyOutlinedTextField( // to insert the username
            field = username,
            isError = isError,
            label = stringResource(R.string.username),
            onDone = { focusManager.clearFocus() })
        MyOutlinedTextField( // to insert the password
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

        LoginButton( // to run the login
            username = username,
            password = password,
            onClick = {
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

/**
 * To login with google or via apple
 */
@Preview
@Composable
fun ThirdPartyLogin(){
    val ctx = LocalContext.current
    Column {
        MyButton(
            onClick = {
                val urlIntent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.google.it/"))

                ctx.startActivity(urlIntent)
            },
            text = stringResource(R.string.google_login),
            imgId = R.drawable.google
        )
        MyButton(
            onClick = {
                val urlIntent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.apple.com/"))

                ctx.startActivity(urlIntent)
            },
            text = stringResource(R.string.apple_login),
            imgId = R.drawable.apple
        )
    }
}

/**
 * Automatically logs in the user saved in the shared preferences
 * @param context the context of the application
 * @param user the user to log in
 * @param focusManager the focusManager for clearing the focus
 * @param isPlaying to manage the pizza loader
 */
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

/**
 * Remembers the email and password of the user to allow login to be automatic the next time
 * @param context the context of the application
 * @param user the user whose email and password want to be saved
 */
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

