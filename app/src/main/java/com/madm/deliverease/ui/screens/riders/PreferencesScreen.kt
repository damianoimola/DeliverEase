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
import androidx.compose.foundation.verticalScroll
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
import java.util.*


@Preview
@Composable
fun PreferenceScreen(){
    var indexOfSelectedWeek : Int by remember { mutableStateOf(1) }
    val currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
    val currentMonth = Calendar.getInstance().get(Calendar.MONTH)
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)

    val months = (currentMonth..currentMonth + 12).toList().toIntArray()
    var selectedMonth by remember { mutableStateOf(months[0]) }
    var selectedYear by remember { mutableStateOf(currentYear) }

    Column {
        MonthSelector(months, selectedMonth, currentYear) { month: Int, isNextYear: Boolean ->
            selectedYear = if (isNextYear)
                currentYear + 1
            else currentYear
            selectedMonth = month
        }
        DaysList(currentDay, currentMonth, currentYear) { weekNumber: Int -> indexOfSelectedWeek = weekNumber }
        WeekPreferences(indexOfSelectedWeek, currentMonth, currentYear)
    }
}

@Composable
fun DaysList(currentDay: Int, selectedMonth: Int, selectedYear: Int, function: (Int) -> Unit) {
    val currentWeek = Calendar.getInstance().get(Calendar.WEEK_OF_MONTH)

    // list of all mondays (first day of week) of the selected month
    val mondaysList = getMondays(selectedYear, selectedMonth+1)
        .toList()
        .toIntArray()
        .map { i -> i.integerToTwoDigit() }

    // list of all days of the selected week
    var daysList by rememberSaveable { mutableStateOf(getWeekDays(selectedYear, selectedMonth+1, currentWeek)) }

    // the selected week
    var selectedWeek by rememberSaveable { mutableStateOf(mondaysList[0]) }


//
//    var weekNumber = 1
//    val mondayList = getMondays(selectedYear, selectedMonth+1)
//
//    val weeks = mondayList
//        .toList()
//        .toIntArray()
//        .filter {
//                i -> i >= currentDay
//        }
//        .map {
//                i -> "${i.integerToTwoDigit()} ${MonthMap[selectedMonth]!!}"
//        }
//
//
//    var selectedWeek by remember { mutableStateOf(weeks[0]) }
//    var lastDayOfWeek by remember { mutableStateOf(
//        (Integer.parseInt(selectedWeek.subSequence(0..1) as String) + 6).toString()
//    )
//    }
//    val remainderString by remember {
//        mutableStateOf(selectedWeek.subSequence(2 until selectedWeek.length))
//    }

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

//                    function(weekNumber)
//                    selectedWeek = it
//                    weekNumber += 1
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






@OptIn(ExperimentalLayoutApi::class)
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




@Preview
@Composable
fun ShiftOptionsV1(){
    val radioOptions = listOf("Yes", "No", "Maybe", "Remember choice")
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[0] ) }
    val checkedState = remember { mutableStateOf(false) }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
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
                    }else{
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

