package com.madm.deliverease.ui.screens.riders

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.madm.common_libs.model.NonPermanentConstraint
import com.madm.common_libs.model.PermanentConstraint
import com.madm.deliverease.R
import com.madm.deliverease.globalAllUsers
import com.madm.deliverease.globalUser
import com.madm.deliverease.ui.widgets.*
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import java.time.temporal.WeekFields
import java.util.*


fun getDate(dayOfWeek: DayOfWeek, weekNumber: Int, month: Int, year: Int): LocalDate {
    val locale = Locale.getDefault()
    val weekFields = WeekFields.of(locale)

    val firstDayOfMonth = YearMonth.of(year, month).atDay(1)
    val firstDayOfWeek = weekFields.firstDayOfWeek

    val adjustedWeekNumber = if (firstDayOfWeek == DayOfWeek.SUNDAY && weekNumber == 1)
        weekNumber + 1
    else weekNumber

    val firstInTargetWeek = firstDayOfMonth
        .with(weekFields.weekOfMonth(), adjustedWeekNumber.toLong())
        .with(firstDayOfWeek)

    return firstInTargetWeek.plusDays(dayOfWeek.value.toLong() - 1)
}


val ShiftOptionMap = mapOf(
    "heavy" to 1,
    "light" to 2
)





@Preview
@Composable
fun ShiftPreferenceScreen(){
    var indexOfSelectedWeek : Int by remember { mutableStateOf(1) }
    val currentMonth = Calendar.getInstance().get(Calendar.MONTH)
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)

    val months = (currentMonth..currentMonth + 11).map{i -> i%12}.toList().toIntArray()
    var selectedMonth by remember { mutableStateOf(months[0]) }
    var selectedYear by remember { mutableStateOf(currentYear) }

    val permanentConstraints = globalUser?.permanentConstraints
    val nonPermanentConstraints = globalUser?.nonPermanentConstraints

    println("PERMANENT $permanentConstraints \n NON PERMANENT $nonPermanentConstraints")

    Column {
        MyPageHeader()
        MonthSelector(months, selectedMonth, currentYear) { month: Int, isNextYear: Boolean ->
            selectedYear = if (isNextYear)
                currentYear + 1
            else currentYear
            selectedMonth = month
        }
        WeeksList(selectedMonth, selectedYear, false) { weekNumber: Int -> indexOfSelectedWeek = weekNumber }
        WeekContent(indexOfSelectedWeek, selectedMonth, selectedYear) {
            ShiftOptions(
                permanentConstraint = permanentConstraints?.firstOrNull { c -> c.dayOfWeek == it.number },
                nonPermanentConstraint = nonPermanentConstraints?.firstOrNull{ c ->
//                    println("######### C DATE ${c.date}, LIST DATE ${Date.from(LocalDate.of(selectedYear, selectedMonth+1, it.number).atStartOfDay(ZoneId.systemDefault()).toInstant())}")
                    c.date == Date.from(
                        LocalDate.of(selectedYear, selectedMonth, it.number)
                            .atStartOfDay(ZoneId.systemDefault())
                            .toInstant()
                    )
                },
                onSelectOption = { kind: Int, permanent: Boolean ->
                    println("######### KIND: $kind, PERMANENT: $permanent")
                }
            )
        }
    }
}




@Composable
fun ShiftOptions(
    permanentConstraint: PermanentConstraint?,
    nonPermanentConstraint: NonPermanentConstraint?,
    onSelectOption: (option: Int, permanent:Boolean) -> Unit
){
    val radioOptions = listOf(
        stringResource(R.string.shift_yes),
        stringResource(R.string.shift_no),
        stringResource(R.string.shift_maybe),
        stringResource(R.string.shift_remember)
    )

    // 0=IN, 1=NOT_IN, 2=PREFER_NO -> mapped with below array of strings
    val permanentPreferenceKind by rememberSaveable {
        mutableStateOf(
            (if (permanentConstraint != null)
                if(ShiftOptionMap.containsKey(permanentConstraint.type))
                    ShiftOptionMap[permanentConstraint.type]!!
                else 0
            else 0)
        )
    }

    val nonPermanentPreferenceKind by rememberSaveable {
        mutableStateOf(
            (if (nonPermanentConstraint != null)
                if(ShiftOptionMap.containsKey(nonPermanentConstraint.type))
                    ShiftOptionMap[nonPermanentConstraint.type]!!
                else 0
            else 0)
        )
    }

    println("########### PERMA KIND $permanentPreferenceKind, ${permanentConstraint}")
    println("########### NON PERMA KIND $nonPermanentPreferenceKind, ${nonPermanentConstraint}")

    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[permanentPreferenceKind]) }
    val checkedState = remember { mutableStateOf(permanentConstraint != null) }


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
                            onClick = {
                                onOptionSelected(text)
                                onSelectOption(radioOptions.indexOf(text), checkedState.value)
                            },
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    } else {
                        Checkbox(
                            checked = checkedState.value,
                            onCheckedChange = {
                                checkedState.value = it
                                onSelectOption(radioOptions.indexOf(text), checkedState.value)
                            },
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
