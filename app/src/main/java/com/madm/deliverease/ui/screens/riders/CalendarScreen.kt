package com.madm.deliverease.ui.screens.riders

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.madm.deliverease.ui.theme.mediumPadding
import com.madm.deliverease.ui.theme.nonePadding
import com.madm.deliverease.ui.theme.smallPadding
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import java.util.*


val MonthMap = mapOf(
    0 to "January",
    1 to "February",
    2 to "March",
    3 to "April",
    4 to "May",
    5 to "June",
    6 to "July",
    7 to "August",
    8 to "September",
    9 to "October",
    10 to "November",
    11 to "December"
)

val ReverseMonthMap = mapOf(
     "January" to 0,
     "February" to 1,
     "March" to 2,
     "April" to 3,
     "May" to 4,
     "June" to 5,
     "July" to 6,
     "August" to 7,
     "September" to 8,
     "October" to 9,
     "November" to 10,
     "December" to 11,
)
data class Day(val number: Int, val name: String)

fun getWeekDays(year: Int, month: Int, week: Int): List<Day> {
    val firstDayOfMonth = LocalDate.of(year, month, 1)
    val firstDayOfWeek = firstDayOfMonth.with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY))
        .plusWeeks(week.toLong() - 1)

    val days = mutableListOf<Day>()
    var currentDate = firstDayOfWeek

    for (i in 0 until 7) {
        val dayNumber = currentDate.dayOfMonth
        val dayName = currentDate.dayOfWeek.getDisplayName(java.time.format.TextStyle.FULL, Locale.getDefault())
        days.add(Day(dayNumber, dayName))
        currentDate = currentDate.plusDays(1)
    }

    return days
}

fun getMondays(year: Int, month: Int): List<Int> {
    val firstOfMonth = LocalDate.of(year, month, 1)
    val lastOfMonth = LocalDate.of(year, month, 1).with(TemporalAdjusters.lastDayOfMonth())

    val mondays = mutableListOf<Int>()
    var currentDate = firstOfMonth.with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY))

    while (currentDate.isBefore(lastOfMonth) || currentDate.isEqual(lastOfMonth)) {
        mondays.add(currentDate.dayOfMonth)
        currentDate = currentDate.with(TemporalAdjusters.next(DayOfWeek.MONDAY))
    }

    return mondays
}

fun Int.integerToTwoDigit() : String {
    return if(this < 10)
        "0$this"
    else "$this"
}








@Composable
fun CalendarScreen(){
    var indexOfSelectedWeek : Int by remember { mutableStateOf(1) }
    val currentMonth = Calendar.getInstance().get(Calendar.MONTH)
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)

    val months = (currentMonth..currentMonth + 11)
        .toList()
        .toIntArray()
        .map { i -> MonthMap[i%12]!! }
    var selectedMonth by remember { mutableStateOf(months[0]) }
    var selectedYear by remember { mutableStateOf(currentYear) }


    Column {
        MonthSelector(months, selectedMonth, currentYear) { month: String, isNextYear: Boolean ->
            println("########## NEXT YEAR $isNextYear")
            selectedYear = if (isNextYear)
                currentYear + 1
            else currentYear
            selectedMonth = month
        }
        WeeksList(ReverseMonthMap[selectedMonth]!!, selectedYear) { weekNumber: Int -> indexOfSelectedWeek = weekNumber }
        WeekShifts(indexOfSelectedWeek, ReverseMonthMap[selectedMonth]!!, selectedYear)
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MonthSelector(
    months: List<String>,
    selectedMonth: String,
    currentYear: Int,
    function: (String, Boolean) -> Unit
){
    var expanded by remember { mutableStateOf(false) }
    var isNextYear = false
    var isNextYearSelected by remember {
        mutableStateOf(false)
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.width(IntrinsicSize.Min)
    ) {
        TextField(
            value = if(!isNextYearSelected)
                "$selectedMonth $currentYear"
            else
                "$selectedMonth ${currentYear + 1}",
            onValueChange = { },
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            modifier = Modifier.width(IntrinsicSize.Min)
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.width(IntrinsicSize.Min)
        ) {
            months.forEach { option ->
                // menu item
                DropdownMenuItem(
                    onClick = {
                        isNextYearSelected = ReverseMonthMap[option]!! < ReverseMonthMap[months[0]]!!
                        function(option, isNextYearSelected)
                        expanded = false
                    }
                ) {
                    if(!isNextYear)
                        Text(text = "$option $currentYear")
                    else
                        Text(text = "$option ${currentYear + 1}")
                }

                if(option == "December")
                    isNextYear = true
            }
        }
    }
}


@Composable
fun WeeksList(selectedMonth: Int, selectedYear: Int, function: (Int) -> Unit) {
    // retrieve the list of all mondays
    val mondayList = getMondays(selectedYear, selectedMonth+1)

    // list of strings in this format: "01 January"
    val weeksList = mondayList
        .toList()
        .toIntArray()
        .map { i -> "${i.integerToTwoDigit()} ${MonthMap[selectedMonth]!!}" }

    // retrieve the selected week
    var selectedWeek by remember { mutableStateOf(weeksList[0]) }


    // retrieve the last day (number) of the selected week
    var lastDayOfWeek by remember { mutableStateOf(
            (Integer.parseInt(selectedWeek.subSequence(0..1) as String) + 6).toString()
        )
    }

    // retrieve the month (string) of the selected week
    val monthOfWeek by remember {
        mutableStateOf(selectedWeek.subSequence(2 until selectedWeek.length))
    }

    Row (
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
    ){
        weeksList.forEach {
            Button(
                onClick = {
                    function(weeksList.indexOf(it) + 1)
                    selectedWeek = it
                    lastDayOfWeek = (Integer.parseInt(it.subSequence(0..1) as String) + 6).toString()
                },
                elevation = ButtonDefaults.elevation(
                    defaultElevation = 6.dp,
                    pressedElevation = 8.dp,
                    disabledElevation = 2.dp
                ),
                modifier = Modifier
                    .padding(smallPadding, smallPadding)
                    .clip(shape = RoundedCornerShape(20)),
                colors = ButtonDefaults.buttonColors(
                    if (selectedWeek == it) Color(0xFFFF9800)
                    else Color(0xFFFF5722)
                ),
                border = BorderStroke(width = 1.dp, color = Color.Red),
                shape = RoundedCornerShape(20)
            ) {
                Text(
                    text = it,
                    color = Color.White
                )
            }

        }
    }

    Row (
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.Start
    ){
        Text(
            text = "Week: ",
            style = TextStyle(
                fontSize = 25.sp,
                fontWeight = FontWeight.Normal
            )
        )
        Text(
            text = "$selectedWeek - $lastDayOfWeek $monthOfWeek",
            style = TextStyle(
                fontSize = 25.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFFFF9800)
            )
        )
    }

    Row {
        Text(text = "You have")
        Text(
            text = " 3 ",
            style = TextStyle(
                color = Color(0xFFFF9800),
                fontWeight = FontWeight.Bold
            ),
        )
        Text(text = " shifts assigned")
    }
}


@Composable
fun WeekShifts(weekNumber: Int, selectedMonth: Int, selectedYear: Int){
    val days = getWeekDays(selectedYear, selectedMonth+1, weekNumber)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround,
        modifier = Modifier
            .padding(top = mediumPadding)
            .fillMaxHeight()
    ) {
        days.forEach { 
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(nonePadding, smallPadding)
            ) {
                Text(text = "${it.number} ${it.name}")
                Divider(modifier = Modifier
                    .fillMaxWidth()
                    .width(2.dp)
                    .padding(start = smallPadding))
            }

            val currentRandomVal = (0..1).random()
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(20))
                    .background(
                        color = if (currentRandomVal == 0) Color.LightGray else Color(
                            0xFFFF9800
                        )
                    )
                    .fillMaxWidth()
                    .padding(smallPadding)
            ){
                Text(
                    text = if (currentRandomVal == 0) "No shift" else "You have a shift!",
                    style = TextStyle(color = Color.White)
                )
            }
        }
    }
}














