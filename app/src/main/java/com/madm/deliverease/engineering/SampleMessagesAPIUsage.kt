package com.madm.deliverease.engineering


import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.madm.common_libs.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch





@Composable
fun DummyComposeFunctionForMessages(){
    val context = LocalContext.current

    // retrieve msgs
    var receivedMessages: List<Message> by remember { mutableStateOf(listOf()) }


    Button(
        onClick = {
            CoroutineScope(Dispatchers.IO).launch {
                testHandleMsgs(
                    { list: List<Message> -> receivedMessages = list },
                    context
                )
            }
        }
    ) {
        Text("CLICK ME")
    }

    ReceivedMessages(messages = receivedMessages)
}




fun testHandleMsgs(
    receivedMessagesCallback: (List<Message>) -> Unit,
    context: Context
) {
    // send msg
    Message("1", "2", "PRIMO").send(context)

    val messagesManager : MessagesManager =
        MessagesManager("2", context)

    messagesManager.getReceivedMessages{ receivedMessagesCallback(it) }
}




@Composable
fun ReceivedMessages(messages: List<Message>) {
    Column() {
        messages.forEach{
            Text(text = "Sender: ${it.senderID}, body: ${it.body}")
        }
    }
}