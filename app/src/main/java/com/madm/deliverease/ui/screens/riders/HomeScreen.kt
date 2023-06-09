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
    var workDayList : List<WorkDay> by rememberSaveable { mutableStateOf(mutableListOf()) }
    var communicationList : MutableList<Message> by rememberSaveable { mutableStateOf(mutableListOf()) }
    var shiftRequestList : List<Message> by rememberSaveable { mutableStateOf(listOf()) }
    val isPlaying = rememberSaveable { mutableStateOf (false) }

    val messagesManager : MessagesManager =
        MessagesManager(globalUser!!.id!!, LocalContext.current)

    val calendarManager : CalendarManager=
        CalendarManager(LocalContext.current)

    calendarManager.getDays { days : List<WorkDay> ->
        workDayList = days
    }

    messagesManager.getReceivedMessages{ list: List<Message> ->
        communicationList = list.filter { it.messageType == Message.MessageType.NOTIFICATION }.toMutableList()
    }

    messagesManager.getReceivedMessages{ list: List<Message> ->
        shiftRequestList = list.filter {
            it.messageType == Message.MessageType.REQUEST
            &&
            it.senderID != globalUser!!.id
            &&
            // index 0 = offered day, index 1 = wanted day (by the sender)
            (
                workDayList.any { d -> globalUser!!.id in d.riders!! }
                &&
                (
                    it.body!!.split("#")[0] !in workDayList.filter { d -> globalUser!!.id in d.riders!! }.map { d -> d.date }.toList()
                    &&
                    it.body!!.split("#")[1] in workDayList.filter { d -> globalUser!!.id in d.riders!! }.map { d -> d.date }.toList()
                )
            )
        }
    }




    Column(horizontalAlignment = Alignment.CenterHorizontally) {
//        MyPageHeader()
        CommunicationCard(communicationList, false, Modifier.weight(1f), isPlaying)
        ShiftChangeCard(shiftRequestList, Modifier.weight(1f))
    }
}



