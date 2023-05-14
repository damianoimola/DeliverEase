package com.madm.deliverease.ui.screens.access

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.madm.deliverease.R
import com.madm.deliverease.ui.theme.gilroy
import com.madm.deliverease.ui.theme.mediumPadding
import com.madm.deliverease.ui.theme.nonePadding
import com.madm.deliverease.ui.widgets.CustomOutlinedTextField

@Composable
fun LoginScreen(){
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(mediumPadding),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "logo",
            modifier = Modifier
                .size((LocalConfiguration.current.screenWidthDp.dp) * 8 / 10, 100.dp)
                .padding(10.dp),
            contentScale = ContentScale.FillWidth
        )

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Username") },
            modifier = Modifier.padding(nonePadding, mediumPadding),
            textStyle = TextStyle(
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
            ),
            maxLines = 1,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Ascii,
                imeAction = ImeAction.Done // Done, not Enter
            ),
            keyboardActions = KeyboardActions(
                onDone = {focusManager.clearFocus()},
            ),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedLabelColor = MaterialTheme.colors.background,
                focusedBorderColor = MaterialTheme.colors.background,
                unfocusedLabelColor = MaterialTheme.colors.background,
                unfocusedBorderColor = MaterialTheme.colors.background,
                backgroundColor = Color.Transparent,
                textColor = MaterialTheme.colors.background,
                leadingIconColor = MaterialTheme.colors.background
            )
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            leadingIcon = { Icon(Icons.Default.Star, contentDescription = "Password") },
            modifier = Modifier
                .padding(nonePadding, mediumPadding),

            textStyle = TextStyle(
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
            ),
            maxLines = 1,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done // Done, not Enter
            ),
            keyboardActions = KeyboardActions(
                onDone = {focusManager.clearFocus()},
            ),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedLabelColor = MaterialTheme.colors.background,
                focusedBorderColor = MaterialTheme.colors.background,
                unfocusedLabelColor = MaterialTheme.colors.background,
                unfocusedBorderColor = MaterialTheme.colors.background,
                backgroundColor = Color.Transparent,
                textColor = MaterialTheme.colors.background,
                leadingIconColor = MaterialTheme.colors.background
            )
        )

        Divider(
            color = MaterialTheme.colors.background,
            thickness = 1.dp,
            modifier = Modifier.padding(mediumPadding)
        )

        Text(
            text = "Forgot Password",
            style = TextStyle(
                fontSize = 15.sp,
                fontFamily = gilroy,
                color = MaterialTheme.colors.background,
                fontWeight = FontWeight.Medium
            )
        )
    }
}