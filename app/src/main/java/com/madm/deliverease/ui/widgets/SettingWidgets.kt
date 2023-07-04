package com.madm.deliverease.ui.widgets

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.madm.deliverease.*
import com.madm.deliverease.R
import com.madm.deliverease.ui.theme.*


/**
 * Settings page for the rider page
 */
@Composable
fun PreferencesSetting(logoutCallback: () -> Unit){
    val context = LocalContext.current
    Column(
        Modifier.padding(nonePadding, smallPadding),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        CustomDivider(stringResource(R.string.setting))
        Language()
        DarkMode()
        CustomDivider(stringResource(R.string.general))
        SettingRow(
            stringResource(R.string.report_bug),
            painterResource(id = R.drawable.bug)
        ) {
            val urlIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://bughunters.google.com/report")
            )

            context.startActivity(urlIntent)
        }
        SettingRow(
            stringResource(R.string.terms_conditions),
            painterResource(id = R.drawable.terms_and_conditions)
        ) {
            val urlIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://policies.google.com/terms?hl=it")
            )

            context.startActivity(urlIntent)
        }
        LogOut(logoutCallback)
    }
}

/**
 * Divider for the different sections of the setting page
 * @param text the name of the following section
 */
@Composable
fun CustomDivider(text: String ){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = CustomTheme.colors.backgroundVariant)
            .padding(smallPadding, extraSmallPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text,
            style = CustomTheme.typography.h3,
            color = CustomTheme.colors.onBackgroundVariant
        )
    }
}

/**
 * Application language selector
 */
@Preview
@Composable
fun Language(){
    val context: Context = LocalContext.current

    // Retrieves the file of the shared preferences
    val sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE)
    // Retrieves from the shared preferences the language set last time
    val startupLanguage = sharedPreferences.getString(STARTUP_LANGUAGE_FIELD, "")!!
    // Checks if every application language must be visible in order to select the wanted one
    var expanded by remember { mutableStateOf(false) }
    // List of the application languages
    val items = listOf(stringResource(R.string.english), stringResource(R.string.italian))
    var selectedIndex by remember {
        mutableStateOf(
            if(startupLanguage.isBlank() || startupLanguage == "en") 0
            else 1
        )
    }

    Box(Modifier.fillMaxWidth()){
        Row(
            Modifier.padding(smallPadding),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.languages),
                modifier = Modifier.size(23.dp),
                contentDescription = "language icon",
                tint = CustomTheme.colors.onBackground
            )
            Divider(Modifier.width(smallPadding), Color.Transparent)
            Text(
                stringResource(R.string.language),
                style = CustomTheme.typography.body1,
                color = CustomTheme.colors.onBackground
            )
        }


        Box(modifier = Modifier
            .align(Alignment.CenterEnd)
            .width(100.dp)
            .padding(end = 10.dp, top = 8.dp))
        {
            Text(
                items[selectedIndex],
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = { expanded = true })
                    .background(CustomTheme.colors.backgroundVariant)
                    .padding(start = 20.dp),
                style = CustomTheme.typography.body1,
                color = CustomTheme.colors.onBackgroundVariant
            )

            DropdownMenu( // menu with every language of the application
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(CustomTheme.colors.backgroundVariant)
            ) {
                items.forEachIndexed { index, s ->
                    DropdownMenuItem(onClick = {
                        selectedIndex = index
                        expanded = false

                        if(s == "English") switchLanguage("en", context)
                        else switchLanguage("it", context)
                    }) {
                        Text(text = s, style = CustomTheme.typography.body1, color = CustomTheme.colors.onBackgroundVariant)
                    }
                }
            }
        }
    }
}


/**
 * Switches the application language to the one specified in languageCode parameter
 * @param languageCode it for italian, en of english
 * @param context the context of application
 */
fun switchLanguage(languageCode: String, context: Context) {
    val sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()

    // setting up the default language inside internal storage
    editor.putString(STARTUP_LANGUAGE_FIELD, languageCode)
    editor.apply()

    // Restart the activity to apply the new language
    val intent = Intent(context, MainActivity::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    context.startActivity(intent)
}

/**
 * Application scheme color selector (for light/dark mode)
 */
@Composable
fun DarkMode(){
    val context : Context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE)

    val savedTheme = sharedPreferences.getString(SELECTED_THEME, if(isSystemInDarkTheme()) "dark" else "light")
    var switchCheckedState by remember { mutableStateOf(savedTheme != "light") }

    Box(Modifier.fillMaxWidth()){
        Row(
            Modifier.padding(smallPadding),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.dark_theme),
                modifier = Modifier.size(28.dp),
                contentDescription = "change theme",
                tint = CustomTheme.colors.onBackground
            )
            Divider(Modifier.width(smallPadding), Color.Transparent)
            Text(
                stringResource(R.string.dark_mode),
                style = CustomTheme.typography.body1,
                color = CustomTheme.colors.onBackground
            )
        }

        Row(
            Modifier
                .fillMaxWidth()
                .padding(largePadding, nonePadding),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Switch(
                checked = switchCheckedState,
                onCheckedChange = {
                    switchCheckedState = it

                    val sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()

                    editor.putString(SELECTED_THEME, if(it) "dark" else "light")
                    editor.apply()

                    darkMode = it
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = CustomTheme.colors.secondary,
                    checkedTrackColor = CustomTheme.colors.secondaryVariant,
                    uncheckedThumbColor = CustomTheme.colors.backgroundVariant,
                    uncheckedTrackColor = CustomTheme.colors.backgroundVariant,
                ),
            )
        }
    }
}

/**
 * @param title name of the setting
 * @param icon icon representing the setting
 * @param onClick lambda to run setting clicked
 */
@Composable
fun SettingRow(
    title: String,
    icon: Painter,
    onClick: () -> Unit = {}
){
    Box(Modifier.fillMaxWidth()) {
        Row(
            Modifier.padding(smallPadding),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = icon,
                modifier = Modifier.size(24.dp),
                contentDescription = "bug",
                tint = CustomTheme.colors.onBackground
            )
            Divider(Modifier.width(smallPadding), Color.Transparent)
            Text(
                title,
                style = CustomTheme.typography.body1,
                color = CustomTheme.colors.onBackground
            )
        }

        Row(
            Modifier
                .fillMaxWidth()
                .padding(largePadding, nonePadding),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onClick,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color.Transparent)
                    .padding(smallPadding)
                    .size(30.dp)
            ) {
                Icon(
                    painterResource(id = R.drawable.next_icon),
                    contentDescription = "next screen",
                    tint = CustomTheme.colors.onBackground
                )
            }
        }
    }
}

/**
 * Button for logging out the application at the end of settings
 * @param logoutCallback lambda to logout the user and return the home page
 */
@Composable
fun LogOut(logoutCallback: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = logoutCallback,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = CustomTheme.colors.primary,
                contentColor = CustomTheme.colors.onPrimary
            ),
            shape = CustomTheme.shapes.large
        ) {
            Icon(
                painter = painterResource(id = R.drawable.log_out),
                contentDescription = "logout",
                modifier = Modifier
                    .padding(smallPadding, nonePadding)
                    .size(25.dp),
            )
            Text(
                text = stringResource(R.string.logout),
                modifier = Modifier.padding(smallPadding, nonePadding),
                style = CustomTheme.typography.body1,
            )
        }
    }
}

