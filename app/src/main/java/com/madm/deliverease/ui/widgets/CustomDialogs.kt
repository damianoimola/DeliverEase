package com.madm.deliverease.ui.widgets

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
import com.madm.deliverease.ui.theme.*
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
            shape = CustomTheme.shapes.large,
            color = CustomTheme.colors.background,
            contentColor = CustomTheme.colors.onBackground
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // DialogBox title
                Row (modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = stringResource(id = R.string.hire_new_rider),
                        style = CustomTheme.typography.h3
                    )
                }

                TextField(
                    value = riderName,
                    onValueChange = { riderName = it },
                    placeholder = { Text(text = stringResource(id = R.string.name) + "*") },
                    modifier = Modifier.padding(smallPadding),
                    isError = isNameError,
                    textStyle = CustomTheme.typography.body1,
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
                )

                TextField(
                    value = riderSurname,
                    onValueChange = { riderSurname = it },
                    placeholder = { Text(text = stringResource(id = R.string.surname) + "*") },
                    modifier = Modifier.padding(smallPadding),
                    isError = isSurnameError,
                    textStyle = CustomTheme.typography.body1,
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
                )

                TextField(
                    value = riderEmail,
                    onValueChange = { riderEmail = it },
                    placeholder = { Text(text = stringResource(id = R.string.username) + "*") },
                    modifier = Modifier.padding(smallPadding),
                    isError = isEmailError,
                    textStyle = CustomTheme.typography.body1,
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
                )

                TextField(
                    value = riderPassword,
                    onValueChange = { riderPassword = it },
                    placeholder = { Text(text = stringResource(id = R.string.password) + "*") },
                    modifier = Modifier.padding(smallPadding),
                    isError = isPasswordError,
                    textStyle = CustomTheme.typography.body1,
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
                )

                // Buttons
                Row (modifier = Modifier.fillMaxWidth()) {

                    DefaultButton(text = stringResource(id = R.string.close), modifier = Modifier
                        .weight(.5f)
                        .padding(8.dp)) {
                        onDismiss()
                    }

                    DefaultButton(text = stringResource(id = R.string.hire), modifier = Modifier
                        .weight(.5f)
                        .padding(8.dp) ) {
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
                    }

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
            shape = MaterialTheme.shapes.large,
            color = CustomTheme.colors.background,
            contentColor = CustomTheme.colors.onBackground
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // DialogBox title
                Row (modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = stringResource(R.string.edit_rider),
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
                    placeholder = { Text(text = stringResource(R.string.name) + "*") },
                    modifier = Modifier.padding(smallPadding),
                    isError = isNameError,
                    textStyle = CustomTheme.typography.body1,
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
                )

                TextField(
                    value = riderSurname,
                    onValueChange = { riderSurname = it },
                    placeholder = { Text(text = stringResource(R.string.surname) +"*") },
                    modifier = Modifier.padding(smallPadding),
                    isError = isSurnameError,
                    textStyle = CustomTheme.typography.body1,
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
                )

                TextField(
                    value = riderEmail,
                    onValueChange = { riderEmail = it },
                    placeholder = { Text(text = stringResource(R.string.username) +"*") },
                    modifier = Modifier.padding(smallPadding),
                    isError = isEmailError,
                    textStyle = CustomTheme.typography.body1,
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
                )

                TextField(
                    value = riderPassword,
                    onValueChange = { riderPassword = it },
                    placeholder = { Text(text = stringResource(id = R.string.password)+"*") },
                    modifier = Modifier.padding(smallPadding),
                    isError = isPasswordError,
                    textStyle = CustomTheme.typography.body1,
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
                )

                // Buttons
                Row (modifier = Modifier.fillMaxWidth()) {

                    DefaultButton(text = stringResource(R.string.close), modifier = Modifier
                        .weight(.5f)
                        .padding(8.dp) ) {
                        onDismiss()
                    }
                    DefaultButton(text = stringResource(R.string.save), modifier = Modifier
                        .weight(.5f)
                        .padding(8.dp)) {
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
                    }

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
            shape = CustomTheme.shapes.large,
            color = CustomTheme.colors.background,
            contentColor = CustomTheme.colors.onBackground) {
            Column(modifier = Modifier
                .padding(20.dp)
                .width(400.dp)
                .wrapContentHeight(),
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
                    DefaultButton(text = stringResource(id = R.string.cancel), modifier = Modifier) {
                        onDismiss()
                    }

                    DefaultButton(text = stringResource(id = R.string._continue), modifier =Modifier ) {
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
                    }
                }
            }
        }
    }
}

@Composable
fun ConstraintsDialog(
    title: String,
    perWeekConstraint: List<String> = listOf(),
    perDayConstraint: List<String> = listOf(),
    emptyDaysConstraint: List<String> = listOf(),
    onContinue: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Card(
            elevation = mediumCardElevation,
            shape = CustomTheme.shapes.medium,
            backgroundColor = CustomTheme.colors.surface,
            contentColor = CustomTheme.colors.onSurface,
            modifier = Modifier
                .wrapContentWidth()
        ) {
            println("############### EDC $emptyDaysConstraint")
            Column(
                modifier = Modifier.padding(smallPadding, nonePadding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = title,
                        style = CustomTheme.typography.h2,
                        color = CustomTheme.colors.onSurface
                    )
                }

                if(perWeekConstraint.isNotEmpty() || perDayConstraint.isNotEmpty())
                    LazyColumn(
                        Modifier
                            .height(200.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if(perWeekConstraint.isNotEmpty()) {
                            item { Text("Rider constraints not respected", style = CustomTheme.typography.h5.copy(fontWeight = FontWeight.SemiBold)) }
                            items(perWeekConstraint) {
                                Text(it, style = CustomTheme.typography.body1)//, color = CustomTheme.colors.onSurface)
                            }
                        }


                        if(perDayConstraint.isNotEmpty()) {
                            item { Text("Riders constraints not respected", style = CustomTheme.typography.h5.copy(fontWeight = FontWeight.SemiBold)) }
                            items(perDayConstraint) {
                                Text(it, style = CustomTheme.typography.body1)//, color = CustomTheme.colors.onSurface)
                            }
                        }
                    }
                else if(emptyDaysConstraint.isNotEmpty())
                    LazyColumn(
                        Modifier
                            .height(200.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        println("############### ECCOLO QUA")
                        item { Text("Days without riders", style = CustomTheme.typography.h5.copy(fontWeight = FontWeight.SemiBold)) }
                        items(emptyDaysConstraint) {
                            Text(it, style = CustomTheme.typography.body1)//, color = CustomTheme.colors.onSurface)
                        }
                        item { Text("Are you sure to continue?", style = CustomTheme.typography.h5.copy(fontWeight = FontWeight.SemiBold)) }
                    }

                Row(
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    DefaultButton(text = "Cancel", modifier = Modifier) { onDismiss() }
                    DefaultButton(text = "Accept", modifier = Modifier) { onContinue() }
                }
            }
        }
    }
}

