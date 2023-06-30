package com.madm.deliverease.ui.screens.riders

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.madm.common_libs.model.*
import com.madm.deliverease.globalUser
import com.madm.deliverease.ui.widgets.*
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate

@SuppressLint("MutableCollectionMutableState")
@Preview
@Composable
fun HomeScreen() {
    val configuration = LocalConfiguration.current
    var workDayList: List<WorkDay> by rememberSaveable { mutableStateOf(mutableListOf()) }
    var communicationList: MutableList<Message> by rememberSaveable { mutableStateOf(mutableListOf()) }
    var shiftRequestList: ArrayList<Message> by rememberSaveable { mutableStateOf(arrayListOf()) }
    var loadingData by rememberSaveable { mutableStateOf(true) }

    val messagesManager = MessagesManager(globalUser!!.id!!, LocalContext.current)

    val calendarManager = CalendarManager(LocalContext.current)

    LaunchedEffect(key1 = rememberCoroutineScope()) {
        coroutineScope {
            launch {
                calendarManager.getDays { days: List<WorkDay> ->
                    workDayList = days
                }
            }
            launch {
                messagesManager.getReceivedMessages { list: List<Message> ->
                    communicationList = list
                        .filter { it.messageType == Message.MessageType.NOTIFICATION }
                        .sortedByDescending { it.messageDate }
                        .toMutableList()
                }
            }
            launch {

                val currentMonth = LocalDate.now().monthValue
                val currentYear = LocalDate.now().year
                val currentDay = LocalDate.now().dayOfMonth

                messagesManager.getReceivedMessages { list: List<Message> ->
                    shiftRequestList = ArrayList(list.filter {
                        it.messageType == Message.MessageType.REQUEST
                                &&
                                it.senderID != globalUser!!.id
                                &&
                                // index 0 = offered day, index 1 = wanted day (by the sender)
                                (
                                        workDayList.any { d -> globalUser!!.id in d.riders!! }
                                                &&
                                                (
                                                        it.body!!.split("#")[0] !in workDayList.filter { d -> globalUser!!.id in d.riders!! }
                                                            .map { d -> d.date }.toList()
                                                                &&
                                                                it.body!!.split("#")[1] in workDayList.filter { d -> globalUser!!.id in d.riders!! }
                                                            .map { d -> d.date }.toList()
                                                                &&
                                                                //controlla che la data di richiesta sia successiva alla data attuale
                                                                (((it.body!!.split("#")[0].split("-")[0].toInt() > currentDay
                                                                        &&
                                                                        it.body!!.split("#")[0].split("-")[1].toInt() == currentMonth
                                                                        )
                                                                        ||
                                                                        it.body!!.split("#")[0].split("-")[1].toInt() > currentMonth
                                                                        )
                                                                        &&
                                                                        it.body!!.split("#")[0].split("-")[2].toInt() >= currentYear
                                                                        )
                                                                &&
                                                                //controlla che la data richiesta sia successiva alla data attuale
                                                                (((it.body!!.split("#")[1].split("-")[0].toInt() > currentDay
                                                                        &&
                                                                        it.body!!.split("#")[1].split("-")[1].toInt() == currentMonth
                                                                        )
                                                                        ||
                                                                        it.body!!.split("#")[1].split("-")[1].toInt() > currentMonth
                                                                        )
                                                                        &&
                                                                        it.body!!.split("#")[1].split("-")[2].toInt() >= currentYear
                                                                        )



                                                        )
                                        )
                    })
                }
            }
        }

        delay(500)

        loadingData = false
    }

    // to remove the request message from the list of messages
    val updateList: (Message) -> Unit = { shiftRequestList.remove(it) }

    if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CommunicationCard(communicationList, false, Modifier.weight(1f), 1, loadingData)
            ShiftChangeCard(shiftRequestList, Modifier.weight(1f), updateList, 1, loadingData)
        }
    } else {
        Row(verticalAlignment = Alignment.CenterVertically) {
            CommunicationCard(communicationList, false, Modifier.weight(1f), 0, loadingData)
            ShiftChangeCard(shiftRequestList, Modifier.weight(1f), updateList, 0, loadingData)
        }
    }

}



