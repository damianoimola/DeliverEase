package com.madm.deliverease.ui.widgets

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.madm.deliverease.R
import com.madm.deliverease.ui.theme.gilroy
import com.madm.deliverease.ui.theme.mediumPadding
import com.madm.deliverease.ui.theme.smallPadding


@Composable
fun MyOutlinedTextField(
    field: MutableState<String>,
    isError: MutableState<Boolean>,
    label: String = "",
    onDone: () -> Unit = {}
){
    OutlinedTextField(
        value = field.value,
        onValueChange = { field.value = it },
        modifier = Modifier
            .padding(bottom = smallPadding)
            /*.border(
                BorderStroke(2.dp, Color(0xFFD8D8D8)),
                shape = RoundedCornerShape(50)
            )
            .clip(RoundedCornerShape(50))*/
            .fillMaxWidth(),
        label = {
            Text(text = label)
        },
        enabled = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Ascii,
            imeAction = ImeAction.Done // Done, not Enter
        ),
        keyboardActions = KeyboardActions(
            onDone = {onDone},
        ),
        singleLine = true,
        maxLines = 1,
        textStyle = TextStyle(
            fontFamily = gilroy,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xff131b31)
        ),
        shape = RoundedCornerShape(20.dp),
        colors = TextFieldDefaults.textFieldColors(
            textColor = Color(0xff131b31),
            backgroundColor = Color.Transparent,
            cursorColor = Color(0xff131b31),
            focusedIndicatorColor = Color.Gray
        ),
        isError = isError.value,
    )
}

@Composable
fun MyButton(
    onClick: () -> Unit = {},
    text: String,
    imgId: Int){
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = mediumPadding, end = mediumPadding, bottom = mediumPadding)
            .border(BorderStroke(2.dp, Color(0xFFD8D8D8)), shape = RoundedCornerShape(50)),
        shape = RoundedCornerShape(6.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.Transparent,
            contentColor = Color(0xff131b31),

            ),
        elevation = ButtonDefaults.elevation(0.dp, 0.dp)
    ) {
        Image(
            painter = painterResource(id = imgId),
            contentDescription = text,
            modifier = Modifier.size(20.dp)
        )
        Text(text = text, modifier = Modifier.padding(6.dp))
    }
}

@Composable
fun LoginButton(
    username: MutableState<String>,
    password: MutableState<String>,
    isError: MutableState<Boolean>,
    goToRiderHome: () -> Unit,
    goToAdminHome: () -> Unit,
    onClick: () -> Unit
){
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = smallPadding)
            .clip(RoundedCornerShape(50))
            .border(
                BorderStroke(2.dp, Color(0xFFD8D8D8)),
                shape = RoundedCornerShape(50)
            ),
        shape = RoundedCornerShape(6.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.Transparent,
            contentColor = Color(0xff131b31)
        ),
        elevation = ButtonDefaults.elevation(0.dp, 0.dp),
        enabled = username.value.isNotBlank() && password.value.isNotBlank(),
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Login", modifier = Modifier.padding(6.dp))
            Icon(Icons.Default.ArrowForward, contentDescription = stringResource(R.string._continue))
        }
    }
}