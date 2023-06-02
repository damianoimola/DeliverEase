package com.madm.deliverease.ui.widgets

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.madm.common_libs.model.User
import com.madm.deliverease.globalAllUsers
import com.madm.deliverease.ui.theme.gilroy
import com.madm.deliverease.ui.theme.smallPadding

@Composable
fun HireNewRiderDialog(onDismiss: () -> Unit) {
    val context = LocalContext.current
    var riderName by rememberSaveable { mutableStateOf("") }
    var riderSurname by rememberSaveable { mutableStateOf("") }
    var riderEmail by rememberSaveable { mutableStateOf("") }
    var riderPassword by rememberSaveable { mutableStateOf("") }

    var isNameError by rememberSaveable { mutableStateOf(false) }
    var isSurnameError by rememberSaveable { mutableStateOf(false) }
    var isEmailError by rememberSaveable { mutableStateOf(false) }
    var isPasswordError by rememberSaveable { mutableStateOf(false) }


    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = false
        )
    ) {
        Surface(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight(),
            shape = MaterialTheme.shapes.large
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // DialogBox title
                Row (modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Hire new Rider",
                        style = TextStyle(
                            fontFamily = gilroy,
                            fontWeight = FontWeight.Bold,
                            fontSize = 30.sp,
                            textAlign = TextAlign.Center
                        )
                    )
                }

                TextField(
                    value = riderName,
                    onValueChange = { riderName = it },
                    placeholder = { Text(text = "Name*") },
                    modifier = Modifier.padding(smallPadding),
                    isError = isNameError
                )

                TextField(
                    value = riderSurname,
                    onValueChange = { riderSurname = it },
                    placeholder = { Text(text = "Surname*") },
                    modifier = Modifier.padding(smallPadding),
                    isError = isSurnameError
                )

                TextField(
                    value = riderEmail,
                    onValueChange = { riderEmail = it },
                    placeholder = { Text(text = "E-mail*") },
                    modifier = Modifier.padding(smallPadding),
                    isError = isEmailError
                )

                TextField(
                    value = riderPassword,
                    onValueChange = { riderPassword = it },
                    placeholder = { Text(text = "Password*") },
                    modifier = Modifier.padding(smallPadding),
                    isError = isPasswordError
                )

                // Buttons
                Row (modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = { onDismiss() },
                        content = { Text(text = "Close") },
                        modifier = Modifier
                            .weight(.5f)
                            .padding(8.dp)
                    )
                    Button(
                        onClick = {
                            isNameError = riderName.isEmpty()
                            isSurnameError = riderSurname.isEmpty()
                            isEmailError = riderEmail.isEmpty()
                            isPasswordError = riderPassword.isEmpty()

                            if(!isNameError && !isSurnameError && !isEmailError && !isPasswordError){
                                val newUser = User(
                                    id = (globalAllUsers.maxOf { (it.id)!!.toInt() } + 1).toString(),
                                    name = riderName,
                                    surname = riderSurname,
                                    email = riderEmail,
                                    password = riderPassword,
                                    permanentConstraints = ArrayList(),
                                    nonPermanentConstraints = ArrayList()
                                )

                                newUser.registerOrUpdate(context)       // updating server info
                                onDismiss()
                                globalAllUsers.add(newUser)             // updating ui with the new rider

                                Toast.makeText(context, "HIRED, HURRAY!!", Toast.LENGTH_SHORT).show()
                            }
                            else
                                Toast.makeText(context, "SOME ERRORS OCCURRED", Toast.LENGTH_SHORT).show()
                        },
                        content = { Text(text = "Hire") },
                        modifier = Modifier
                            .weight(.5f)
                            .padding(8.dp)
                    )
                }
            }
        }
    }
}