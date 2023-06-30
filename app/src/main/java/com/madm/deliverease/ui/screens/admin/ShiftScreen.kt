package com.madm.deliverease.ui.screens.admin

import android.content.Context
import android.content.res.Configuration
import android.os.Parcelable
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.madm.common_libs.internal_storage_manager.deleteDraftDays
import com.madm.common_libs.internal_storage_manager.retrieveDraftCalendar
import com.madm.common_libs.internal_storage_manager.saveDraftCalendar
import com.madm.common_libs.model.CalendarManager
import com.madm.common_libs.model.Message
import com.madm.common_libs.model.User
import com.madm.common_libs.model.WorkDay
import com.madm.deliverease.*
import com.madm.deliverease.R
import com.madm.deliverease.ui.theme.*
import com.madm.deliverease.ui.widgets.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import java.time.LocalDate
import java.time.ZoneId
import java.util.*


@Parcelize
class CheckBoxItem(val user: User, val isAllocated: Boolean) : Parcelable {
    @IgnoredOnParcel
    var isChecked: Boolean by mutableStateOf(isAllocated)
}



@Preview
@Composable
fun ShiftsScreen() {
    val configuration = LocalConfiguration.current

    val defaultMessage: String = stringResource(R.string.default_message_send_shift)
    val context = LocalContext.current
    var selectedWeek: Int by remember { mutableStateOf(getCurrentWeekOfMonth()) }
    val currentMonth = Calendar.getInstance()[Calendar.MONTH]
    val currentYear = Calendar.getInstance()[Calendar.YEAR]

    val months = ((currentMonth - 2)..currentMonth + 2).toList().map { i -> Math.floorMod(i, 12) }.toIntArray()
    var selectedMonth by remember { mutableStateOf(months[2]) }
    var selectedYear by remember { mutableStateOf(currentYear) }


    var showDialog by rememberSaveable { mutableStateOf(false) }
    var perWeekConstraint: List<String> by rememberSaveable { mutableStateOf(listOf()) }
    var perDayConstraint: List<String> by rememberSaveable { mutableStateOf(listOf()) }
    var emptyDaysConstraint: List<String> by rememberSaveable { mutableStateOf(listOf()) }


    // retrieving all working days in server
    var workingDays: List<WorkDay> by rememberSaveable { mutableStateOf(listOf()) }
    // newly added working days (still not on server)
    val updatedWorkingDays: ArrayList<WorkDay> by rememberSaveable { mutableStateOf(arrayListOf()) }
    // EVERY working day both server and newly added
    var weekWorkingDays: ArrayList<WorkDay> by rememberSaveable { mutableStateOf(arrayListOf()) }





    // handling drafted working days
    val draftedWorkingDays: List<WorkDay> = retrieveDraftCalendar(context) ?: listOf()
    val thisWeekDays = getWeekDatesInFormat(selectedYear, selectedMonth + 1, selectedWeek + 1)
    val thisWeekDrafted = draftedWorkingDays.any { it.date in thisWeekDays }
    val thisWeekPublicized = workingDays.any { it.date in thisWeekDays }

    draftedWorkingDays.forEach { println("DRAFTED ${it.date} ANY $thisWeekDrafted") }
    var updated by rememberSaveable { mutableStateOf(false) }

//    workingDays = if((!thisWeekPublicized && !thisWeekDrafted) || thisWeekPublicized)
//        workingDays
//    else draftedWorkingDays

//    weekWorkingDays = if((!thisWeekPublicized && !thisWeekDrafted) || thisWeekPublicized)
//        ArrayList(workingDays)
//    else ArrayList(draftedWorkingDays)

//    weekWorkingDays = ArrayList(workingDays)

    // if the selected week has not been publicized
    // and there is a draft about this week, take it
    if(!thisWeekPublicized && thisWeekDrafted && !updated) {
        weekWorkingDays = ArrayList(draftedWorkingDays)
        updatedWorkingDays.addAll(draftedWorkingDays)
        updated = true
    }

    val calendarManager = CalendarManager(context)

    if (!showDialog && weekWorkingDays.isEmpty()) {
        calendarManager.getDays { days ->
            println("API CALL")

            runBlocking { delay(500) }

            workingDays = days
            weekWorkingDays = ArrayList(days)
        }
    } else if (showDialog && weekWorkingDays.isNotEmpty()) {
        val toastMessage = stringResource(R.string.shift_not_changed)
        ConstraintsDialog(
            title = stringResource(R.string.constraints_not_respected),
            perWeekConstraint = perWeekConstraint,
            perDayConstraint = perDayConstraint,
            emptyDaysConstraint = emptyDaysConstraint,
            onContinue = {
                if (updatedWorkingDays.isNotEmpty()) {
                    weekWorkingDays.clear()
                    calendarManager.insertDays(updatedWorkingDays)
                    deleteDraftDays(context, updatedWorkingDays)
                } else
                    Toast.makeText(
                        context,
                        toastMessage,
                        Toast.LENGTH_SHORT
                    ).show()
                showDialog = !showDialog
            },
            onDismiss = { showDialog = !showDialog }
        )
    }


    if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
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

            WeeksList(
                selectedMonth,
                selectedYear,
                selectedWeek
            ) { weekNumber: Int -> selectedWeek = weekNumber }

            WeekContent(selectedWeek, selectedMonth, selectedYear,
                { weekDay ->
                    ShiftItem(
                        weekDay,
                        selectedWeek,
                        selectedYear,
                        selectedMonth,
                        weekWorkingDays,
                        updatedWorkingDays,
                        workingDays
                    )
                }
            ) {
                ButtonDraftAndSubmit(
                    saveDraft = {
                        val cal = com.madm.common_libs.model.Calendar(updatedWorkingDays.toList())
                        saveDraftCalendar(context, cal)
                    },
                    updateServer = {
                        val (perWeekList, perDayList, emptyDaysList) = constraintsChecker(
                            context = context,
                            weekWorkingDays = ArrayList(weekWorkingDays),
                            selectedWeek = selectedWeek,
                            selectedMonth = selectedMonth,
                            selectedYear = selectedYear
                        )

                        perWeekConstraint = perWeekList
                        perDayConstraint = perDayList
                        emptyDaysConstraint = emptyDaysList

                        showDialog = true
                    },
                    notifyRiders = {
                        Message(
                            senderID = globalUser!!.id,
                            receiverID = "0",
                            body = defaultMessage,
                            type = Message.MessageType.NOTIFICATION.displayName
                        ).send(context)
                    }
                )
            }
        }
    } else {
        Row(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.width(IntrinsicSize.Min)) {
                MonthSelector(
                    months,
                    selectedMonth,
                    currentYear
                ) { month: Int, isNextYear: Boolean ->
                    selectedYear = if (isNextYear)
                        currentYear + 1
                    else currentYear
                    selectedMonth = month
                    selectedWeek = 1
                }

                WeeksList(
                    selectedMonth,
                    selectedYear,
                    selectedWeek
                ) { weekNumber: Int -> selectedWeek = weekNumber }
            }

            WeekContent(selectedWeek, selectedMonth, selectedYear,
                { weekDay ->
                    ShiftItem(
                        weekDay,
                        selectedWeek,
                        selectedYear,
                        selectedMonth,
                        weekWorkingDays,
                        updatedWorkingDays,
                        workingDays
                    )
                }
            ) {
                ButtonDraftAndSubmit(
                    saveDraft = {},
                    updateServer = {
                        val (perWeekList, perDayList, emptyDaysList) = constraintsChecker(
                            context = context,
                            weekWorkingDays = ArrayList(weekWorkingDays),
                            selectedWeek = selectedWeek,
                            selectedMonth = selectedMonth,
                            selectedYear = selectedYear
                        )

                        perWeekConstraint = perWeekList
                        perDayConstraint = perDayList
                        emptyDaysConstraint = emptyDaysList

                        showDialog = true
                    },
                    notifyRiders = {
                        Message(
                            senderID = globalUser!!.id,
                            receiverID = "0",
                            body = defaultMessage,
                            type = Message.MessageType.NOTIFICATION.displayName
                        ).send(context)
                    }
                )
            }
        }
    }
}

@Composable
private fun ShiftItem(
    weekDay: WeekDay,
    selectedWeek: Int,
    selectedYear: Int,
    selectedMonth: Int,
    weekWorkingDays: ArrayList<WorkDay>,
    updatedWorkingDays: ArrayList<WorkDay>,
    workingDays: List<WorkDay>
) {
    // retrieve the selected date in a full format
    val selectedDateFormatted =
        if (weekDay.number < 7 && selectedWeek != 0 && selectedWeek != 1)
            Date.from(
                LocalDate.of(
                    selectedYear,
                    (selectedMonth + 2) % 12,
                    weekDay.number
                ).atStartOfDay(ZoneId.systemDefault()).toInstant()
            )
        else
            Date.from(
                LocalDate.of(
                    selectedYear,
                    (selectedMonth + 1) % 12,
                    weekDay.number
                ).atStartOfDay(ZoneId.systemDefault()).toInstant()
            )

    println("WEEK CONTENT - $selectedWeek - $selectedDateFormatted")

    // filter all users that are available
    val availableRidersList: List<User> = globalAllUsers.filter { user ->
        val permanent = user.permanentConstraints.firstOrNull {
            it.dayOfWeek == selectedDateFormatted.toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate().dayOfWeek.value
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

    val ifNeededRidersList: List<User> = globalAllUsers.filter { user ->
        val permanent = user.permanentConstraints.firstOrNull {
            it.dayOfWeek == selectedDateFormatted.toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate().dayOfWeek.value
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

    //to do animation
    var isVisible by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20))
            .background(
                color = CustomTheme.colors.tertiary,
            )
            .fillMaxWidth()
            .padding(smallPadding)
            .clickable {
                isVisible = !isVisible
            },
    ) {
        Text(
            text = stringResource(R.string.click_to_see_shift),
            style = CustomTheme.typography.body1,
            color = CustomTheme.colors.onTertiary,
            modifier = Modifier.align(Alignment.CenterStart),
        )
    }


    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically() + expandVertically(
            // Expand from the top.
            expandFrom = Alignment.Top
        ) + fadeIn(
            // Fade in with the initial alpha of 0.3f.
            initialAlpha = 0.3f
        ),
        exit = shrinkVertically() + fadeOut()
    ) {
        RidersAvailabilities(
            availableRidersList = availableRidersList,
            ifNeededRidersList = ifNeededRidersList,
            selectedDate = selectedDateFormatted,
            workingDays = weekWorkingDays
        ) { riderId, isAllocated ->

            var riderList: ArrayList<String> = arrayListOf()

            var anyWd =
                updatedWorkingDays.any { d -> d.workDayDate == selectedDateFormatted }

            if (anyWd) {
                val wd =
                    updatedWorkingDays.first { d -> d.workDayDate == selectedDateFormatted }
                wd.riders!!.forEach { riderList.add(it) }

                if (!isAllocated) riderList.remove(riderId)
                else riderList.add(riderId)

                wd.riders = riderList.distinct()
            } else {
                val tmp =
                    workingDays.singleOrNull { d -> d.workDayDate == selectedDateFormatted }

                if (tmp != null) tmp.riders!!.forEach { riderList.add(it) }

                if (!isAllocated) riderList.remove(riderId)
                else riderList.add(riderId)

                val wd = WorkDay()
                wd.riders = riderList
                wd.workDayDate = selectedDateFormatted
                updatedWorkingDays.add(wd)
            }



            riderList = arrayListOf()
            anyWd =
                weekWorkingDays.any { d -> d.workDayDate == selectedDateFormatted }
            if (anyWd) {
                val wwd =
                    weekWorkingDays.first { d -> d.workDayDate == selectedDateFormatted }
                wwd.riders!!.forEach { riderList.add(it) }

                if (!isAllocated) riderList.remove(riderId)
                else riderList.add(riderId)

                wwd.riders = riderList.distinct()
            } else {
                val tmp =
                    updatedWorkingDays.singleOrNull { d -> d.workDayDate == selectedDateFormatted }

                if (tmp != null) tmp.riders!!.forEach { riderList.add(it) }

                if (!isAllocated) riderList.remove(riderId)
                else riderList.add(riderId)

                val wd = WorkDay()
                wd.riders = riderList
                wd.workDayDate = selectedDateFormatted
                weekWorkingDays.add(wd)
            }
        }
    }
}





/**
 * @return a Pair object, in first place there is perWeekConstraints, in second place there is perDayConstraints
 */
fun constraintsChecker(
    context: Context,
    weekWorkingDays: ArrayList<WorkDay>,
    selectedWeek: Int,
    selectedMonth: Int,
    selectedYear: Int
): Triple<List<String>, List<String>, List<String>> {
    // open the shared prefs file
    val sharedPreferences =
        context.getSharedPreferences(SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE)

    // TODO: load just one time when composable starts up (Damiano)
    val minWeek = sharedPreferences.getInt(ADMIN_MIN_WEEK, 0)
    val maxWeek = sharedPreferences.getInt(ADMIN_MAX_WEEK, 0)
    val minDay = sharedPreferences.getInt(ADMIN_MIN_DAY, 0)
    val maxDay = sharedPreferences.getInt(ADMIN_MAX_DAY, 0)

    // retrieving first and last day of selected week
    val calendar = Calendar.getInstance()
    calendar[Calendar.YEAR] = selectedYear
    calendar[Calendar.MONTH] = selectedMonth
    calendar[Calendar.WEEK_OF_MONTH] = selectedWeek
    calendar[Calendar.HOUR] = 0

    // Set the start and end dates of the selected week
    calendar[Calendar.DAY_OF_WEEK] = Calendar.MONDAY
    calendar[Calendar.HOUR_OF_DAY] = 0
    calendar[Calendar.MINUTE] = 0
    calendar[Calendar.SECOND] = 0
    calendar[Calendar.MILLISECOND] = 0
    val startDate = calendar.time

    calendar[Calendar.WEEK_OF_MONTH] = selectedWeek + 1
    calendar[Calendar.DAY_OF_WEEK] = Calendar.SUNDAY
    calendar[Calendar.HOUR_OF_DAY] = 23
    calendar[Calendar.MINUTE] = 59
    calendar[Calendar.SECOND] = 59
    calendar[Calendar.MILLISECOND] = 999
    val endDate = calendar.time

    println("DATE ${startDate..endDate}")

    data class WeeklyRider(
        val id: String = "",
        var name: String = "",
        var surname: String = "",
        var workingDays: Int = 0
    )

    val ridersThisWeek: ArrayList<WeeklyRider> = arrayListOf()

    val perWeekList: MutableList<String> = mutableListOf()
    val perDayList: MutableList<String> = mutableListOf()
    val emptyDays: MutableList<String> = mutableListOf()

    weekWorkingDays
        .filter { it.workDayDate in startDate..endDate && it.riders!!.isNotEmpty() }
        .forEach { d ->
            d.riders?.forEach { riderID ->
                val anyRider = ridersThisWeek.any { it.id == riderID }

                if (anyRider) ridersThisWeek.first { it.id == riderID }.workingDays += 1
                else ridersThisWeek.add(
                    WeeklyRider(
                        riderID,
                        globalAllUsers.first { it.id == riderID }.name!!,
                        globalAllUsers.first { it.id == riderID }.surname!!,
                        1
                    )
                )

                println("RIDER $riderID COUNTER ${ridersThisWeek.first { it.id == riderID }.workingDays}")
            }
        }

    ridersThisWeek
        .filter { it.workingDays !in minWeek..maxWeek }
        .forEach { perWeekList += "${it.name} ${it.surname}" }

    weekWorkingDays
        .filter { it.workDayDate in startDate..endDate && it.riders!!.isNotEmpty() && it.riders?.count() !in minDay..maxDay }
        .forEach { perDayList += it.date.toString() }

    weekWorkingDays
        .filter { it.workDayDate in startDate..endDate && it.riders!!.isEmpty() }
        .forEach { emptyDays += it.date.toString() }

    return Triple(perWeekList, perDayList, emptyDays)
}


@Composable
fun RidersAvailabilities(
    availableRidersList: List<User>,
    ifNeededRidersList: List<User>,
    selectedDate: Date,
    workingDays: List<WorkDay>,
    riderSelected: (String, Boolean) -> Unit
) {
    val selectedWorkDay: WorkDay? = workingDays.firstOrNull { it.workDayDate == selectedDate }

    val allocatedRiderList: List<User> = if (selectedWorkDay != null)
        globalAllUsers.filter { user -> user.id in selectedWorkDay.riders!!.toList() }
    else listOf()


    val availableRidersCheckboxList: List<CheckBoxItem> =
        availableRidersList.map { CheckBoxItem(it, it in allocatedRiderList) }

    val ifNeededRidersCheckboxList: List<CheckBoxItem> =
        ifNeededRidersList.map { CheckBoxItem(it, it in allocatedRiderList) }

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
        Text(text, color = CustomTheme.colors.onBackground)
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .width(2.dp)
                .padding(start = smallPadding),
            color = CustomTheme.colors.onBackground
        )
    }
}

@Composable
fun RidersCheckboxCard(
    riderCheckBoxList: List<CheckBoxItem>,
    riderSelected: (String, Boolean) -> Unit
) {
    Card(
        elevation = mediumCardElevation,
        shape = Shapes.medium,
        modifier = Modifier
            .fillMaxWidth()
            .padding(nonePadding, smallPadding),
        backgroundColor = CustomTheme.colors.surface,
        contentColor = CustomTheme.colors.onSurface
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
                        modifier = Modifier.padding(start = 0.dp),
                        colors = CheckboxDefaults.colors(
                            checkedColor = CustomTheme.colors.tertiary,
                            uncheckedColor = CustomTheme.colors.tertiaryVariant,
                            checkmarkColor = CustomTheme.colors.onTertiary
                        )
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
    saveDraft: () -> Unit,
    updateServer: () -> Unit,
    notifyRiders: () -> Unit
) {
    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {

        DefaultButton(
            text = stringResource(R.string.save_draft), modifier = Modifier
                .weight(1f)
                .padding(10.dp)
        ) {
            saveDraft()
        }

        DefaultButton(
            text = stringResource(R.string.submit), modifier = Modifier
                .weight(1f)
                .padding(10.dp)
        ) {
            updateServer()
            notifyRiders()
        }

    }
}



