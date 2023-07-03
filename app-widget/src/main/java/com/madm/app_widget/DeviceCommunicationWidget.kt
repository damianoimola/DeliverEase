package com.madm.app_widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.*
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.layout.*
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.madm.common_libs.model.Message
import com.madm.common_libs.model.MessagesManager
import kotlinx.coroutines.*

object DeviceCommunicationWidget: GlanceAppWidget() {

    val communicationKey = stringPreferencesKey("communication")
    val dateKey = stringPreferencesKey("date")

    @Composable
    override fun Content() {
        val communication = currentState(key = communicationKey) ?: ""
        val date = currentState(key = dateKey) ?: ""
        Column(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(Color(0xFFF4F4F4)),
            verticalAlignment = Alignment.Vertical.CenterVertically,
            horizontalAlignment = Alignment.Horizontal.CenterHorizontally
        ) {
            Text(
                text = communication,
                style = TextStyle(
                    fontWeight = FontWeight.Medium,
                    color = ColorProvider(Color(0xFF263330)),
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                ),
                modifier = GlanceModifier.padding(4.dp),
                
            )
            Row(horizontalAlignment = Alignment.End) {
                Text(
                    text = date,
                    style = TextStyle(
                        fontWeight = FontWeight.Medium,
                        color = ColorProvider(Color(0xFF263330)),
                        fontSize = 10.sp,
                        textAlign = TextAlign.End
                    ),
                    modifier = GlanceModifier.padding(10.dp)
                )
            }
            Button(
                text = "Reload",
                onClick = actionRunCallback(ReloadActionCallback::class.java),
                colors = ButtonColors(ColorProvider(Color(0xFFB94434)), ColorProvider(Color(0xFFFFF7F7)))
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
            val messagesManager = MessagesManager("0", context)

            messagesManager.getReceivedMessages { list: List<Message> ->
                communicationList = list
                    .filter { it.messageType == Message.MessageType.NOTIFICATION }
                    .reversed()
                    .take(1)
                    .toMutableList()
            }

            delay(2000)

            val currentCommunication = communicationList[0].body
            val currentDate = communicationList[0].date

            prefs[DeviceCommunicationWidget.communicationKey] = currentCommunication!!
            prefs[DeviceCommunicationWidget.dateKey] = currentDate!!
        }
        DeviceCommunicationWidget.update(context, glanceId)
    }
}