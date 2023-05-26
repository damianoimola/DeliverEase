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
import com.madm.common_libs.model.Message
import com.madm.common_libs.model.MessagesManager
import com.madm.deliverease.globalUser
import com.madm.deliverease.ui.widgets.*

@Preview
@Composable
fun HomeScreen() {
//    val communicationList = listOf(
//        Communication("News 1! I have published calendar 1", "21/05/2023"),
//        Communication("News 2! I have published calendar 2", "20/05/2023"),
//        Communication("News 3! I have published calendar 3", "19/05/2023"),
//        Communication("News 4! I have published calendar 4", "18/05/2023"),
//        Communication("News 5! I have published calendar 5", "17/05/2023"),
//        Communication("News 6! I have published calendar 6", "16/05/2023"),
//        Communication("News 7! I have published calendar 7", "15/05/2023"),
//        Communication("News 8! I have published calendar 8", "14/05/2023"),
//        Communication("News 9! I have published calendar 9", "13/05/2023"),
//    )

//    val shiftRequestList = remember {
//        mutableStateListOf(
//            ShiftRequest("Mario Rossi", "23/12/2023", "22/12/2023"),
//            ShiftRequest("Merlin Mango", "23/12/2023", "22/12/2023"),
//            ShiftRequest("Giulia Schiff", "23/12/2023", "22/12/2023"),
//            ShiftRequest("Nome Cognomen", "23/12/2023", "22/12/2023"),
//            ShiftRequest("Azzo Corri", "23/12/2023", "22/12/2023"),
//            ShiftRequest("Paracetamol Palazzo", "23/12/2023", "22/12/2023"),
//            ShiftRequest("Francesco Virgolino", "23/12/2023", "22/12/2023"),
//        )
//    }



    var communicationList : List<Message> by rememberSaveable { mutableStateOf(listOf()) }
    var shiftRequestList : List<Message> by rememberSaveable { mutableStateOf(listOf()) }

    val messagesManager : MessagesManager = MessagesManager(globalUser!!.id!!, LocalContext.current)

    messagesManager.getReceivedMessages{ list: List<Message> ->
        communicationList = list.filter { it.messageType == Message.MessageType.NOTIFICATION }
    }

    messagesManager.getReceivedMessages{ list: List<Message> ->
        shiftRequestList = list.filter { it.messageType == Message.MessageType.REQUEST }
    }




    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        MyPageHeader()
        CommunicationCard(communicationList, false, Modifier.weight(1f))
        ShiftChangeCard(shiftRequestList, Modifier.weight(1f))
    }
}



