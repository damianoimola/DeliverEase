package com.madm.deliverease.ui.widgets

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.madm.deliverease.R
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import com.madm.deliverease.ui.theme.gilroy

@Composable
fun CustomOutlinedTextField(
    label : String,
    isPassword: Boolean = false,
    leadingIcon: @Composable () -> Unit = {}
){
    var text by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        value = text,
        onValueChange = { text = it },
        label = { Text(label) },
        leadingIcon = leadingIcon,
//        leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Username") },
        modifier = Modifier.padding(0.dp, 0.dp, 5.dp, 0.dp),
        textStyle = TextStyle(
            fontSize = 20.sp,
            fontWeight = FontWeight.Normal,
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
            focusedLabelColor = MaterialTheme.colors.onPrimary,
            focusedBorderColor = MaterialTheme.colors.primary,
            unfocusedLabelColor = MaterialTheme.colors.onPrimary,
            unfocusedBorderColor = MaterialTheme.colors.primaryVariant,
            backgroundColor = Color.Transparent,
            textColor = MaterialTheme.colors.onPrimary,
            leadingIconColor = MaterialTheme.colors.onPrimary
        )
    )
}

