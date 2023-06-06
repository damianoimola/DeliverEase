package com.madm.deliverease.engineering

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.madm.common_libs.model.CalendarManager
import com.madm.common_libs.model.WorkDay


@Composable
fun DummyComposeFunctionForCalendar(){
    val context = LocalContext.current
    var calendar: List<WorkDay> by remember { mutableStateOf(listOf()) }

    Button(
        onClick = {
            testHandleCalendar({ list ->
                calendar = list
                println("########## DONE")
           }, context)
        }
    ) {
        Text("CLICK ME")
    }

    ReceivedDays(calendar)
}



fun testHandleCalendar(
    receivedCalendarCallback: (List<WorkDay>) -> Unit,
    context: Context
) {
    val calendarManager : CalendarManager = CalendarManager(context)

    calendarManager.getDays{ receivedCalendarCallback(it) }
}

@Composable
fun ReceivedDays(days: List<WorkDay>) {
    Column() {
        days.forEach{
            Text(text = "Date: ${it.workDayDate}, IDs: ${it.riders}")
        }
    }
}