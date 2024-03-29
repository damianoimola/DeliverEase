package com.madm.deliverease.ui.widgets

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.madm.deliverease.R
import com.madm.deliverease.ui.theme.CustomTheme
import com.madm.deliverease.ui.theme.mediumPadding
import com.madm.deliverease.ui.theme.smallPadding

@Composable
fun MyOutlinedTextField(
    field: MutableState<String>,
    isError: Boolean,
    label: String = "",
    onDone: () -> Unit = {},
    visualTransformation : VisualTransformation? = null,
    keyboardOptions : KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Ascii, imeAction = ImeAction.Done)
){
    var showPassword by remember { mutableStateOf(visualTransformation == null) }

    OutlinedTextField(
        value = field.value,
        onValueChange = { field.value = it },
        modifier = Modifier
            .padding(bottom = smallPadding)
            .fillMaxWidth(),
        label = { Text(text = label) },
        enabled = true,
        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = keyboardOptions,
        keyboardActions = KeyboardActions(onDone = {onDone()}),
        singleLine = true,
        maxLines = 1,
        textStyle = CustomTheme.typography.body1,
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
        trailingIcon = {
            if(visualTransformation != null) {
                if (showPassword) {
                    IconButton(onClick = { showPassword = false }) {
                        Icon(
                            imageVector = Icons.Filled.Visibility,
                            contentDescription = "hide_password",
                            tint = CustomTheme.colors.onBackground
                        )
                    }
                } else {
                    IconButton(
                        onClick = { showPassword = true }) {
                        Icon(
                            imageVector = Icons.Filled.VisibilityOff,
                            contentDescription = "hide_password",
                            tint = CustomTheme.colors.onBackground
                        )
                    }
                }
            }
        }
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
            style = CustomTheme.typography.button
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


@Composable
fun DefaultButton(text: String, modifier: Modifier, onClick: () -> Unit){
    Button(
        onClick =  onClick ,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = CustomTheme.colors.primary,
            contentColor = CustomTheme.colors.onPrimary,
        ),
        shape = CustomTheme.shapes.large
    ) {
        Text(
            text = text,
            style = CustomTheme.typography.button
        )
    }
}


