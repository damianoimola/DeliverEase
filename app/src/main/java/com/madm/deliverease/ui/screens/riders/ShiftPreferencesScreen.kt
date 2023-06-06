package com.madm.deliverease.ui.screens.riders

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.madm.common_libs.model.*
import com.madm.deliverease.R
import com.madm.deliverease.globalUser
import com.madm.deliverease.ui.widgets.*
import java.time.LocalDate
import java.time.ZoneId
import java.util.*
import java.util.Calendar


val ShiftOptionMap = mapOf(
    "open" to 0,
    "heavy" to 1,
    "light" to 2
)

val ReverseShiftOptionMap = mapOf(
    0 to "open",
    1 to "heavy",
    2 to "light"
)

fun getDayOfWeekNumber(month: Int, year: Int, day: Int): Int {
    val date = LocalDate.of(year, month, day)
    val dayOfWeek = date.dayOfWeek
    val dayOfWeekNumber = (dayOfWeek.value - 1) % 7
    return dayOfWeekNumber
}

@Composable
fun ShiftPreferenceScreen(){
    var selectedWeek : Int by remember { mutableStateOf(Calendar.getInstance().get(Calendar.WEEK_OF_MONTH) - 1) }
    val currentMonth = Calendar.getInstance().get(Calendar.MONTH)
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)

    val months = (currentMonth..currentMonth + 11).map{i -> i%12}.toList().toIntArray()
    var selectedMonth by remember { mutableStateOf(months[0]) }
    var selectedYear by remember { mutableStateOf(currentYear) }

    val permanentConstraints = globalUser!!.permanentConstraints.sortedByDescending { x -> x.dayOfWeek }
    val nonPermanentConstraints = globalUser!!.nonPermanentConstraints.sortedByDescending { x -> x.constraintDate }

    val context = LocalContext.current

    Column {
        MyPageHeader()
        MonthSelector(months, selectedMonth, currentYear) { month: Int, isNextYear: Boolean ->
            selectedYear = if (isNextYear)
                currentYear + 1
            else currentYear
            selectedMonth = month
        }
        WeeksList(selectedMonth, selectedYear, selectedWeek, false) { weekNumber: Int -> selectedWeek = weekNumber }
        WeekContent(selectedWeek, selectedMonth, selectedYear) {
            // retrieve the selected date in a full format
            val selectedDateFormatted = if (it.number < 7 && selectedWeek != 0)
                Date.from(LocalDate.of(selectedYear, (selectedMonth+2)%12, it.number).atStartOfDay(ZoneId.systemDefault()).toInstant())
            else
                Date.from(LocalDate.of(selectedYear, (selectedMonth+1)%12, it.number).atStartOfDay(ZoneId.systemDefault()).toInstant())

            ShiftOptions(
                permanentConstraint = permanentConstraints.firstOrNull { c -> c.dayOfWeek == getDayOfWeekNumber((selectedMonth+1)%12, selectedYear, it.number) },
                nonPermanentConstraint = nonPermanentConstraints.firstOrNull{ c -> c.constraintDate == selectedDateFormatted },
                onComplete = { kind: Int, permanent: Boolean ->
                    if(permanent) {
                        val tmpConstraint =
                            permanentConstraints.firstOrNull { c -> c.dayOfWeek == getDayOfWeekNumber((selectedMonth+1)%12, selectedYear, it.number) }


                        if(tmpConstraint == null)
                            globalUser!!.permanentConstraints.add(
                                PermanentConstraint(
                                    getDayOfWeekNumber((selectedMonth+1)%12, selectedYear, it.number),
                                    ReverseShiftOptionMap[kind]
                                )
                            )
                        else
                            globalUser!!.permanentConstraints.first{ c ->
                                c.dayOfWeek == getDayOfWeekNumber((selectedMonth+1)%12, selectedYear, it.number)
                            }.type = ReverseShiftOptionMap[kind]
                    } else {
                        val tmpConstraint =
                            nonPermanentConstraints.firstOrNull { c -> c.constraintDate == selectedDateFormatted }

                        if(tmpConstraint == null) {
                            val npc = NonPermanentConstraint(ReverseShiftOptionMap[kind])
                            npc.constraintDate = selectedDateFormatted

                            globalUser!!.nonPermanentConstraints.add(npc)
                        }
                        else globalUser!!.nonPermanentConstraints.first { c -> c.constraintDate == selectedDateFormatted }.type = ReverseShiftOptionMap[kind]
                    }

                    globalUser!!.registerOrUpdate(context)
                }
            )
        }
    }
}




@Composable
fun ShiftOptions(
    permanentConstraint: PermanentConstraint?,
    nonPermanentConstraint: NonPermanentConstraint?,
    onComplete: (option: Int, permanent:Boolean) -> Unit,
){
    val radioOptions = listOf(
        stringResource(R.string.shift_yes),
        stringResource(R.string.shift_no),
        stringResource(R.string.shift_maybe),
        stringResource(R.string.shift_remember)
    )

    var checkedState by remember { mutableStateOf(permanentConstraint != null) }

    // 0=IN, 1=NOT_IN, 2=PREFER_NO -> mapped with below array of strings
    val permanentPreferenceKind = if (permanentConstraint != null)
        ShiftOptionMap[permanentConstraint.type]!!
    else 0

    val nonPermanentPreferenceKind = if (nonPermanentConstraint != null)
        ShiftOptionMap[nonPermanentConstraint.type]!!
    else 0

    var actualOption by remember{
        mutableStateOf(
            if(nonPermanentConstraint != null)
                nonPermanentPreferenceKind
            else permanentPreferenceKind
        )
    }

    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[actualOption]) }
    onOptionSelected(radioOptions[actualOption])

    LazyVerticalGrid(
        userScrollEnabled = true,
        columns = GridCells.Fixed(2),
        modifier = Modifier.height(150.dp),
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
                    if (text != stringResource(R.string.shift_remember)) {
                        RadioButton(
                            selected = (text == selectedOption),
                            onClick = {
                                onOptionSelected(text)
                                actualOption = radioOptions.indexOf(text)
                            },
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    } else {
                        Checkbox(
                            checked = checkedState,
                            onCheckedChange = {
                                checkedState = it
                            },
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }

                    Text(text = text, modifier = Modifier.padding(start = 4.dp))
                }
            }

            item (span = { GridItemSpan(maxLineSpan) }) {
                Row (
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ){
                    Button(
                        onClick = {
                            onComplete(
                                radioOptions.indexOf(selectedOption),
                                checkedState
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            stringResource(R.string.save_your_preference),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    )
}
