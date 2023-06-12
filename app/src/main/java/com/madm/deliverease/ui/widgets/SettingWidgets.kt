package com.madm.deliverease.ui.widgets


import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.madm.deliverease.*
import com.madm.deliverease.R
import com.madm.deliverease.ui.theme.*
import java.util.*


@Composable
fun PreferencesSetting(logoutCallback: () -> Unit){
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
        )
        SettingRow(
            stringResource(R.string.terms_conditions),
            painterResource(id = R.drawable.terms_and_conditions)
        )
        LogOut(logoutCallback)
    }
}

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
            style = CustomTheme.typography.h4,
            color = CustomTheme.colors.onBackgroundVariant
        )
    }
}

@Preview
@Composable
fun Language(){
    val context: Context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }
    val items = listOf(stringResource(R.string.english), stringResource(R.string.italian))
    var selectedIndex by remember { mutableStateOf(0) }

    Box(Modifier.fillMaxWidth()){
        Row(
            Modifier.padding(smallPadding),
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.languages),
                modifier = Modifier.size(25.dp),
                contentDescription = "language icon",
                tint = CustomTheme.colors.onBackground
            )
            Divider(Modifier.width(smallPadding), Color.Transparent)
            Text(
                stringResource(R.string.language),
                style = CustomTheme.typography.h4,
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

            DropdownMenu(
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




@Composable
fun DarkMode(){
    var switchCheckedState by remember { mutableStateOf(false) }

    Box(Modifier.fillMaxWidth()){
        Row(
            Modifier.padding(smallPadding),
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.dark_theme),
                modifier = Modifier.size(25.dp),
                contentDescription = "change theme",
                tint = CustomTheme.colors.onBackground
            )
            Divider(Modifier.width(smallPadding), Color.Transparent)
            Text(
                stringResource(R.string.dark_mode),
                style = CustomTheme.typography.h4,
                color = CustomTheme.colors.onBackground
            )
        }

        Row(
            Modifier
                .fillMaxWidth()
                .padding(largePadding, nonePadding),
            horizontalArrangement = Arrangement.End
        ) {
            Switch(
                checked = switchCheckedState,
                onCheckedChange = { switchCheckedState = it },
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

@Composable
fun SettingRow(
    title: String,
    icon: Painter,
    onClick: () -> Unit = {}
){
    Box(Modifier.fillMaxWidth()) {
        Row(
            Modifier.padding(smallPadding),
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = icon,
                modifier = Modifier.size(25.dp),
                contentDescription = "bug",
                tint = CustomTheme.colors.onBackground
            )
            Divider(Modifier.width(mediumPadding), Color.Transparent)
            Text(
                title,
                style = CustomTheme.typography.h4,
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

@Composable
fun TermsAndConditions(){
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(40.dp)
        .clickable { /*TODO*/ }){
        Icon(painter = painterResource(id = R.drawable.terms_and_conditions), contentDescription = "terms and conditions",
            modifier = Modifier
                .padding(start = 12.dp, top = 1.dp)
                .size(28.dp)
                .align(Alignment.CenterStart))
        Text(
                    stringResource(R.string.terms_conditions), modifier = Modifier
                .padding(start = 48.dp, top = 0.dp)
                .align(Alignment.CenterStart),
            style = TextStyle(
                fontFamily = gilroy,
                fontSize = 18.sp,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.SemiBold
            ))

        Icon(painter = painterResource(id = R.drawable.next_icon), contentDescription = "next screen",
            modifier = Modifier
                .padding(end = 4.dp, top = 10.dp)
                .size(44.dp)
                .align(Alignment.CenterEnd))
    }
}

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
            )
        ) {
            Icon(
                painter = painterResource(id = R.drawable.log_out),
                contentDescription = "logout",
                modifier = Modifier
                    .padding(smallPadding)
                    .size(28.dp),
            )
            Text(
                text = stringResource(R.string.logout),
                modifier = Modifier.padding(smallPadding, nonePadding),
                style = CustomTheme.typography.h4,
            )
        }
    }
}

