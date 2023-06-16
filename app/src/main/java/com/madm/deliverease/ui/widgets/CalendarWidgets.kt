package com.madm.deliverease.ui.widgets

import android.os.Parcelable
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.madm.deliverease.R
import com.madm.deliverease.ui.theme.CustomTheme
import com.madm.deliverease.ui.theme.gilroy
import com.madm.deliverease.ui.theme.mediumPadding
import com.madm.deliverease.ui.theme.nonePadding
import com.madm.deliverease.ui.theme.smallPadding
import kotlinx.parcelize.Parcelize
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

@Parcelize
data class WeekDay(val number: Int, val name: String) : Parcelable

fun getWeekDays(year: Int, month: Int, week: Int): List<WeekDay> {
    val firstDayOfMonth = LocalDate.of(year, month, 1)
    val firstDayOfWeek = firstDayOfMonth.with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY))
        .plusWeeks(week.toLong() - 1)

    val weekDays = mutableListOf<WeekDay>()
    var currentDate = firstDayOfWeek

    for (i in 0 until 7) {
        val dayNumber = currentDate.dayOfMonth
        val dayName = currentDate.dayOfWeek.getDisplayName(java.time.format.TextStyle.FULL, Locale.getDefault())
        weekDays.add(WeekDay(dayNumber, dayName))
        currentDate = currentDate.plusDays(1)
    }

    return weekDays
}

fun Int.integerToTwoDigit() : String {
    return if(this < 10)
        "0$this"
    else "$this"
}


fun getMondays(year: Int, month: Int, afterCurrentDay : Boolean): List<Int> {
    val firstOfMonth = LocalDate.of(year, month, 1)
    val lastOfMonth = LocalDate.of(year, month, 1).with(TemporalAdjusters.lastDayOfMonth())
    val today = LocalDate.now()

    val mondays = mutableListOf<Int>()
    var currentDate = firstOfMonth.with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY))

    while (currentDate.isBefore(lastOfMonth) || currentDate.isEqual(lastOfMonth)) {
        if(afterCurrentDay && (currentDate > today)) {
            mondays.add(currentDate.dayOfMonth)
        } else if (!afterCurrentDay){
            mondays.add(currentDate.dayOfMonth)
        }

        currentDate = currentDate.with(TemporalAdjusters.next(DayOfWeek.MONDAY))
    }

    return mondays
}




@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MonthSelector(
    months: IntArray,
    selectedMonth: Int,
    currentYear: Int,
    function: (Int, Boolean) -> Unit
){
    var expanded by remember { mutableStateOf(false) }
    var isNextYearSelected by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.width(IntrinsicSize.Min)
    ) {
        TextField(
            value = if(!isNextYearSelected)
                "${MonthMap[selectedMonth]} $currentYear"
            else
                "${MonthMap[selectedMonth]} ${currentYear + 1}",
            onValueChange = { },
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = CustomTheme.colors.backgroundVariant,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                textColor = CustomTheme.colors.onBackgroundVariant,
                trailingIconColor = CustomTheme.colors.onBackgroundVariant
            ),
            modifier = Modifier.width(IntrinsicSize.Min)
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(IntrinsicSize.Min)
                .background(CustomTheme.colors.backgroundVariant)
        ) {
            months.forEach { option ->
                // menu item
                DropdownMenuItem(
                    onClick = {
                        isNextYearSelected = option < months[0]
                        function(option, isNextYearSelected)
                        expanded = false
                    },
                ) {
                    if(option < months[0]) {
                        Text(
                            "${MonthMap[option]} ${currentYear + 1}",
                            style = CustomTheme.typography.body2,
                            color = CustomTheme.colors.onBackgroundVariant
                        )
                    }
                    else {
                        Text(
                            "${MonthMap[option]} $currentYear",
                            style = CustomTheme.typography.body1,
                            color = CustomTheme.colors.onBackgroundVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun WeekContent(weekNumber: Int, selectedMonth: Int, selectedYear: Int, content: @Composable (WeekDay) -> Unit, lastItem: @Composable () -> Unit = {}){
    val days = getWeekDays(selectedYear, selectedMonth+1, weekNumber)

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround,
        modifier = Modifier
            .padding(top = mediumPadding)
            .fillMaxHeight()
    ) {
        itemsIndexed(days){_, day ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(nonePadding, smallPadding)
            ) {
                Text(
                    "${day.number} ${day.name}",
                    style = CustomTheme.typography.body1,
                    color = CustomTheme.colors.onBackground
                )
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .width(2.dp)
                        .padding(start = smallPadding),
                    color = CustomTheme.colors.onBackground
                )
            }

            content(day)
        }

        item {
            lastItem()
        }
    }
}


@Composable
fun WeeksList(selectedMonth: Int, selectedYear: Int, selectedWeek:Int, afterCurrentDay: Boolean, function: (Int) -> Unit) {
    // list of all mondays (first day of week) of the selected month
    val mondaysList =
        getMondays(selectedYear, (selectedMonth % 12) + 1, afterCurrentDay)
            .toList()
            .toIntArray()
            .map { i -> i.integerToTwoDigit() }

    // list of all days of the selected week
    var daysList = getWeekDays(selectedYear, (selectedMonth % 12) + 1, selectedWeek)

    // the selected week
    var selectedWeekString = mondaysList[selectedWeek-1]
    println("################# SELECTED WEEK $selectedWeek")
    println("################# MONDAY LIST $mondaysList")
    println("################# SELECTED WEEK STRING $selectedWeekString")

    Row (
        modifier = Modifier
            .fillMaxWidth().padding(0.dp)
            .horizontalScroll(rememberScrollState())
    ){
        mondaysList.forEach {
            Button(
                onClick = {
                    function(mondaysList.indexOf(it) + 1)
                    selectedWeekString = it
                    // update the list of days of the selected week
                    daysList = getWeekDays(selectedYear, selectedMonth+1, mondaysList.indexOf(it) + 1)
                },
//                elevation = ButtonDefaults.elevation(
//                    defaultElevation = 6.dp,
//                    pressedElevation = 8.dp,
//                    disabledElevation = 2.dp
//                ),
                modifier = Modifier
                    .padding(smallPadding, smallPadding)
                    .clip(shape = RoundedCornerShape(20)),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = if (selectedWeekString == it) CustomTheme.colors.tertiary else CustomTheme.colors.primary,
                    contentColor = if (selectedWeekString == it) CustomTheme.colors.onTertiary else CustomTheme.colors.onPrimary,
                ),
                border = BorderStroke(width = 1.dp, color = if (selectedWeekString == it) CustomTheme.colors.primary else CustomTheme.colors.tertiary),
                shape = RoundedCornerShape(20)
            ) {
                Text(
                    text = it,
                    color = Color.White,
                    style = TextStyle(
                        fontFamily = gilroy
                    )
                )
            }
        }
    }

    Row (
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier.padding(0.dp)
    ){
        Text(
            text = stringResource(R.string.week),
            style = CustomTheme.typography.h2,
            color = CustomTheme.colors.onBackground
        )
        Text(
            text = "${daysList.first().number} ${MonthMap[selectedMonth]} - ${daysList.last().number} ${ if(daysList.first().number>daysList.last().number) MonthMap[(selectedMonth + 1)%12] else MonthMap[selectedMonth]}",
            style = CustomTheme.typography.h2,
            color = CustomTheme.colors.tertiary
        )
    }
}
