package com.madm.deliverease.ui.screens.riders

import android.content.res.Configuration
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.madm.common_libs.model.*
import com.madm.deliverease.R
import com.madm.deliverease.globalUser
import com.madm.deliverease.ui.theme.CustomTheme
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
    return (dayOfWeek.value - 1) % 7
}

@Composable
fun ShiftPreferenceScreen(){
    val configuration = LocalConfiguration.current
    var selectedWeek: Int by remember { mutableStateOf(getCurrentWeekOfMonth()) }
    var currentMonth = Calendar.getInstance()[Calendar.MONTH]
    var currentYear = Calendar.getInstance()[Calendar.YEAR]
    var nextYear = false
    var nextMonth = false

    //getCurrentWeekOfMonth ritorna 11 se la settimana successiva ricade nel prossimo mese
    if(selectedWeek == 11)  nextMonth = true

    if(nextMonth){
        selectedWeek = 1
        currentMonth = getNextMonth()
    }

    if(nextMonth && currentMonth == 0){
        nextYear = true
        currentYear = getNextYear()
    }


    val months = (currentMonth ..currentMonth + 2).toList().map { i -> Math.floorMod(i, 12) }.toIntArray()
    var selectedMonth by remember { mutableStateOf(months[0]) }
    var selectedYear by remember { mutableStateOf(currentYear) }

    val permanentConstraints = globalUser!!.permanentConstraints.sortedByDescending { x -> x.dayOfWeek }
    val nonPermanentConstraints = globalUser!!.nonPermanentConstraints.sortedByDescending { x -> x.constraintDate }

    val context = LocalContext.current


    if(configuration.orientation == Configuration.ORIENTATION_PORTRAIT){
        Column {
            MonthSelector(months, selectedMonth, currentYear, nextYear) { month: Int, isNextYear: Boolean ->
                selectedYear = if (isNextYear)
                    currentYear + 1
                else currentYear
                selectedMonth = month
                selectedWeek = 1
            }
            WeeksList(selectedMonth, selectedYear, selectedWeek) { weekNumber: Int -> selectedWeek = weekNumber }
            WeekContent(selectedWeek, selectedMonth, selectedYear, {
                // retrieve the selected date in a full format
                val selectedDateFormatted = Date.from(localDateFormat(it, selectedWeek, selectedMonth).atStartOfDay(ZoneId.systemDefault()).toInstant())
                /*
                val selectedDateFormatted = if (it.number < 7 && (selectedWeek != 0 && selectedWeek != 1))
                    Date.from(LocalDate.of(selectedYear, (selectedMonth+2)%12, it.number).atStartOfDay(ZoneId.systemDefault()).toInstant())
                else
                    Date.from(LocalDate.of(selectedYear, (selectedMonth+1)%12, it.number).atStartOfDay(ZoneId.systemDefault()).toInstant())
*/
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
            })
        }
    } else {
        Row(modifier = Modifier.fillMaxSize()) {
            Column (modifier = Modifier.width(IntrinsicSize.Min)) {
                MonthSelector(months, selectedMonth, currentYear, nextYear) { month: Int, isNextYear: Boolean ->
                    selectedYear = if (isNextYear)
                        currentYear + 1
                    else currentYear
                    selectedMonth = month
                    selectedWeek = 1
                }
                WeeksList(selectedMonth, selectedYear, selectedWeek) { weekNumber: Int -> selectedWeek = weekNumber }
            }


            WeekContent(selectedWeek, selectedMonth, selectedYear, {
                // retrieve the selected date in a full format
                val selectedDateFormatted = Date.from(localDateFormat(it, selectedWeek, selectedMonth).atStartOfDay(ZoneId.systemDefault()).toInstant())


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
            })
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

    var actualOption = if(nonPermanentConstraint != null) nonPermanentPreferenceKind else permanentPreferenceKind

    var selectedOption by remember { mutableStateOf(radioOptions[actualOption]) }

    // TEST
    val shiftRememberString = stringResource(id = R.string.shift_remember)

    val onOptionSelected: (String) -> Unit = { if (it != shiftRememberString) selectedOption = it }
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
                            onClick = { onOptionSelected(text) }
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
                            modifier = Modifier.padding(start = 4.dp),
                            colors = RadioButtonDefaults.colors(
                                selectedColor = CustomTheme.colors.tertiary,
                                unselectedColor = CustomTheme.colors.tertiaryVariant
                            )
                        )
                    } else {
                        Checkbox(
                            checked = checkedState,
                            onCheckedChange = { checkedState = it },
                            modifier = Modifier.padding(start = 4.dp),
                            colors = CheckboxDefaults.colors(
                                checkedColor = CustomTheme.colors.tertiary,
                                uncheckedColor = CustomTheme.colors.tertiaryVariant,
                                checkmarkColor = CustomTheme.colors.onTertiary
                            )
                        )
                    }

                    Text(
                        text = text,
                        modifier = Modifier.padding(start = 4.dp),
                        style = CustomTheme.typography.body1,
                        color = CustomTheme.colors.onBackground
                    )
                }
            }

            item (span = { GridItemSpan(maxLineSpan) }) {
                Row (
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ){
                    DefaultButton(text = stringResource(R.string.save_your_preference), modifier = Modifier
                        .clip(shape = CustomTheme.shapes.large)
                        .fillMaxWidth()) {
                        onComplete(
                            radioOptions.indexOf(selectedOption),
                            checkedState
                        )
                    }

                }
            }
        }
    )
}
