package com.madm.deliverease.ui.screens.admin

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.madm.common_libs.model.*
import com.madm.deliverease.globalAllUsers
import com.madm.deliverease.globalUser
import com.madm.deliverease.ui.widgets.*
import java.time.LocalDate
import java.time.ZoneId
import java.util.*
import kotlinx.coroutines.*

@SuppressLint("MutableCollectionMutableState")
@Preview
@Composable
fun HomeScreen() {
    val configuration = LocalConfiguration.current

    // getting API data
    var riderList : List<User> by rememberSaveable { mutableStateOf(listOf()) }
    var todayWorkDay : WorkDay by rememberSaveable { mutableStateOf(WorkDay()) }
    var communicationList : MutableList<Message> by rememberSaveable { mutableStateOf(mutableListOf()) }
    val isPlaying = rememberSaveable { mutableStateOf (false) }
    var loadingData by rememberSaveable { mutableStateOf(true) }

    val messagesManager = MessagesManager(globalUser!!.id!!, LocalContext.current)

    val calendarManager = CalendarManager(LocalContext.current)

    LaunchedEffect(key1 = rememberCoroutineScope()) {
        coroutineScope {
            launch {
                messagesManager.getAllMessages { list: List<Message>? ->
                    if (list != null)
                        communicationList = list
                            .filter { it.messageType == Message.MessageType.NOTIFICATION }
                            .sortedByDescending { it.date }.toMutableList()
                }
            }

            launch {
                calendarManager.getDays { list: List<WorkDay> ->
                    todayWorkDay = list.first {
                        it.workDayDate == Date.from(
                            LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()
                        )
                    }
                    riderList = globalAllUsers.filter { user -> user.id in todayWorkDay.riders!!.toList() }
                }
            }
        }

        delay(500)
        loadingData = false
    }

    if(isPlaying.value)
        PizzaLoaderDialog(isPlaying = isPlaying)

    if(configuration.orientation == Configuration.ORIENTATION_PORTRAIT){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            TodayRidersCard(modifier = Modifier.weight(1f), riderList, 2, 1, loadingData)
            CommunicationCard(communicationList, true, Modifier.weight(1f), 1, loadingData)
        }
    } else {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            TodayRidersCard(modifier = Modifier.weight(1f), riderList, 2, 0, loadingData)
            CommunicationCard(communicationList, true, Modifier.weight(1f), 0, loadingData)
        }
    }
}