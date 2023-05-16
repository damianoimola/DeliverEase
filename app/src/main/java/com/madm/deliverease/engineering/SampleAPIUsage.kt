package com.madm.deliverease.engineering

import com.madm.common_libs.server.Message
import com.madm.common_libs.server.MessagesHandler

import android.content.Context
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch





@Composable
fun DummyComposeFunction(){
    val context = LocalContext.current

    // retrieve msgs
    var receivedMessages: List<Message> = remember { mutableStateListOf(Message()) }
    var sentMessages: List<Message> = remember { mutableStateListOf(Message()) }
    var chatMessages: List<Message> = remember { mutableStateListOf(Message()) }


    Button(
        onClick = {
            CoroutineScope(Dispatchers.IO).launch {
                testHandleMsgs(
                    receivedMessages,
                    { list: List<Message> ->
                        receivedMessages = list
                        println("####### 1° MESSAGGIO RICEVUTO ${receivedMessages[0].message}")
                    },
                    sentMessages,
                    { list: List<Message> ->
                        sentMessages = list
                        println("####### 1° MESSAGGIO INVIATO ${sentMessages[0].message}")
                    },
                    chatMessages,
                    { list: List<Message> ->
                        chatMessages = list
                        println("####### 1° MESSAGGIO CONVERSAZIONE ${chatMessages[0].message}")
                    },
                    context
                )
            }
        }
    ) {
        Text("CLICK ME")
    }
}




fun testHandleMsgs(
    receivedMessages: List<Message>,
    receivedMessagesCallback: (List<Message>) -> Unit,
    sentMessages: List<Message>,
    sentMessagesCallback: (List<Message>) -> Unit,
    chatMessages: List<Message>,
    chatMessagesCallback: (List<Message>) -> Unit,
    context: Context
) {
    // send msg
    Message("1", "2", "PRIMO").send(context)
    Message("1", "3", "SECONDO").send(context)
    Message("2", "3", "TERZO").send(context)
    Message("3", "2", "QUARTO").send(context)


    val msgList : MessagesHandler = MessagesHandler("2", context)

    msgList.getReceivedMessages(receivedMessagesCallback)
    msgList.getSentMessages("3", sentMessagesCallback)
    msgList.getConversationMessages("3", chatMessagesCallback)
}