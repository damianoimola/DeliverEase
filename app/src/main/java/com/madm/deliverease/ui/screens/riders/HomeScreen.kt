package com.madm.deliverease.ui.screens.riders

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.madm.common_libs.model.*
import com.madm.deliverease.globalUser
import com.madm.deliverease.ui.widgets.*

@Preview
@Composable
fun HomeScreen() {
    var communicationList : MutableList<Message> by rememberSaveable { mutableStateOf(mutableListOf()) }
    var shiftRequestList : List<Message> by rememberSaveable { mutableStateOf(listOf()) }
    var isPlaying = rememberSaveable { mutableStateOf (false) }

    val messagesManager : MessagesManager =
        MessagesManager(globalUser!!.id!!, LocalContext.current)

    messagesManager.getReceivedMessages{ list: List<Message> ->
        communicationList = list.filter { it.messageType == Message.MessageType.NOTIFICATION }.toMutableList()
    }

    messagesManager.getReceivedMessages{ list: List<Message> ->
        shiftRequestList = list.filter { it.messageType == Message.MessageType.REQUEST }
    }




    Column(horizontalAlignment = Alignment.CenterHorizontally) {
//        MyPageHeader()
        CommunicationCard(communicationList, false, Modifier.weight(1f), isPlaying)
        ShiftChangeCard(shiftRequestList, Modifier.weight(1f))
    }
}



