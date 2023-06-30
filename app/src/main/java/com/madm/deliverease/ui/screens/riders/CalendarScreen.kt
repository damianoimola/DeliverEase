package com.madm.deliverease.ui.screens.riders

import android.content.Context
import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.madm.common_libs.model.*
import com.madm.deliverease.R
import com.madm.deliverease.globalUser
import com.madm.deliverease.ui.theme.CustomTheme
import com.madm.deliverease.ui.theme.smallPadding
import com.madm.deliverease.ui.widgets.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import java.util.Calendar



@Preview
@Composable
fun CalendarScreen() {
    val configuration = LocalConfiguration.current

    var selectedWeek: Int by remember { mutableStateOf(getCurrentWeekOfMonth()) }
    var currentMonth = Calendar.getInstance()[Calendar.MONTH]
    var currentYear = Calendar.getInstance()[Calendar.YEAR]
    var nextMonth = false
    var nextYear = false

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

    //list of previous change requests done by user
    val messagesManager = MessagesManager(globalUser!!.id!!, LocalContext.current)
    var shiftRequestList: ArrayList<Message> by rememberSaveable { mutableStateOf(arrayListOf()) }
    var workDayList: List<WorkDay> by rememberSaveable { mutableStateOf(mutableListOf()) }
    val calendarManager = CalendarManager(LocalContext.current)

    calendarManager.getDays { days: List<WorkDay> ->
        workDayList = days
    }
    messagesManager.getReceivedMessages { list: List<Message> ->
        shiftRequestList = ArrayList(
            list.filter {
                it.messageType == Message.MessageType.REQUEST
                &&
                it.senderID == globalUser!!.id
            }
        )
    }


    val offeredDay = mutableListOf<WeekDay>()

    for (shift in shiftRequestList) {
        val dayStr = shift.body!!.split("#")[0]
        val offDate = dayStr.split("-")[0]
        val offMonth = dayStr.split("-")[1]
        offeredDay.add(WeekDay(offDate.toInt(), offMonth.toInt(), ""))
    }

    //variable used to change interface when the change shift button is clicked
    val swap = remember { mutableStateOf(false) }

    //variable used to store the date of the day we would like to trade
    var clickedWeekday: String by remember { mutableStateOf("") }

    //variable used to store the date of the day we would like to change
    var previousWeekDay: String by remember { mutableStateOf("") }

    // getting API data
    var shiftList: List<WorkDay> by rememberSaveable { mutableStateOf(listOf()) }


    calendarManager.getDays { list: List<WorkDay> ->
        shiftList = list.filter { it.riders!!.contains(globalUser!!.id) }
    }

    //variable used to show the dialog
    var showCustomDialog by rememberSaveable { mutableStateOf(false) }

    //where the dialog is actually shown
    if (showCustomDialog) ChangeShiftDialog(
        dayOfTheWeek = clickedWeekday,
        previousWeekDay = previousWeekDay,
        selectedMonth,
        selectedYear
    ) { showCustomDialog = false }

    if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {

        Column {
            //month selector
            MonthSelector(months, selectedMonth, currentYear, nextYear) { month: Int, isNextYear: Boolean ->
                selectedYear = if (isNextYear)
                    currentYear + 1
                else currentYear
                selectedMonth = month
                selectedWeek = 1
            }
            //horizontal weekList
            WeeksList(selectedMonth, selectedYear, selectedWeek) { weekNumber: Int ->
                selectedWeek = weekNumber
            }
            //divider
            WeekContent(selectedWeek, selectedMonth, selectedYear, { weekDay ->
                // create a date from selected day
                val selectedDate: LocalDate? = localDateFormat(weekDay, selectedWeek, selectedYear)
                    /*if (weekDay.number < 7 && selectedWeek != 0 && selectedWeek != 1)
                    LocalDate.of(selectedYear, selectedMonth + 2, weekDay.number)
                else if(weekDay.number < 7 && selectedWeek != 0 && selectedWeek != 1 && weekDay.month ==11)
                    LocalDate.of(selectedYear+1, 1, weekDay.number)
                else if(weekDay.month == 11)
                    LocalDate.of(selectedYear, 1, weekDay.number)
                else
                    LocalDate.of(selectedYear, (selectedMonth + 1) % 12, weekDay.number)

                val year

                if()

                LocalDate.of(year, month, weekNumber)*/

                println("SELECTED DATE  "+selectedDate)
                ShiftRow(
                    shiftList.any {
                        // converting API date
                        val inputDateString = it.workDayDate.toString()
                        val inputDateFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH)
                        val outputDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        val date: Date = inputDateFormat.parse(inputDateString)!!
                        val outputDateString: String = outputDateFormat.format(date)

                        outputDateString == selectedDate.toString()
                    }, swap, offeredDay, weekDay, LocalContext.current,
                    {
                        val inputDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
                        val outputDateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.ITALIAN)
                        val date: Date = inputDateFormat.parse(selectedDate.toString())!!
                        val outputDateString: String = outputDateFormat.format(date)
                        clickedWeekday = outputDateString
                    },
                    {
                        val inputDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
                        val outputDateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.ITALIAN)
                        val date: Date = inputDateFormat.parse(selectedDate.toString())!!
                        val outputDateString: String = outputDateFormat.format(date)
                        previousWeekDay = outputDateString
                    }

                ) { showCustomDialog = it }
                swap.value = false
            })
        }
    } else {
        Row(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.width(IntrinsicSize.Min)) {
                //month selector
                MonthSelector(
                    months,
                    selectedMonth,
                    currentYear,
                    nextYear
                ) { month: Int, isNextYear: Boolean ->
                    selectedYear = if (isNextYear)
                        currentYear + 1
                    else currentYear
                    selectedMonth = month
                    selectedWeek = 1
                }
                //horizontal weekList
                WeeksList(
                    selectedMonth,
                    selectedYear,
                    selectedWeek
                ) { weekNumber: Int -> selectedWeek = weekNumber }
            }

            //divider
            WeekContent(selectedWeek, selectedMonth, selectedYear, { weekDay ->
                // create a date from selected day
                val selectedDate: LocalDate? = localDateFormat(weekDay, selectedWeek, selectedYear)

                ShiftRow(
                    shiftList.any {
                        // converting API date
                        val inputDateString = it.workDayDate.toString()
                        val inputDateFormat =
                            SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH)
                        val outputDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        val date: Date = inputDateFormat.parse(inputDateString)!!
                        val outputDateString: String = outputDateFormat.format(date)

                        outputDateString == selectedDate.toString()
                    }, swap, offeredDay, weekDay, LocalContext.current,
                    {
                        val inputDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
                        val outputDateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.ITALIAN)
                        val date: Date = inputDateFormat.parse(selectedDate.toString())!!
                        val outputDateString: String = outputDateFormat.format(date)
                        clickedWeekday = outputDateString
                    },
                    {
                        val inputDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
                        val outputDateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.ITALIAN)
                        val date: Date = inputDateFormat.parse(selectedDate.toString())!!
                        val outputDateString: String = outputDateFormat.format(date)
                        previousWeekDay = outputDateString
                    }
                ) { showCustomDialog = it }
                swap.value = false
            })
        }
    }

}


/**
*haveAShift is used to memorize if the rider has a turn in that day
* swap is passed because it need to be set true when clicking the swap button
* setWeekDay is used to initialize the variable clickedWeekDay declared in the calling function
* setPreviousDay is used to initialize the variable previousWeekday declared in the calling function
* showDialog is used to set true or false the variable showCustomDialog declared in the calling function
 */
@Composable
fun ShiftRow(
    haveAShift: Boolean,
    swap: MutableState<Boolean>,
    shiftList: List<WeekDay>,
    weekDay: WeekDay,
    context: Context,
    setWeekDay: () -> Unit,
    setPreviousWeekday: () -> Unit,
    showDialog: (Boolean) -> Unit
) {

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20))
            .background(
                color = when (Pair(haveAShift, swap.value)) {
                    Pair(false, false) -> Color.LightGray
                    Pair(true, false) -> Color(0xFFFF9800)
                    Pair(false, true) -> Color(0xFFFF9800)
                    else -> Color.LightGray
                }
            )
            .fillMaxWidth()
            .padding(smallPadding)
            .clickable {
                /*
                when I click the row and the row of a day I don't have a turn (!haveAShift) and I have enter the change
                shift mode(swap.value), I set the day I want to trade and call the dialog
                 */
                if (swap.value && !haveAShift) {

                    setWeekDay()
                    showDialog(true)

                }
            }

    ) {
        Text(
            text = when(Pair(haveAShift, swap.value)){
                Pair(false, false) -> stringResource(R.string.noShift)
                Pair(true, false) -> stringResource(R.string.yesShift)
                Pair(false, true) -> stringResource(R.string.swapText)
                else -> stringResource(R.string.haveATurn)
            } ,
            style = CustomTheme.typography.body1,
            modifier = Modifier.align(Alignment.CenterStart)
        )

        if (haveAShift && !swap.value) {

            Icon(
                painter = painterResource(id = R.drawable.swap),
                contentDescription = "shift change",
                modifier = Modifier
                    .padding(start = 140.dp, top = 1.dp)
                    .size(25.dp)
                    .align(Alignment.CenterEnd)
                    .clickable {
                        /*
                        when I click the swap icon i set the current date I want to change and set the swap
                        value in order to change the interface
                         */
                        var found = false
                        for (shift in shiftList) {
                            if (shift.number == weekDay.number && shift.month == weekDay.month) {
                                Toast
                                    .makeText(
                                        context,
                                        "You have already asked to change shift for this day",
                                        Toast.LENGTH_SHORT
                                    )
                                    .show()
                                found = true
                            }
                        }
                        if (!found) {
                            setPreviousWeekday()
                            swap.value = true
                        }

                    },
                tint = Color.Red
            )
        } else if (haveAShift && swap.value) {
            Icon(
                painter = painterResource(id = R.drawable.x_button),
                contentDescription = "back",
                modifier = Modifier
                    .padding(start = 140.dp, top = 1.dp)
                    .size(28.dp)
                    .align(Alignment.CenterEnd)
                    .clickable {
                        /*
                        when I click the back icon I set the swap value false to return to the
                        original screen
                         */
                        swap.value = false
                    },
                tint = Color.Black
            )
        }

    }
}










