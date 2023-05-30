package com.madm.deliverease.ui.screens.admin

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
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
    var todayWorkDay : Day by rememberSaveable { mutableStateOf(Day()) }
    var communicationList : List<Message> by rememberSaveable { mutableStateOf(listOf()) }

    val messagesManager : MessagesManager =
        MessagesManager(globalUser!!.id!!, LocalContext.current)

    val calendarManager : CalendarManager =
        CalendarManager(LocalContext.current)

    messagesManager.getAllMessages{ list: List<Message> ->
        communicationList = list.filter { it.messageType == Message.MessageType.NOTIFICATION }
    }

    calendarManager.getDays{ list: List<Day> ->
        todayWorkDay = list.first {
            it.date == Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant())
        }

        riderList = globalAllUsers.filter { user ->
            user.id in todayWorkDay.riders!!.toList()
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MyPageHeader()
        TodayRidersCard(riderList, modifier = Modifier.weight(1f))
        CommunicationCard(communicationList, true, Modifier.weight(1f)) { text: String -> println(text) }
    }
}