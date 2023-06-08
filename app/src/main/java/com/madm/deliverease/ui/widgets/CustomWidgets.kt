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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.madm.deliverease.R
import com.madm.deliverease.ui.theme.*

@Composable
fun MyPageHeader(modifier: Modifier = Modifier){
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = smallPadding),
    ){
        Image(
            painter = painterResource(id = R.drawable.logo_dark_icon_hd),
            contentDescription = "Logo",//stringResource(R.string.logo),
            modifier = Modifier
                .size((LocalConfiguration.current.screenWidthDp.dp), 50.dp)
                .padding(0.dp),
            contentScale = ContentScale.FillHeight,
            alignment = Alignment.CenterStart,
        )
        Text(
            text = stringResource(R.string.app_name),
            modifier = Modifier.fillMaxWidth(),
            style = TextStyle(
                textAlign = TextAlign.Center,
                fontSize = 35.sp,
                fontFamily = gilroy,
                fontWeight = FontWeight.Bold,
                color = Color.Black // TODO Ralisin: set text color
            )

        )
    }
}


@Composable
fun MyOutlinedTextField(
    field: MutableState<String>,
    isError: Boolean,
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
        textStyle = CustomTheme.typography.h5,
        shape = RoundedCornerShape(20.dp),
        colors = TextFieldDefaults.textFieldColors(
            textColor = CustomTheme.colors.onBackground,
            backgroundColor = CustomTheme.colors.surface,
            cursorColor = CustomTheme.colors.onBackground,
            focusedLabelColor = CustomTheme.colors.onBackground,
            focusedIndicatorColor = CustomTheme.colors.onBackground,
            unfocusedLabelColor = CustomTheme.colors.onBackgroundVariant,
            unfocusedIndicatorColor = CustomTheme.colors.onBackgroundVariant,
            placeholderColor = CustomTheme.colors.onBackgroundVariant
        ),
        isError = isError,
    )
}

@Composable
fun MyButton(
    onClick: () -> Unit = {},
    text: String,
    imgId: Int
){
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = mediumPadding, end = mediumPadding, bottom = mediumPadding),
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = CustomTheme.colors.background,
            contentColor = CustomTheme.colors.onBackground
        ),
        elevation = ButtonDefaults.elevation(0.dp, 0.dp),
        border = BorderStroke(1.dp, CustomTheme.colors.onBackground)
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = imgId),
            contentDescription = text,
            modifier = Modifier.size(20.dp),
        )
        Text(
            text = text,
            modifier = Modifier.padding(6.dp),
            style = CustomTheme.typography.h6
        )
    }
}

@Composable
fun LoginButton(
    username: MutableState<String>,
    password: MutableState<String>,
    onClick: () -> Unit
){
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = smallPadding)
            .clip(RoundedCornerShape(50)),
        border = BorderStroke(1.dp, CustomTheme.colors.onBackground),
        shape = RoundedCornerShape(50.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = CustomTheme.colors.background,
            contentColor = CustomTheme.colors.onBackground,
            disabledBackgroundColor = CustomTheme.colors.backgroundVariant,
            disabledContentColor = CustomTheme.colors.onBackgroundVariant
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


