package com.madm.deliverease.ui.screens.riders

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.madm.deliverease.R
import com.madm.deliverease.ui.theme.mediumPadding
import com.madm.deliverease.ui.theme.nonePadding
import com.madm.deliverease.ui.theme.smallPadding
import com.madm.deliverease.ui.widgets.MyPageHeader
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import java.util.*


@Preview
@Composable
fun PreferenceScreen(){
    var indexOfSelectedWeek : Int by remember { mutableStateOf(1) }
    val currentMonth = Calendar.getInstance().get(Calendar.MONTH)
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)

    val months = (currentMonth..currentMonth + 12).toList().toIntArray()
    var selectedMonth by remember { mutableStateOf(months[0]) }
    var selectedYear by remember { mutableStateOf(currentYear) }

    Column {
        MyPageHeader()
        MonthSelector(months, selectedMonth, currentYear) { month: Int, isNextYear: Boolean ->
            selectedYear = if (isNextYear)
                currentYear + 1
            else currentYear
            selectedMonth = month
        }
        DaysList(selectedMonth, selectedYear) { weekNumber: Int -> indexOfSelectedWeek = weekNumber }
        WeekPreferences(indexOfSelectedWeek, selectedMonth, selectedYear)
    }
}


/*
This Composable function regards the upper part of the interface -> the calendar
 */
@Composable
fun DaysList(selectedMonth: Int, selectedYear: Int, function: (Int) -> Unit) {
    val currentWeek = Calendar.getInstance().get(Calendar.WEEK_OF_MONTH)

    // list of all mondays (first day of week) of the selected month
    val mondaysList = getMondaysAfterCurrentDay(selectedYear, selectedMonth+1)
        .toList()
        .toIntArray()
        .map { i -> i.integerToTwoDigit() }

    // list of all days of the selected week
    var daysList by rememberSaveable { mutableStateOf(getWeekDays(selectedYear, selectedMonth+1, currentWeek)) }

    // the selected week
    var selectedWeek by rememberSaveable { mutableStateOf(mondaysList[0]) }

    Row (
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())

    ){
        mondaysList.forEach {
            Button(
                onClick = {
                    function(mondaysList.indexOf(it) + 1)
                    selectedWeek = it
                    // update the list of days of the selected week
                    daysList = getWeekDays(selectedYear, selectedMonth+1, mondaysList.indexOf(it) + 1)

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
            text = "${daysList.first().number} ${MonthMap[selectedMonth]} - ${daysList.last().number} ${ if(daysList.first().number>daysList.last().number) MonthMap[(selectedMonth + 1)%12] else MonthMap[selectedMonth]}",
            style = TextStyle(
                fontSize = 25.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFFFF9800)
            )
        )
    }
}




/*
This composable function regards the lower layer of the interface
 */
@Composable
fun WeekPreferences(indexOfSelectedWeek: Int, currentMonth: Int, currentYear: Int) {
    val days = getWeekDays(currentYear, currentMonth+1, indexOfSelectedWeek)

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround,
        modifier = Modifier.padding(top = mediumPadding)
    ) {
        items(days){
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

            ShiftOptions()
        }
    }
}




/*
This function regards the preference setting itself
 */
@Composable
fun ShiftOptions(){
    val radioOptions = listOf(stringResource(R.string.shift_yes), stringResource(R.string.shift_no), stringResource(
            R.string.shift_maybe), stringResource(R.string.shift_remember)
                )
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[0] ) }
    val checkedState = remember { mutableStateOf(false) }


    LazyVerticalGrid(
        userScrollEnabled = true,
        columns = GridCells.Fixed(2),
        modifier = Modifier.height(100.dp),
        content = {

            items(radioOptions) { text ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = (text == selectedOption),
                            onClick = {
                                onOptionSelected(text)
                            }
                        )
                        .padding(horizontal = 4.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (text != "Remember choice") {
                        RadioButton(
                            selected = (text == selectedOption),
                            onClick = { onOptionSelected(text) },
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    } else {
                        Checkbox(
                            checked = checkedState.value,
                            onCheckedChange = { checkedState.value = it },
                            modifier = Modifier.padding(start = 4.dp)
                        )

                    }

                    Text(
                        text = text,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
            }
        }
    )
}




/*
This function is used to calculate the mondays after the current date (modification of getMonday by Damiano)
 */
fun getMondaysAfterCurrentDay(year: Int, month: Int): List<Int> {
    val firstOfMonth = LocalDate.of(year, month, 1)
    val lastOfMonth = LocalDate.of(year, month, 1).with(TemporalAdjusters.lastDayOfMonth())
    val today = LocalDate.now()

    val mondays = mutableListOf<Int>()
    var currentDate = firstOfMonth.with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY))

    while (currentDate.isBefore(lastOfMonth) || currentDate.isEqual(lastOfMonth)) {
        if(currentDate > today) {
            mondays.add(currentDate.dayOfMonth)
        }
        currentDate = currentDate.with(TemporalAdjusters.next(DayOfWeek.MONDAY))
    }

    return mondays
}

