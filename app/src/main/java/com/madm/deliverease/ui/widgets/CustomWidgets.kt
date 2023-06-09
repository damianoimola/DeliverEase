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
import androidx.compose.ui.platform.LocalContext
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.madm.deliverease.R
import com.madm.deliverease.ui.theme.*
import kotlin.system.exitProcess

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
fun ConfirmExitingApp(onDismiss: () -> Unit){

    val context = LocalContext.current

    Dialog(onDismissRequest = { onDismiss()},
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Surface(modifier = Modifier
            .wrapContentWidth()
            .wrapContentHeight(),
            shape = MaterialTheme.shapes.large) {
            Column(modifier = Modifier.padding(20.dp).width(400.dp).wrapContentHeight(),
                verticalArrangement = Arrangement.spacedBy(25.dp)) {

                Text(text = stringResource(R.string.exiting_question),
                    style = TextStyle(
                        fontFamily = gilroy,
                        fontWeight = FontWeight.Normal,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center
                    )
                )

                Row(
                    modifier = Modifier.width(400.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(onClick = { onDismiss() }) {
                        Text(text = stringResource(id = R.string.cancel),
                            style = TextStyle(
                                fontFamily = gilroy,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 15.sp,
                                textAlign = TextAlign.Center
                            ))
                    }
                    Button(onClick = {
                        //closing dialog
                        onDismiss()
                        exitProcess(0)

                    }) {
                        Text(text = stringResource(R.string.exit),
                            style = TextStyle(
                                fontFamily = gilroy,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 15.sp,
                                textAlign = TextAlign.Center
                            ))
                    }
                }
            }
        }
    }
}

