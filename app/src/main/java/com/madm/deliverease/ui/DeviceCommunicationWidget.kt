package com.madm.deliverease.ui

import android.content.Context
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
import com.madm.deliverease.globalUser
import com.madm.deliverease.ui.theme.CustomTheme
import com.madm.deliverease.ui.theme.largePadding
import com.madm.deliverease.ui.theme.mediumPadding
import com.madm.deliverease.ui.theme.smallPadding
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
                .background(CustomTheme.colors.background),
            verticalAlignment = Alignment.Vertical.CenterVertically,
            horizontalAlignment = Alignment.Horizontal.CenterHorizontally
        ) {
            Text(
                text = communication,
                style = TextStyle(
                    fontWeight = FontWeight.Medium,
                    color = ColorProvider(CustomTheme.colors.onBackground),
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                ),
                modifier = GlanceModifier.padding(smallPadding),
                
            )
            Row(horizontalAlignment = Alignment.End) {
                Text(
                    text = date,
                    style = TextStyle(
                        fontWeight = FontWeight.Medium,
                        color = ColorProvider(CustomTheme.colors.onBackground),
                        fontSize = 10.sp,
                        textAlign = TextAlign.End
                    ),
                    modifier = GlanceModifier.padding(bottom = largePadding)
                )
            }
            Button(
                text = "Reload",
                onClick = actionRunCallback(ReloadActionCallback::class.java),
                colors = ButtonColors(ColorProvider(CustomTheme.colors.primary), ColorProvider(CustomTheme.colors.onPrimary))
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

            messagesManager.getReceivedMessages { list: List<Message> ->
                communicationList = list
                    .filter { it.messageType == Message.MessageType.NOTIFICATION }
                    .reversed()
                    .take(1)
                    .toMutableList()
            }

            delay(1000)

            val currentCommunication = communicationList[0].body
            val currentDate = communicationList[0].date

            prefs[DeviceCommunicationWidget.communicationKey] = currentCommunication!!
            prefs[DeviceCommunicationWidget.dateKey] = currentDate!!
        }
        DeviceCommunicationWidget.update(context, glanceId)
    }
}