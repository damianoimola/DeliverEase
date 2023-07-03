package com.madm.deliverease.ui.widgets

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
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
    isPortrait: Int,
    isLoading: Boolean = false
) {
    val context = LocalContext.current
    val showTextField = remember { mutableStateOf(false) }
    val textFieldValue = remember { mutableStateOf("") }

    val density = LocalDensity.current
    val focusManager = LocalFocusManager.current

    Card(
        elevation = mediumCardElevation,
        shape = CustomTheme.shapes.medium,
        modifier = modifier
            .fillMaxSize()
            .padding(smallPadding * (1 - isPortrait), smallPadding * isPortrait),
        backgroundColor = CustomTheme.colors.surface
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Top card bar with title and iconButton to show text field
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(smallPadding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    stringResource(R.string.communication_title),
                    style = CustomTheme.typography.h3,
                    textAlign = TextAlign.Center,
                    color = CustomTheme.colors.onSurface
                )
                if(showAddButton) {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        IconButton(
                            onClick = { showTextField.value = !showTextField.value },
                            enabled = !isLoading,
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
                        placeholder = { Text(stringResource(R.string.new_communication)) }
                    )
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        DefaultButton(text = stringResource(R.string.send), Modifier ) {
                            showTextField.value = !showTextField.value

                            val msg = Message(                  // declaration of the message
                                senderID = globalUser!!.id,
                                receiverID = "0",
                                body = textFieldValue.value,
                                type = Message.MessageType.NOTIFICATION.displayName
                            )

                            msg.send(context) { messageSent ->          // sending message to server

                                if (messageSent) {
                                    println("COMM NUMBER BEFORE ${communicationList.count()}")
                                    communicationList.add(              // updating ui with new communication
                                        0,
                                        msg
                                    )
                                    println("COMM NUMBER AFTER ${communicationList.count()}")

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
                        }
                        Spacer(Modifier.width(4.dp))
                        DefaultButton(text = stringResource(R.string.cancel), Modifier) {
                            textFieldValue.value = ""
                            showTextField.value = !showTextField.value
                        }
                    }
                }
            }

            // Text if have not any notification
            if(communicationList.isEmpty() && !isLoading) Text(
                stringResource(R.string.no_communications),
                style = CustomTheme.typography.body1,
                color = CustomTheme.colors.onSurface
            )

            // CommunicationList
            Column(modifier = Modifier.verticalScroll(rememberScrollState())){
                (if(isLoading) listOf(1,2,3,4,5,6,7,8) else communicationList).forEach {
                    Card(
                        Modifier.padding(smallPadding),
                        backgroundColor = CustomTheme.colors.surface,
                        contentColor = CustomTheme.colors.onSurface,
                        elevation = extraSmallCardElevation
                    ) {
                        if(isLoading)
                            ShimmerCustomCommunication()
                        else {
                            CustomCommunication((it as Message).body!!, (it as Message).date)
                        }
                    }
                }
//                content = {
//                    items(if(isLoading) listOf(1,2,3,4,5,6,7,8) else communicationList) { item ->
//                        Card(
//                            Modifier.padding(smallPadding),
//                            backgroundColor = CustomTheme.colors.surface,
//                            contentColor = CustomTheme.colors.onSurface,
//                            elevation = extraSmallCardElevation
//                        ) {
//                            if(isLoading)
//                                ShimmerCustomCommunication()
//                            else {
//                                CustomCommunication((item as Message).body!!, (item as Message).date)
//                            }
//                        }
//                    }
//                }
        }
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
        Text(noticeText, style = CustomTheme.typography.body1)
        Spacer(modifier = Modifier.height(2.dp))
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.publishedOn) + publishDate, style = CustomTheme.typography.body2)
        }
    }
}

@Composable
fun ShimmerCustomCommunication() {
    Column(
        modifier = Modifier
            .clip(CustomTheme.shapes.small)
            .fillMaxWidth()
            .padding(smallPadding)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp)
                .clip(RoundedCornerShape(2.dp))
                .shimmerEffect()
        )

        Spacer(modifier = Modifier.height(4.dp))
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(20.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .shimmerEffect()
            )
        }
    }
}