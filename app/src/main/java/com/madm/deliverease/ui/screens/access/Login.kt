package com.madm.deliverease.ui.screens.access

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.madm.deliverease.R
import com.madm.deliverease.ui.theme.gilroy
import com.madm.deliverease.ui.theme.mediumPadding
import com.madm.deliverease.ui.theme.nonePadding
import com.madm.deliverease.ui.theme.thinPadding

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
            painter = painterResource(id = R.drawable.logo_dark),
            contentDescription = "logo",
            modifier = Modifier
                .size((LocalConfiguration.current.screenWidthDp.dp), 100.dp)
                .padding(mediumPadding),
            contentScale = ContentScale.FillHeight
        )

        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Username",
                fontFamily = gilroy,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xff131b31)
            )
        }
        TextField(
            modifier = Modifier
                .padding(nonePadding, nonePadding, nonePadding, mediumPadding)
                .border(BorderStroke(2.dp, Color(0xFFD8D8D8)), shape = RoundedCornerShape(50))
                .clip(RoundedCornerShape(50))
                .fillMaxWidth(),
            value = username,
            textStyle = TextStyle(
                fontFamily = gilroy,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xff131b31)
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
            onValueChange = { username = it },
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color(0xff131b31),
                backgroundColor = Color.Transparent,
                cursorColor = Color(0xff131b31),
                focusedIndicatorColor = Color.Transparent
            )
        )

        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Password",
                fontFamily = gilroy,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xff131b31)
            )
        }
        TextField(
            modifier = Modifier
                .padding(nonePadding, nonePadding, nonePadding, mediumPadding)
                .border(BorderStroke(2.dp, Color(0xFFD8D8D8)), shape = RoundedCornerShape(50))
                .clip(RoundedCornerShape(50))
                .fillMaxWidth(),
            value = password,
            textStyle = TextStyle(
                fontFamily = gilroy,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xff131b31)
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
            onValueChange = { password = it },
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color(0xff131b31),
                backgroundColor = Color.Transparent,
                cursorColor = Color(0xff131b31),
                focusedIndicatorColor = Color.Transparent
            )
        )

        Button(
            onClick = { },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = mediumPadding, end = mediumPadding, bottom = mediumPadding)
                .clip(RoundedCornerShape(50))
                .border(BorderStroke(2.dp, Color(0xFFD8D8D8)), shape = RoundedCornerShape(50)),
            shape = RoundedCornerShape(6.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Transparent,
                contentColor = Color(0xff131b31)
            ),
            elevation = ButtonDefaults.elevation(0.dp,0.dp),
            enabled = username.isNotBlank() && password.isNotBlank()
        ) {
            Row (
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(text = "Login", modifier = Modifier.padding(6.dp))
                Icon(Icons.Default.ArrowForward, contentDescription = "Continue")
            }
        }

        Divider(
            color = Color(0xFFD8D8D8),
            thickness = 1.dp,
            modifier = Modifier.padding(mediumPadding)
        )

        Button(
            onClick = { },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = mediumPadding, end = mediumPadding, bottom = mediumPadding)
                .border(BorderStroke(2.dp, Color(0xFFD8D8D8)), shape = RoundedCornerShape(50)),
            shape = RoundedCornerShape(6.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Transparent,
                contentColor = Color(0xff131b31),

            ),
            elevation = ButtonDefaults.elevation(0.dp,0.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.google),
                contentDescription = "Login with Google",
                modifier = Modifier.size(20.dp)
            )
            Text(text = "Login with Google", modifier = Modifier.padding(6.dp))
        }

        Button(
            onClick = { },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = mediumPadding, end = mediumPadding, bottom = mediumPadding)
                .border(BorderStroke(2.dp, Color(0xFFD8D8D8)), shape = RoundedCornerShape(50)),
            shape = RoundedCornerShape(6.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Transparent,
                contentColor = Color(0xff131b31),

                ),
            elevation = ButtonDefaults.elevation(0.dp,0.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.google),
                contentDescription = "Login with Apple",
                modifier = Modifier.size(20.dp)
            )
            Text(text = "Login with Apple", modifier = Modifier.padding(6.dp))
        }
    }
}