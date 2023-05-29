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
//    val riderList = remember { mutableStateListOf(
//        Rider("Name1", "Surname1"),
//        Rider("Name2", "Surname2"),
//        Rider("Name3", "Surname3"),
//        Rider("Name4", "Surname4"),
//        Rider("Name5", "Surname5"),
//        Rider("Name6", "Surname6"),
//        Rider("Name7", "Surname7"),
//        Rider("Name8", "Surname8"),
//        Rider("Name9", "Surname9"),
//        Rider("Name10", "Surname10"),
//        Rider("Name11", "Surname11"),
//        Rider("Name12", "Surname12"),
//        Rider("Name13", "Surname13"),
//        Rider("Name14", "Surname14"),
//        Rider("Name15", "Surname15"),
//        Rider("Name16", "Surname16"),
//    ) }


    // getting API data
    var riderList : List<User> by rememberSaveable { mutableStateOf(listOf()) }
    var workDay : Day by rememberSaveable { mutableStateOf(Day()) }
    var communicationList : List<Message> by rememberSaveable { mutableStateOf(listOf()) }

    val messagesManager : MessagesManager =
        MessagesManager(globalUser!!.id!!, LocalContext.current)

    val calendarManager : CalendarManager =
        CalendarManager(LocalContext.current)

    messagesManager.getAllMessages{ list: List<Message> ->
        communicationList = list.filter { it.messageType == Message.MessageType.NOTIFICATION }
    }

    calendarManager.getDays{ list: List<Day> ->
        workDay = list.first {
            println("########## DATE ${it.date}, ${Date()}")
            it.date == Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant())
        }

        riderList = globalAllUsers.filter { user ->
            user.id in workDay.riders!!.toList()
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