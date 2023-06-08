package com.madm.deliverease.ui.screens.riders

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.madm.common_libs.model.*
import com.madm.deliverease.R
import com.madm.deliverease.globalUser
import com.madm.deliverease.ui.theme.smallPadding
import com.madm.deliverease.ui.widgets.MonthSelector
import com.madm.deliverease.ui.widgets.MyPageHeader
import com.madm.deliverease.ui.widgets.WeekContent
import com.madm.deliverease.ui.widgets.WeeksList
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import java.util.Calendar

@Preview
@Composable
fun CalendarScreen(){
    var selectedWeek : Int by remember { mutableStateOf(Calendar.getInstance().get(Calendar.WEEK_OF_MONTH)) }
    val currentMonth = Calendar.getInstance().get(Calendar.MONTH)
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)

    val months = (currentMonth..currentMonth + 11).toList().map{i-> i%12}.toIntArray()
    var selectedMonth by remember { mutableStateOf(months[0]) }
    var selectedYear by remember { mutableStateOf(currentYear) }

    val swap = remember{ mutableStateOf(false)}


    // getting API data
    var shiftList : List<WorkDay> by rememberSaveable { mutableStateOf(listOf()) }

    val calendarManager : CalendarManager =
        CalendarManager(LocalContext.current)

    calendarManager.getDays{ list: List<WorkDay> ->
        shiftList = list.filter { it.riders!!.contains(globalUser!!.id) }
    }


    Column {
//        MyPageHeader()
        MonthSelector(months, selectedMonth, currentYear) { month: Int, isNextYear: Boolean ->
            selectedYear = if (isNextYear)
                currentYear + 1
            else currentYear
            selectedMonth = month
        }
        WeeksList(selectedMonth, selectedYear, selectedWeek, false) { weekNumber: Int -> selectedWeek = weekNumber }
        WeekContent(selectedWeek, selectedMonth, selectedYear) { weekDay ->
            ShiftRow(
                shiftList.any {
                    var selectedDate: LocalDate? = null

                    // create a date from selected day
                    selectedDate = if (weekDay.number < 7 && selectedWeek != 0)
                        LocalDate.of(selectedYear, (selectedMonth + 2)%11, weekDay.number)
                    else
                        LocalDate.of(selectedYear, (selectedMonth + 1)%11, weekDay.number)

                    // converting API date
                    val inputDateString = it.workDayDate.toString()
                    val inputDateFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH)
                    val outputDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val date: Date = inputDateFormat.parse(inputDateString)!!
                    val outputDateString: String = outputDateFormat.format(date)

                    outputDateString == selectedDate.toString()
                }, swap
            )
            swap.value = false
        }
    }
}






@Composable
fun ShiftRow(haveAShift: Boolean, swap: MutableState<Boolean>){
    val currentRandomVal = (0..1).random()

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20))
            .background(color =
            (if (!haveAShift && !swap.value) {Color.LightGray }
            else if( !swap.value) { Color(0xFFFF9800)}
            else if(!haveAShift && swap.value){
                Color(0xFFFF9800)
            } else {
                Color.LightGray
            })
            )
            .fillMaxWidth()
            .padding(smallPadding).clickable { /*TODO*/ }
    ) {
        Text(
            text = (if (!haveAShift && !swap.value) {"No shift"}
            else if(!swap.value){
             "You have a shift!"}
            else if(!haveAShift && swap.value){
                 "Click here to swap day"
            } else {
                "You already have a turn"
            }),
            style = TextStyle(color = Color.White),
        )

        if (haveAShift && !swap.value) {

            Icon(
                painter = painterResource(id = R.drawable.swap),
                contentDescription = "shift change",
                modifier = Modifier
                    .padding(start = 140.dp, top = 1.dp)
                    .size(28.dp)
                    .align(Alignment.CenterEnd)
                    .clickable { swap.value = true },
                tint = Color.Red
            )
        }

    }
}










