package com.madm.deliverease.ui

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.*
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.madm.common_libs.model.Message
import com.madm.common_libs.model.MessagesManager
import com.madm.deliverease.globalUser
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

object DeviceCommunicationWidget: GlanceAppWidget() {

    val communicationKey = stringPreferencesKey("communication")

    @Composable
    override fun Content() {
        val communication = currentState(key = communicationKey) ?: ""
        Column(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(Color.DarkGray),
            verticalAlignment = Alignment.Vertical.CenterVertically,
            horizontalAlignment = Alignment.Horizontal.CenterHorizontally
        ) {
            Text(
                text = communication,
                style = TextStyle(
                    fontWeight = FontWeight.Medium,
                    color = ColorProvider(Color.White),
                    fontSize = 26.sp
                )
            )
            Button(
                text = "Reload",
                onClick = actionRunCallback(ReloadActionCallback::class.java)
            )
        }
    }
}

class CommunicationWidgetReceiver: GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget
        get() = DeviceCommunicationWidget
}

class ReloadActionCallback: ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        var communicationList: MutableList<Message> = mutableListOf()

        updateAppWidgetState(context, glanceId) { prefs ->
            val messagesManager = MessagesManager(globalUser!!.id!!, context)

//            val coroutineScope = coroutineScope {
//                launch {
//                    messagesManager.getReceivedMessages { list: List<Message> ->
//                        communicationList = list
//                            .filter { it.messageType == Message.MessageType.NOTIFICATION }
//                            .sortedByDescending { it.messageDate }.take(1)
//                            .toMutableList()
//                    }
//                }
//            }

            messagesManager.getReceivedMessages { list: List<Message> ->
                communicationList = list
                    .filter { it.messageType == Message.MessageType.NOTIFICATION }
                    .sortedByDescending { it.messageDate }.take(1)
                    .toMutableList()
            }

            delay(1000)

            val currentCommunication = communicationList[0].body
            println("AAAAAAAAA" + currentCommunication)
            prefs[DeviceCommunicationWidget.communicationKey] = currentCommunication!!
        }
        DeviceCommunicationWidget.update(context, glanceId)
    }
}