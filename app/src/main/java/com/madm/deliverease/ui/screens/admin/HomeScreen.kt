package com.madm.deliverease.ui.screens.admin

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.madm.common_libs.model.*
import com.madm.deliverease.globalAllUsers
import com.madm.deliverease.globalUser
import com.madm.deliverease.ui.widgets.*
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

@Preview
@Composable
fun HomeScreen() {
    // getting API data
    var riderList : List<User> by rememberSaveable { mutableStateOf(listOf()) }
    var todayWorkDay : WorkDay by rememberSaveable { mutableStateOf(WorkDay()) }
    var communicationList : MutableList<Message> by rememberSaveable { mutableStateOf(mutableListOf()) }
    var isPlaying = rememberSaveable { mutableStateOf (false) }

    val messagesManager = MessagesManager(globalUser!!.id!!, LocalContext.current)

    val calendarManager = CalendarManager(LocalContext.current)

    messagesManager.getAllMessages{ list: List<Message>? ->
        if(list != null )
            communicationList = list
                .filter { it.messageType == Message.MessageType.NOTIFICATION }
                .sortedByDescending { it.date }.toMutableList()
    }

    calendarManager.getDays{ list: List<WorkDay> ->
        todayWorkDay = list.first {
            it.date == Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant())
        }

        riderList = globalAllUsers.filter { user ->
            user.id in todayWorkDay.riders!!.toList()
        }
    }

    if(isPlaying.value)
        PizzaLoaderDialog(isPlaying = isPlaying)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        MyPageHeader()
        TodayRidersCard(riderList, 2, modifier = Modifier.weight(1f))
        CommunicationCard(communicationList, true, Modifier.weight(1f), isPlaying)
    }
}