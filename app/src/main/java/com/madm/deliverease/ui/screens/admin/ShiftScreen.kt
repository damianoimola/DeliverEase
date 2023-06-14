package com.madm.deliverease.ui.screens.admin

import android.content.Context
import android.os.Parcelable
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.madm.common_libs.model.*
import com.madm.deliverease.*
import com.madm.deliverease.R
import com.madm.deliverease.ui.theme.Shapes
import com.madm.deliverease.ui.theme.mediumCardElevation
import com.madm.deliverease.ui.theme.nonePadding
import com.madm.deliverease.ui.theme.smallPadding
import com.madm.deliverease.ui.widgets.*
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import java.time.LocalDate
import java.time.ZoneId
import java.util.*
import java.util.Calendar
import kotlin.collections.ArrayList



@Parcelize
class CheckBoxItem(val user: User, val isAllocated : Boolean) : Parcelable{
    @IgnoredOnParcel
    var isChecked: Boolean by mutableStateOf(isAllocated)
}



@Composable
fun ShiftsScreen() {
    println("SHIFT SCREEN")
    val defaultMessage: String = stringResource(R.string.default_message_send_shift)
    val context = LocalContext.current
    var selectedWeek : Int by remember { mutableStateOf(Calendar.getInstance().get(Calendar.WEEK_OF_MONTH) - 1) }
    val currentMonth = Calendar.getInstance().get(Calendar.MONTH)
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)

    val months = (currentMonth..currentMonth + 11).map{i -> i%12}.toList().toIntArray()
    var selectedMonth by remember { mutableStateOf(months[0]) }
    var selectedYear by remember { mutableStateOf(currentYear) }


    var showDialog by rememberSaveable { mutableStateOf(false) }
    var errorMessage : String by rememberSaveable { mutableStateOf("") }


    // retrieving all working days in server
    var workingDays: List<WorkDay> by rememberSaveable { mutableStateOf(listOf()) }
    // newly added working days (still not on server)
    val updatedWorkingDays: ArrayList<WorkDay> by rememberSaveable { mutableStateOf(arrayListOf()) }
    // EVERY working day both server and newly added
    var weekWorkingDays: ArrayList<WorkDay> by rememberSaveable { mutableStateOf(arrayListOf()) }

    val calendarManager : CalendarManager = CalendarManager(context)

    if(!showDialog && weekWorkingDays.isEmpty())
        calendarManager.getDays { days ->
            println("API CALL")
            workingDays = days
            weekWorkingDays = ArrayList(days)
        }
    else if(showDialog && weekWorkingDays.isNotEmpty()){
        WrongConstraintsDialog(
            errorMessage.ifBlank { "Are you sure to continue?" },
            {
                if (updatedWorkingDays.isNotEmpty()) {
                    weekWorkingDays.clear()
                    calendarManager.insertDays(updatedWorkingDays)
                } else
                    Toast.makeText(context, "Shifts are not changed, calendar has not been updated", Toast.LENGTH_SHORT).show()
            }
        ) { showDialog = !showDialog }
    }


    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.Start
    ) {
        MonthSelector(months, selectedMonth, currentYear) { month: Int, isNextYear: Boolean ->
            selectedYear = if (isNextYear)
                currentYear + 1
            else currentYear
            selectedMonth = month
            selectedWeek = 1
        }

        WeeksList(selectedMonth, selectedYear, selectedWeek, false) { weekNumber: Int -> selectedWeek = weekNumber }

        ButtonDraftAndSubmit({
            errorMessage = shiftsConstraintsErrorMessage(context, ArrayList(weekWorkingDays), selectedWeek, selectedMonth, selectedYear)

            showDialog = true
        }) {
            Message(
                senderID = globalUser!!.id,
                receiverID = "0",
                body = defaultMessage,
                type = Message.MessageType.NOTIFICATION.displayName
            ).send(context)
        }


        WeekContent(selectedWeek, selectedMonth, selectedYear) { weekDay ->
            // retrieve the selected date in a full format
            val selectedDateFormatted = if (weekDay.number < 7 && selectedWeek != 0)
                Date.from(LocalDate.of(selectedYear, (selectedMonth+2)%12, weekDay.number).atStartOfDay(ZoneId.systemDefault()).toInstant())
            else
                Date.from(LocalDate.of(selectedYear, (selectedMonth+1)%12, weekDay.number).atStartOfDay(ZoneId.systemDefault()).toInstant())

            println("WEEK CONTENT - $selectedWeek - $selectedDateFormatted")

            // filter all users that are available
            val availableRidersList : List<User> = globalAllUsers.filter { user ->
                val permanent = user.permanentConstraints.firstOrNull {
                    it.dayOfWeek == selectedDateFormatted.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().dayOfWeek.value
                    &&
                    it.type!!.lowercase() != "open"
                } == null

                val nonPermanent = user.nonPermanentConstraints.firstOrNull {
                    it.constraintDate == selectedDateFormatted
                    &&
                    it.type!!.lowercase() != "open"
                } == null

                permanent && nonPermanent && user.id != "0"
            }

            val ifNeededRidersList : List<User> = globalAllUsers.filter { user ->
                val permanent = user.permanentConstraints.firstOrNull {
                    it.dayOfWeek == selectedDateFormatted.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().dayOfWeek.value
                            &&
                            it.type == "light"
                } != null

                val nonPermanent = user.nonPermanentConstraints.firstOrNull {
                    it.constraintDate == selectedDateFormatted
                            &&
                            it.type == "light"
                } != null

                (permanent || nonPermanent) && user.id != "0"
            }


            RidersAvailabilities(
                availableRidersList = availableRidersList,
                ifNeededRidersList = ifNeededRidersList,
                selectedDate = selectedDateFormatted,
                workingDays = weekWorkingDays
            ){ riderId, isAllocated ->

                var riderList: ArrayList<String> = arrayListOf()

                var anyWd = updatedWorkingDays.any{ d -> d.workDayDate == selectedDateFormatted }

                if(anyWd){
                    val wd = updatedWorkingDays.first { d -> d.workDayDate == selectedDateFormatted }
                    wd.riders!!.forEach { riderList.add(it) }

                    if(!isAllocated) riderList.remove(riderId)
                    else riderList.add(riderId)

                    wd.riders = riderList.distinct()
                } else {
                    val tmp = workingDays.singleOrNull { d -> d.workDayDate == selectedDateFormatted }

                    if(tmp != null) tmp.riders!!.forEach{ riderList.add(it) }

                    if(!isAllocated) riderList.remove(riderId)
                    else riderList.add(riderId)

                    val wd = WorkDay()
                    wd.riders = riderList
                    wd.workDayDate = selectedDateFormatted
                    updatedWorkingDays.add(wd)
                }



                riderList = arrayListOf()
                anyWd = weekWorkingDays.any{ d -> d.workDayDate == selectedDateFormatted }
                if(anyWd) {
                    val wwd = weekWorkingDays.first { d -> d.workDayDate == selectedDateFormatted }
                    wwd.riders!!.forEach { riderList.add(it) }

                    if(!isAllocated) riderList.remove(riderId)
                    else riderList.add(riderId)

                    wwd.riders = riderList.distinct()
                } else {
                    val tmp = updatedWorkingDays.singleOrNull { d -> d.workDayDate == selectedDateFormatted }

                    if(tmp != null) tmp.riders!!.forEach{ riderList.add(it) }

                    if(!isAllocated) riderList.remove(riderId)
                    else riderList.add(riderId)

                    val wd = WorkDay()
                    wd.riders = riderList
                    wd.workDayDate = selectedDateFormatted
                    weekWorkingDays.add(wd)
                }
            }
        }
    }
}

fun shiftsConstraintsErrorMessage(
    context: Context,
    weekWorkingDays: ArrayList<WorkDay>,
    selectedWeek: Int,
    selectedMonth: Int,
    selectedYear: Int
): String {
    // open the shared prefs file
    val sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE)

    // TODO: load just one time when composable starts up (Damiano)
    val minWeek = sharedPreferences.getInt(ADMIN_MIN_WEEK, 0)
    val maxWeek = sharedPreferences.getInt(ADMIN_MAX_WEEK, 0)
    val minDay = sharedPreferences.getInt(ADMIN_MIN_DAY, 0)
    val maxDay = sharedPreferences.getInt(ADMIN_MAX_DAY, 0)

    var errorMessage : String = ""


    // retrieving first and last day of selected week
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.YEAR, selectedYear)
    calendar.set(Calendar.MONTH, selectedMonth)
    calendar.set(Calendar.WEEK_OF_MONTH, selectedWeek+1)
    calendar.set(Calendar.HOUR, 0)

    // Set the start and end dates of the selected week
    calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    val startDate = calendar.time

    calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
    calendar.set(Calendar.HOUR_OF_DAY, 23)
    calendar.set(Calendar.MINUTE, 59)
    calendar.set(Calendar.SECOND, 59)
    calendar.set(Calendar.MILLISECOND, 999)
    val endDate = calendar.time


    data class WeeklyRider(val id: String = "", var workingDays: Int = 0)
    val ridersThisWeek : ArrayList<WeeklyRider> = arrayListOf()

    weekWorkingDays
        .filter { it.workDayDate in startDate..endDate && it.riders!!.isNotEmpty() }
        .forEach { d ->
        d.riders?.forEach { riderID ->
            val anyRider = ridersThisWeek.any{ it.id == riderID }

            if(anyRider) ridersThisWeek.first { it.id == riderID }.workingDays += 1
            else ridersThisWeek.add(WeeklyRider(riderID, 1))

            println("RIDER $riderID COUNTER ${ridersThisWeek.first { it.id == riderID }.workingDays}")
        }
    }


    val filter = ridersThisWeek
        .filter { it.workingDays !in minWeek..maxWeek }
        .map { it.id }
        .distinct()

    errorMessage += if(filter.isNotEmpty())
        "• PER-WEEK CONSTRAINTS EXCEEDED FOR USERS ${filter}\n"
    else ""

    weekWorkingDays
        .filter { it.workDayDate in startDate..endDate && it.riders!!.isNotEmpty() }
        .forEach {
        if(it.riders?.count() !in minDay..maxDay){
            errorMessage += "• PER-DAY CONSTRAINTS EXCEEDED FOR THE DAY ${it.date}\n"

            println("############ ${it.riders} - ${it.riders?.count()} - ${minDay..maxDay} - ${it.riders?.count() !in minDay..maxDay}")
        }
    }
    return errorMessage
}


@Composable
fun RidersAvailabilities(
    availableRidersList: List<User>,
    ifNeededRidersList: List<User>,
    selectedDate: Date,
    workingDays: List<WorkDay>,
    riderSelected : (String, Boolean) -> Unit
) {
    val selectedWorkDay : WorkDay? = workingDays.firstOrNull { it.workDayDate == selectedDate }

    val allocatedRiderList : List<User> = if(selectedWorkDay != null)
        globalAllUsers.filter { user -> user.id in selectedWorkDay.riders!!.toList() }
    else listOf()


    val availableRidersCheckboxList : List<CheckBoxItem> = availableRidersList.map { CheckBoxItem(it, it in allocatedRiderList) }

    val ifNeededRidersCheckboxList : List<CheckBoxItem> = ifNeededRidersList.map { CheckBoxItem(it, it in allocatedRiderList) }

    Column {
        // Card with available riders
        if (availableRidersCheckboxList.isNotEmpty()) {
            TextLineSeparator(stringResource(R.string.available))
            RidersCheckboxCard(availableRidersCheckboxList) { u, b -> riderSelected(u, b) }
        }

        // Section with only if_needed riders
        if (ifNeededRidersCheckboxList.isNotEmpty()) {
            TextLineSeparator(stringResource(R.string.if_needed))
            RidersCheckboxCard(ifNeededRidersCheckboxList) { u, b -> riderSelected(u, b) }
        }
    }
}





@Composable
fun TextLineSeparator(text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(nonePadding, smallPadding)
    ) {
        Text(text)
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .width(2.dp)
                .padding(start = smallPadding)
        )
    }
}

@Composable
fun RidersCheckboxCard(
    riderCheckBoxList: List<CheckBoxItem>,
    riderSelected : (String, Boolean) -> Unit
) {
    Card(
        elevation = mediumCardElevation,
        shape = Shapes.medium,
        modifier = Modifier
            .fillMaxWidth()
            .padding(nonePadding, smallPadding)
    ) {
        Column(
            modifier = Modifier.padding(smallPadding),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            riderCheckBoxList.forEach {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                        .height(20.dp)
                        .clickable {
                            it.isChecked = !it.isChecked
                            riderSelected(it.user.id!!, it.isChecked)
                        },
                ) {
                    Checkbox(
                        checked = it.isChecked,
                        onCheckedChange = { isChecked ->
                            it.isChecked = isChecked
                            riderSelected(it.user.id!!, it.isChecked)
                        },
                        modifier = Modifier.padding(start = 0.dp)
                    )
                    Text(
                        text = "${it.user.name} ${it.user.surname}",
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
            }
        }
    }
}


@Composable
fun ButtonDraftAndSubmit(
    updateServer: () -> Unit,
    notifyRiders: () -> Unit
) {
    Row( horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        Button(
            onClick = { updateServer() },
            shape = RoundedCornerShape(30),
            // border = BorderStroke(1.dp, Color.Red), // TODO Ralisin: set theme border
            modifier = Modifier
                .weight(1f)
                .padding(32.dp),
            // colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFBF3604)) // TODO Ralisin: set theme color buttons
        ) {
            Text(text = stringResource(R.string.save_draft) /*color = Color.Red TODO Ralisin: set text color with theme*/)
        }

        Button(
            onClick = {
                updateServer()
                notifyRiders()
            },
            shape = RoundedCornerShape(30), // TODO Ralisin: decide if to leave RoundedCornerShape of choose a default
            // border = BorderStroke(1.dp, Color.Transparent /*Color.Red TODO: set theme color border*/),
            modifier = Modifier
                .weight(1f)
                .padding(32.dp),
            // colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFBF3604)) // TODO Ralisin: set theme color buttons
        ) {
            Text(text = stringResource(R.string.submit) /*color = Color.Red TODO Ralisin: set theme color*/)
        }
    }
}



