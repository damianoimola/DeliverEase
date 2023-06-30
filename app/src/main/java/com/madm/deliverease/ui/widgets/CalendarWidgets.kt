package com.madm.deliverease.ui.widgets

import android.os.Parcelable
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
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
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import java.time.temporal.WeekFields
import java.util.*


@Parcelize
data class WeekDay(val number: Int, val month: Int, val name: String) : Parcelable

/**
 * Returns a list of the week days for a specific week in the specified year and month.
 *
 * @param year The year.
 * @param month The month (1-12).
 * @param week The week number (1 and onwards).
 * @return A list of [WeekDay] objects representing the week days for the specified week in the specified year and month.
 */
fun getWeekDays(year: Int, month: Int, week: Int): List<WeekDay> {
    val firstDayOfMonth = LocalDate.of(year, month, 1)

    // Get the first day of the specified week in the specified month and year
    val firstDayOfWeek = firstDayOfMonth.with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY))
        .plusWeeks(week.toLong() - 1)

    val weekDays = mutableListOf<WeekDay>()
    var currentDate = firstDayOfWeek

    for (i in 0 until 7) {
        val dayNumber = currentDate.dayOfMonth
        val dayMonth = currentDate.monthValue
        val dayName = currentDate.dayOfWeek.getDisplayName(
            java.time.format.TextStyle.FULL,
            Locale.getDefault()
        )

        weekDays.add(WeekDay(dayNumber, dayMonth, dayName))
        currentDate = currentDate.plusDays(1)
    }

    return weekDays
}

fun Int.integerToTwoDigit(): String {
    return if (this < 10)
        "0$this"
    else "$this"
}


fun getNumberOfWeeks(month: Int, year: Int): Int {
    val yearMonth = YearMonth.of(year, month)
    val weekFields = WeekFields.of(Locale.getDefault())
    val firstOfMonth = yearMonth.atDay(1)
    val firstWeekOfMonth = firstOfMonth[weekFields.weekOfWeekBasedYear()]
    val lastOfMonth = yearMonth.atEndOfMonth()
    val lastWeekOfMonth = lastOfMonth[weekFields.weekOfWeekBasedYear()]

    return lastWeekOfMonth - firstWeekOfMonth + 1
}

/**
 * Returns a list of all the Mondays in the specified year and month.
 *
 * @param year The year.
 * @param month The month (1-12).
 * @return A list of integers representing the day of the month for all the Mondays in the specified year and month.
 */
fun getMondays(year: Int, month: Int): List<Int> {
    val firstOfMonth = LocalDate.of(year, month, 1)
    val lastOfMonth = LocalDate.of(year, month, 1).with(TemporalAdjusters.lastDayOfMonth())

    val mondays = mutableListOf<Int>()
    var currentDate = firstOfMonth.with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY))

    while (currentDate.isBefore(lastOfMonth) || currentDate.isEqual(lastOfMonth)) {
        /* if(afterCurrentDay && (currentDate > today)) {
             mondays.add(currentDate.dayOfMonth)
         } else if (!afterCurrentDay){

         */
        mondays.add(currentDate.dayOfMonth)
        //}

        currentDate = currentDate.with(TemporalAdjusters.next(DayOfWeek.MONDAY))
    }

    return mondays
}

/**
 * Checks if the selected week is before the current week.
 *
 * @param selectedYear The year of the selected week.
 * @param selectedMonth The month of the selected week (1-12).
 * @param selectedWeekOfMonth The week of the month of the selected week.
 * @return `true` if the selected week is before the current week, `false` otherwise.
 */
fun isWeekBeforeCurrentWeek(
    selectedYear: Int,
    selectedMonth: Int,
    selectedWeekOfMonth: Int
): Boolean {
    val currentWeek = LocalDate.now()[WeekFields.of(Locale.getDefault()).weekOfMonth()]
    val currentMonth = LocalDate.now().monthValue
    val currentYear = LocalDate.now().year

    return (selectedYear < currentYear) || (selectedYear == currentYear && selectedMonth < currentMonth) || (selectedYear == currentYear && selectedMonth == currentMonth && selectedWeekOfMonth < currentWeek)
}

/**
 * Returns a list of week dates in the specified format for a given year, month, and week of the month.
 *
 * @param year The year.
 * @param month The month (1-12).
 * @param weekOfMonth The week number of the month (1 and onwards).
 * @return A list of strings representing the week dates in the specified format.
 */
fun getWeekDatesInFormat(
    year: Int,
    month: Int,
    weekOfMonth: Int
): List<String> {
    val firstDayOfMonth = LocalDate.of(year, month, 1)
    val firstDayOfWeek = firstDayOfMonth.withDayOfMonth(1)
        .with(DayOfWeek.MONDAY)
        .plusWeeks((weekOfMonth - 1).toLong())

    val dates = mutableListOf<String>()
    val dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

    for (i in 0 until 7) {
        val currentDate = firstDayOfWeek.plusDays(i.toLong())
        dates.add(currentDate.format(dateFormatter))
    }

    return dates
}

/**
 * Returns the current week number of the month.
 *
 * @return The week number of the month (1 and onwards).
 */
fun getCurrentWeekOfMonth(): Int {
    val currentDate = LocalDate.now()
    val firstDayOfMonth = currentDate.with(TemporalAdjusters.firstDayOfMonth())
    val firstMonday = firstDayOfMonth.with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY))
    val currentMonday = currentDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
    return (currentMonday.dayOfMonth - firstMonday.dayOfMonth) / 7 + 1
}

/**
 * A composable function that displays a dropdown menu for selecting a month in the calendar
 *
 * @param months An array of integers representing the months (0-11) of the monthMap to consider.
 * @param selectedMonth The currently selected month.
 * @param currentYear The current year.
 * @param function A function that will be called when a month is selected.
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MonthSelector(
    months: IntArray,
    selectedMonth: Int,
    currentYear: Int,
    function: (Int, Boolean) -> Unit
){
    val monthMap = mapOf(
        0 to stringResource(R.string.january),
        1 to stringResource(R.string.february),
        2 to stringResource(R.string.march),
        3 to stringResource(R.string.april),
        4 to stringResource(R.string.may),
        5 to stringResource(R.string.june),
        6 to stringResource(R.string.july),
        7 to stringResource(R.string.august),
        8 to stringResource(R.string.september),
        9 to stringResource(R.string.october),
        10 to stringResource(R.string.november),
        11 to stringResource(R.string.december)
    )
    var expanded by remember { mutableStateOf(false) }
    var isNextYearSelected by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.width(IntrinsicSize.Min)
    ) {
        TextField(
            value = if(!isNextYearSelected)
                "${monthMap[selectedMonth]} $currentYear"
            else
                "${monthMap[selectedMonth]} ${currentYear + 1}",
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
                    if (option < months[0]) {
                        Text(
                            "${monthMap[option]} ${currentYear + 1}",
                            style = CustomTheme.typography.body2,
                            color = CustomTheme.colors.onBackgroundVariant
                        )
                    } else {
                        Text(
                            "${monthMap[option]} $currentYear",
                            style = CustomTheme.typography.body1,
                            color = CustomTheme.colors.onBackgroundVariant
                        )
                    }
                }
            }
        }
    }
}

/**
 * Renders the content of a week based on the selected month, year, and week number.
 *
 * @param weekNumber The week number to render.
 * @param selectedMonth The selected month.
 * @param selectedYear The selected year.
 * @param content The composable function that renders the content of each weekday.
 * @param lastItem An optional composable function that renders an additional item at the end of the week's content.
 */
@Composable
fun WeekContent(
    weekNumber: Int,
    selectedMonth: Int,
    selectedYear: Int,
    content: @Composable (WeekDay) -> Unit,
    lastItem: @Composable () -> Unit = {},
) {
    // list of all mondays (first day of week) of the selected month
    val mondaysList =
        getMondays(selectedYear, (selectedMonth % 12) + 1)
            .toList()
            .toIntArray()
            .map { i -> i.integerToTwoDigit() }

    val days: List<WeekDay> = getWeekDays(selectedYear, selectedMonth + 1, weekNumber)
//    val days: List<WeekDay> = if (
//        mondaysList.count() != getNumberOfWeeks( selectedMonth, selectedYear )
//        && weekNumber !in 1..4
//    )
//        getWeekDays(selectedYear, selectedMonth + 1, weekNumber - 1)
//    else getWeekDays(selectedYear, selectedMonth + 1, weekNumber)


    println("SELECTED WEEK $weekNumber")


    val emptyScreen = isWeekBeforeCurrentWeek(selectedYear, selectedMonth + 1, weekNumber + 1)

    if (emptyScreen) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(nonePadding, smallPadding)
                .fillMaxSize(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                stringResource(id = R.string.previous_message),
                style = CustomTheme.typography.body1,
                color = CustomTheme.colors.onBackground,
                textAlign = TextAlign.Center
            )
        }
    } else {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .padding(top = mediumPadding)
                .fillMaxHeight()
                .verticalScroll(rememberScrollState())
        ) {
            days.forEach { day ->
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

            lastItem()
        }
    }
}


@Composable
fun WeeksList(selectedMonth: Int, selectedYear: Int, selectedWeek:Int, function: (Int) -> Unit) {

    val monthMap = mapOf(
        0 to stringResource(R.string.january),
        1 to stringResource(R.string.february),
        2 to stringResource(R.string.march),
        3 to stringResource(R.string.april),
        4 to stringResource(R.string.may),
        5 to stringResource(R.string.june),
        6 to stringResource(R.string.july),
        7 to stringResource(R.string.august),
        8 to stringResource(R.string.september),
        9 to stringResource(R.string.october),
        10 to stringResource(R.string.november),
        11 to stringResource(R.string.december)
    )
    // list of all mondays (first day of week) of the selected month
    val mondaysList =
        getMondays(selectedYear, (selectedMonth % 12) + 1)
            .toList()
            .toIntArray()
            .map { i -> i.integerToTwoDigit() }

    // list of all days of the selected week
    var daysList: List<WeekDay>

    var selectedWeekString : String
    // the selected week

    // the problem is that the number of mondays is not the same as the number of weeks
//    if (mondaysList.count() != getNumberOfWeeks(selectedMonth, selectedYear)
//        && selectedWeek !in 1..4
//    ) {
//        selectedWeekString = mondaysList[selectedWeek - 2]
//        daysList = getWeekDays(selectedYear, (selectedMonth % 12) + 1, selectedWeek - 1)
//    } else {
//        selectedWeekString = mondaysList[selectedWeek - 1]
//        daysList = getWeekDays(selectedYear, (selectedMonth % 12) + 1, selectedWeek)
//    }

    selectedWeekString = mondaysList[selectedWeek - 1]
    daysList = getWeekDays(selectedYear, (selectedMonth % 12) + 1, selectedWeek)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp)
            .horizontalScroll(rememberScrollState())
    ) {
        mondaysList.forEach {
            Button(
                onClick = {
                    function(mondaysList.indexOf(it) + 1)
                    selectedWeekString = it
                    // update the list of days of the selected week
                    daysList =
                        getWeekDays(selectedYear, selectedMonth + 1, mondaysList.indexOf(it) + 1)
                },
                modifier = Modifier
                    .padding(smallPadding, smallPadding)
                    .clip(shape = RoundedCornerShape(20)),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = if (selectedWeekString == it) CustomTheme.colors.tertiary else CustomTheme.colors.primary,
                    contentColor = if (selectedWeekString == it) CustomTheme.colors.onTertiary else CustomTheme.colors.onPrimary,
                ),
                border = BorderStroke(
                    width = 1.dp,
                    color = if (selectedWeekString == it) CustomTheme.colors.primary else CustomTheme.colors.tertiary
                ),
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

    Row(
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier.padding(0.dp)
    ) {
        Text(
            text = stringResource(R.string.week),
            style = CustomTheme.typography.h2,
            color = CustomTheme.colors.onBackground
        )
        Text(
            text = "${daysList.first().number} ${monthMap[selectedMonth]} - ${daysList.last().number} ${ if(daysList.first().number>daysList.last().number) monthMap[(selectedMonth + 1)%12] else monthMap[selectedMonth]}",
            style = CustomTheme.typography.h2,
            color = CustomTheme.colors.tertiary
        )
    }
}
