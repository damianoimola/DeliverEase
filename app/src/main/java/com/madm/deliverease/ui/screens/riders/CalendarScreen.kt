package com.madm.deliverease.ui.screens.riders

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import com.madm.common_libs.model.CalendarManager
import com.madm.common_libs.model.Day
import com.madm.deliverease.globalUser
import com.madm.deliverease.ui.theme.smallPadding
import com.madm.deliverease.ui.widgets.MonthSelector
import com.madm.deliverease.ui.widgets.MyPageHeader
import com.madm.deliverease.ui.widgets.WeekContent
import com.madm.deliverease.ui.widgets.WeeksList
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.*







@Composable
fun CalendarScreen(){
    var indexOfSelectedWeek : Int by remember { mutableStateOf(1) }
    val currentMonth = Calendar.getInstance().get(Calendar.MONTH)
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)

    val months = (currentMonth..currentMonth + 11).toList().map{i-> i%12}.toIntArray()
    var selectedMonth by remember { mutableStateOf(months[0]) }
    var selectedYear by remember { mutableStateOf(currentYear) }


    // getting API data
    var shiftList : List<Day> by rememberSaveable { mutableStateOf(listOf()) }

    val calendarManager : CalendarManager = CalendarManager(LocalContext.current)

    calendarManager.getDays{ list: List<Day> ->
        shiftList = list.filter { it.riders!!.contains(globalUser!!.id) }
    }


    Column {
        MyPageHeader()
        MonthSelector(months, selectedMonth, currentYear) { month: Int, isNextYear: Boolean ->
            selectedYear = if (isNextYear)
                currentYear + 1
            else currentYear
            selectedMonth = month
        }
        WeeksList(selectedMonth, selectedYear, false) { weekNumber: Int -> indexOfSelectedWeek = weekNumber }
        WeekContent(indexOfSelectedWeek, selectedMonth, selectedYear) { weekDay ->
            ShiftRow(
                shiftList.any {
                    var selectedDate: LocalDate? = null

                    // create a date from selected day
                    selectedDate = if (weekDay.number < 7 && indexOfSelectedWeek != 0)
                        LocalDate.of(selectedYear, (selectedMonth + 2)%11, weekDay.number)
                    else
                        LocalDate.of(selectedYear, (selectedMonth + 1)%11, weekDay.number)

                    // converting API date
                    val inputDateString = it.date.toString()
                    val inputDateFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH)
                    val outputDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val date: Date = inputDateFormat.parse(inputDateString)!!
                    val outputDateString: String = outputDateFormat.format(date)

                    println("######## $selectedDate, $outputDateString")

                    outputDateString == selectedDate.toString()
                }
            )
        }
    }
}






@Composable
fun ShiftRow(haveAShift: Boolean){
    val currentRandomVal = (0..1).random()
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(20))
            .background(color = if (!haveAShift) Color.LightGray else Color(0xFFFF9800))
            .fillMaxWidth()
            .padding(smallPadding)
    ){
        Text(
            text = if (!haveAShift) "No shift" else "You have a shift!",
            style = TextStyle(color = Color.White)
        )
    }
}










