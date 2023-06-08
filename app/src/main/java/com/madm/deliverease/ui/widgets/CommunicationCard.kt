package com.madm.deliverease.ui.widgets

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.madm.common_libs.model.*
import com.madm.deliverease.R
import com.madm.deliverease.globalUser
import com.madm.deliverease.ui.theme.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun CommunicationCard(
    communicationList: MutableList<Message>,
    showAddButton: Boolean,
    modifier: Modifier = Modifier,
    isPlaying: MutableState<Boolean>
) {
    val context = LocalContext.current
    val showTextField = remember { mutableStateOf(false) }
    val textFieldValue = remember { mutableStateOf("") }

    val density = LocalDensity.current
    val focusManager = LocalFocusManager.current

    Card(
        elevation = mediumCardElevation,
        shape = MaterialTheme.shapes.medium,
        modifier = modifier
            .fillMaxSize()
            .padding(nonePadding, smallPadding),
        backgroundColor = CustomTheme.colors.surface
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Top card bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(smallPadding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    stringResource(R.string.communication_title),
                    style = CustomTheme.typography.h4,
                    textAlign = TextAlign.Center
                )
                if(showAddButton) {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        IconButton(
                            onClick = { showTextField.value = !showTextField.value },
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(CustomTheme.colors.primary)
                                .size(40.dp)
                        ) {
                            if(!showTextField.value) Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = stringResource(R.string.add),
                                tint = CustomTheme.colors.onPrimary
                            )
                            else Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.remove),
                                contentDescription = stringResource(R.string.remove),
                                tint = CustomTheme.colors.onPrimary
                            )
                        }
                    }
                }
            }

            // Text Field animated
            AnimatedVisibility(
                visible = showTextField.value,
                enter = slideInVertically {
                    // Slide in from 40 dp from the top.
                    with(density) { -40.dp.roundToPx() }
                } + expandVertically(
                    // Expand from the top.
                    expandFrom = Alignment.Top
                ) + fadeIn(
                    // Fade in with the initial alpha of 0.3f.
                    initialAlpha = 0.3f
                ),
                exit = slideOutVertically() + shrinkVertically() + fadeOut()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(smallPadding, nonePadding)
                ) {
                    TextField(
                        value = textFieldValue.value,
                        onValueChange = { textFieldValue.value = it },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Ascii,
                            imeAction = ImeAction.Done // Done, not Enter
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {focusManager.clearFocus()},
                        ),
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
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = {
                                showTextField.value = !showTextField.value
                                isPlaying.value = true

                                val msg = Message(                  // declaration of the message
                                    senderID = globalUser!!.id,
                                    receiverID = "0",
                                    body = textFieldValue.value,
                                    type = Message.MessageType.NOTIFICATION.displayName
                                )

                                msg.send(context) { messageSent ->          // sending message to server

                                    if (messageSent) {
                                        communicationList.add(              // updating ui with new communication
                                            0,
                                            msg
                                        )

                                        CoroutineScope(Dispatchers.Main).launch {
                                            Toast.makeText(
                                                context,
                                                "Message has been sent correctly!",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    } else {
                                        CoroutineScope(Dispatchers.Main).launch {
                                            Toast.makeText(
                                                context,
                                                "Message has not been sent!",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                }

                                textFieldValue.value = ""
                                isPlaying.value = false
                            }
                        ) {
                            Text(stringResource(R.string.send))
                        }
                        Spacer(Modifier.width(4.dp))
                        Button(
                            onClick = {
                                textFieldValue.value = ""
                                showTextField.value = !showTextField.value
                            }
                        ) {
                            Text(stringResource(R.string.cancel))
                        }
                    }
                }
            }

            // Text if have not any notification
            if(communicationList.isEmpty()) Text(stringResource(R.string.no_communications), style = TextStyle(fontSize = 18.sp))

            // CommunicationList
            LazyColumn(
                content = {
                    items(communicationList) { item ->
                        Card(Modifier.padding(smallPadding)) {
                            CustomCommunication(item.body!!, item.date)
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun CustomCommunication(
    noticeText: String,
    publishDate: String
) {
    Column(
        modifier = Modifier
            .clip(Shapes.small)
            .fillMaxWidth()
            .padding(smallPadding)
    ) {
        Text(noticeText)
        Spacer(modifier = Modifier.height(2.dp))
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.publishedOn) + publishDate) /* TODO: set to typography */
        }
    }
}