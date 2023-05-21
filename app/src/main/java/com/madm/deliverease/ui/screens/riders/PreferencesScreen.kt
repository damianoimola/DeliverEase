package com.madm.deliverease.ui.screens.riders

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
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


@Preview
@Composable
fun PreferenceScreen(){
    var indexOfSelectedWeek : Int by remember { mutableStateOf(1) }
    val currentDay = java.util.Calendar.getInstance().get(java.util.Calendar.DAY_OF_MONTH)
    val currentMonth = java.util.Calendar.getInstance().get(java.util.Calendar.MONTH)
    val currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)

    val months = (currentMonth..currentMonth + 12)
        .toList()
        .toIntArray()
        .map { i -> MonthMap[i%12]!! }
    var selectedMonth by remember { mutableStateOf(months[0]) }

    Column{
        MonthSelector(months, selectedMonth) { item: String -> selectedMonth = item }
        DaysList(currentDay, currentMonth, currentYear) { weekNumber: Int -> indexOfSelectedWeek = weekNumber }
        WeekPreferences(indexOfSelectedWeek, currentMonth, currentYear)
    }
}

@Composable
fun WeekPreferences(indexOfSelectedWeek: Int, currentMonth: Int, currentYear: Int) {
    val days = getWeekDays(currentYear, currentMonth+1, indexOfSelectedWeek)

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

                ShiftOptions()

        }
    }
}

@Preview
@Composable
fun ShiftOptions(){
    val radioOptions = listOf(R.string.shift_yes, R.string.shift_no, R.string.shift_maybe, R.string.remember_choise)
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[0] ) }
    val checkedState = remember { mutableStateOf(false) }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            content = {
                items(radioOptions) { text ->
                        Row(
                            Modifier
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
                            if (text != R.string.remember_choise) {
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
                                text = stringResource(id = text),
                                modifier = Modifier.padding(start = 4.dp)
                            )
                        }
                    }
            }
        )

}


@Composable
fun DaysList(currentDay: Int, selectedMonth: Int, selectedYear: Int, function: (Int) -> Unit) {
    var weekNumber = 1
    val mondayList = getMondays(selectedYear, selectedMonth+1)

    val weeks = mondayList
        .toList()
        .toIntArray()
        .filter {
           i -> i >= currentDay
        }
        .map {
                i -> "${i.integerToTwoDigit()} ${MonthMap[selectedMonth]!!}"
        }


    var selectedWeek by remember { mutableStateOf(weeks[0]) }
    var lastDayOfWeek by remember { mutableStateOf(
        (Integer.parseInt(selectedWeek.subSequence(0..1) as String) + 6).toString()
    )
    }
    val remainderString by remember {
        mutableStateOf(selectedWeek.subSequence(2 until selectedWeek.length))
    }

    Row (
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())

    ){
        weeks.forEach {
            Button(
                onClick = {
                    function(weekNumber)
                    selectedWeek = it
                    lastDayOfWeek = (Integer.parseInt(it.subSequence(0..1) as String) + 6).toString()
                    weekNumber += 1
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
            text = "$selectedWeek - $lastDayOfWeek $remainderString",
            style = TextStyle(
                fontSize = 25.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFFFF9800)
            )
        )
    }
}