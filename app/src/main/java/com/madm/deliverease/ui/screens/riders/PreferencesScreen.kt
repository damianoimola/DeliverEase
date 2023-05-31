package com.madm.deliverease.ui.screens.riders

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.madm.deliverease.R
import com.madm.deliverease.ui.widgets.*
import java.util.*


@Preview
@Composable
fun PreferenceScreen(){
    var indexOfSelectedWeek : Int by remember { mutableStateOf(1) }
    val currentMonth = Calendar.getInstance().get(Calendar.MONTH)
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)

    val months = (currentMonth..currentMonth + 11).map{i -> i%12}.toList().toIntArray()
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
        WeeksList(selectedMonth, selectedYear, true) { weekNumber: Int -> indexOfSelectedWeek = weekNumber }
        WeekContent(indexOfSelectedWeek, selectedMonth, selectedYear) { ShiftOptions() }
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
                    if (text != stringResource(id = R.string.shift_remember)) {
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
