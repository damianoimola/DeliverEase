package com.madm.deliverease.ui.widgets

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.madm.common_libs.model.Message
import com.madm.common_libs.model.User
import com.madm.deliverease.R
import com.madm.deliverease.globalAllUsers
import com.madm.deliverease.globalUser
import com.madm.deliverease.ui.theme.CustomTheme
import com.madm.deliverease.ui.theme.gilroy
import com.madm.deliverease.ui.theme.smallPadding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun HireNewRiderDialog(callbackFunction: (User) -> Unit, onDismiss: () -> Unit) {
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
                                callbackFunction(newUser)

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


@Composable
fun EditRiderDialog(user: User, callbackFunction: (User, User) -> Unit, onDismiss: () -> Unit) {
    val context = LocalContext.current
    var riderName by rememberSaveable { mutableStateOf(user.name!!) }
    var riderSurname by rememberSaveable { mutableStateOf(user.surname!!) }
    var riderEmail by rememberSaveable { mutableStateOf(user.email!!) }
    var riderPassword by rememberSaveable { mutableStateOf(user.password!!) }

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
                        text = "Edit Rider",
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
                    Button(colors = ButtonDefaults.buttonColors(
                        backgroundColor = CustomTheme.colors.primary,
                        contentColor = CustomTheme.colors.onPrimary,
                    ),
                        onClick = {
                            isNameError = riderName.isEmpty()
                            isSurnameError = riderSurname.isEmpty()
                            isEmailError = riderEmail.isEmpty()
                            isPasswordError = riderPassword.isEmpty()

                            if(!isNameError && !isSurnameError && !isEmailError && !isPasswordError){
                                val newUser = User(
                                    id = user.id,
                                    name = riderName,
                                    surname = riderSurname,
                                    email = riderEmail,
                                    password = riderPassword,
                                    permanentConstraints = user.permanentConstraints,
                                    nonPermanentConstraints = user.nonPermanentConstraints
                                )

                                newUser.registerOrUpdate(context)       // updating server info
                                onDismiss()
                                callbackFunction(user, newUser)

                                Toast.makeText(context, "MODIFIED, HURRAY!!", Toast.LENGTH_SHORT).show()
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




@Composable
fun ChangeShiftDialog(dayOfTheWeek: WeekDay?, previousWeekDay: WeekDay?, month: Int, year: Int,  onDismiss: () -> Unit){

    val context = LocalContext.current

    Dialog(onDismissRequest = { onDismiss()},
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )) {
        Surface(modifier = Modifier
            .wrapContentWidth()
            .wrapContentHeight(),
            shape = MaterialTheme.shapes.large) {
            Column(modifier = Modifier.padding(20.dp).width(400.dp).wrapContentHeight(),
            verticalArrangement = Arrangement.spacedBy(25.dp)) {

                Text(stringResource(id = R.string.confirmation_change)+" "+dayOfTheWeek?.name+" ?",
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
                    Button( colors = ButtonDefaults.buttonColors(
                        backgroundColor = CustomTheme.colors.primary,
                        contentColor = CustomTheme.colors.onPrimary,
                    ),
                        onClick = { onDismiss() }) {
                        Text(text = stringResource(id = R.string.cancel),
                            style = CustomTheme.typography.button)
                    }
                    Button( colors = ButtonDefaults.buttonColors(
                        backgroundColor = CustomTheme.colors.primary,
                        contentColor = CustomTheme.colors.onPrimary,
                    ),
                        onClick = {

                        // declaration of the message
                        val msg = Message(
                            senderID = globalUser!!.id,
                            receiverID = "0",
                            body = "${previousWeekDay?.number?.integerToTwoDigit()}-${((month+1)%12).integerToTwoDigit()}-$year#${dayOfTheWeek?.number?.integerToTwoDigit()}-${((month+1)%12).integerToTwoDigit()}-${year}",
                            type = Message.MessageType.REQUEST.displayName
                        )

                        // sending message to server
                        msg.send(context) { messageSent ->

                            if (messageSent) {
                                CoroutineScope(Dispatchers.Main).launch {
                                    Toast.makeText(
                                        context,
                                        "Request sent!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } else {
                                CoroutineScope(Dispatchers.Main).launch {
                                    Toast.makeText(
                                        context,
                                        "Request cannot be sent, please try later",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                        //closing dialog
                        onDismiss()

                    }) {
                        Text(text = stringResource(id = R.string._continue),
                            style = CustomTheme.typography.button)
                    }
                }
            }
        }
    }
}




@Composable
fun WrongConstraintsDialog(errorMessage: String, onContinue: () -> Unit, onDismiss: () -> Unit){
    val context = LocalContext.current
    Dialog(onDismissRequest = { onDismiss()},
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )) {
        Surface(modifier = Modifier
            .wrapContentWidth()
            .wrapContentHeight(),
            shape = MaterialTheme.shapes.large) {
            Column(modifier = Modifier.padding(20.dp).width(400.dp).wrapContentHeight(),
                verticalArrangement = Arrangement.spacedBy(25.dp)) {

                Text(errorMessage,
                    style = TextStyle(
                        fontFamily = gilroy,
                        fontWeight = FontWeight.Normal,
                        fontSize = 17.sp,
                        textAlign = TextAlign.Start
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
                            )
                        )
                    }
                    Button(onClick = {
                        onContinue()
                        //closing dialog
                        onDismiss()
                    }) {
                        Text(text = stringResource(id = R.string._continue),
                            style = TextStyle(
                                fontFamily = gilroy,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 15.sp,
                                textAlign = TextAlign.Center
                            )
                        )
                    }
                }
            }
        }
    }
}





















