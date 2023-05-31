package com.madm.deliverease.ui.screens.admin

import android.os.Parcelable
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.madm.common_libs.model.CalendarManager
import com.madm.common_libs.model.Message
import com.madm.common_libs.model.WorkDay
import com.madm.common_libs.model.User
import com.madm.deliverease.R
import com.madm.deliverease.globalAllUsers
import com.madm.deliverease.globalUser
import com.madm.deliverease.ui.theme.nonePadding
import com.madm.deliverease.ui.theme.smallPadding
import com.madm.deliverease.ui.widgets.*
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import java.time.LocalDate
import java.time.ZoneId
import java.util.*
import kotlin.collections.ArrayList



fun getNext90Days(): List<LocalDate> {
    val currentDate = LocalDate.now()
    val next90Days: ArrayList<LocalDate> = arrayListOf()

    // from tomorrow
    for (i in 1 until 91) {
        val date = currentDate.plusDays(i.toLong())
        next90Days.add(date)
    }

    return next90Days.toList()
}





@Composable
fun ShiftsScreen() {
    var selectedDay : Int by rememberSaveable { mutableStateOf(0) }
    var selectedDate : Date by rememberSaveable { mutableStateOf(Date.from(LocalDate.now().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant())) }

    var availableRidersList : List<User> by rememberSaveable { mutableStateOf(listOf()) }
    var ifNeededRidersList : List<User> by rememberSaveable { mutableStateOf(listOf()) }


    // filter all users that are available
    availableRidersList = globalAllUsers.filter {  user ->
        val permanent = user.permanentConstraints.firstOrNull {
            it.dayOfWeek == selectedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().dayOfWeek.value
        } == null

        val nonPermanent = user.nonPermanentConstraints.firstOrNull {
            // TODO: "OPEN"
            it.date == selectedDate
        } == null

        permanent && nonPermanent && user.id != "0"
    }


    // filter all users that if needed will come
    ifNeededRidersList = globalAllUsers.filter {  user ->
        val permanent = user.permanentConstraints.firstOrNull {
            it.dayOfWeek == selectedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().dayOfWeek.value
            &&
            it.type == "light"
        } != null

        val nonPermanent = user.nonPermanentConstraints.firstOrNull {
            it.date == selectedDate && it.type == "light"
        } != null

        (permanent || nonPermanent) && user.id != "0"
    }


    Column(
        modifier = Modifier.fillMaxHeight()
    ) {
        MyPageHeader()

        DaysList(selectedDay){ dayNumber, date ->
            selectedDay = dayNumber
            selectedDate = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant())
        }

        RidersAvailabilities(availableRidersList, ifNeededRidersList, selectedDate)
    }

}

@Composable
fun DaysList(selectedDay: Int, function: (Int, LocalDate) -> Unit) {
    val days = getNext90Days()
    var selectedDayString by rememberSaveable { mutableStateOf(days[selectedDay]) }

    LazyRow(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ){
        itemsIndexed(days){ _, it ->
            val month = it.month
            val dayNumber = it.dayOfMonth
            val dayName = it.dayOfWeek

            Button(
                onClick = {
                    function(days.indexOf(it) + 1, it)
                    selectedDayString = it
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
                    if (selectedDayString == it) Color(0xFFFF9800)
                    else Color(0xFFFF5722)
                ),
                border = BorderStroke(width = 1.dp, color = Color.Red),
                shape = RoundedCornerShape(20)
            ) {
                Text(
                    text = "$month\n$dayNumber ${dayName.toString().subSequence(0..2)}",
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}



@Parcelize
class CheckBoxItem(val user: User, val isAllocated : Boolean) : Parcelable{
    @IgnoredOnParcel
    var isChecked: Boolean by mutableStateOf(isAllocated)
}


@Composable
fun RidersAvailabilities(
    availableRidersList: List<User>,
    ifNeededRidersList: List<User>,
    selectedDate: Date
) {
    val context = LocalContext.current
    var selectedWorkDay : WorkDay? by rememberSaveable { mutableStateOf(WorkDay()) }
    var allocatedRiderList : List<User> by rememberSaveable { mutableStateOf(listOf()) }
    var availableRidersCheckboxList : List<CheckBoxItem> by rememberSaveable { mutableStateOf(listOf()) }
    var ifNeededRidersCheckboxList : List<CheckBoxItem> by rememberSaveable { mutableStateOf(listOf()) }
    val calendarManager : CalendarManager = CalendarManager(LocalContext.current)

    calendarManager.getDays{ list: List<WorkDay> ->
        selectedWorkDay = list.firstOrNull { it.date == selectedDate }

        allocatedRiderList = if(selectedWorkDay != null)
            globalAllUsers.filter { user -> user.id in selectedWorkDay!!.riders!!.toList() }
        else
            listOf()

        availableRidersCheckboxList = availableRidersList.map {
            CheckBoxItem(it, it in allocatedRiderList)
        }

        ifNeededRidersCheckboxList = ifNeededRidersList.map {
            CheckBoxItem(it, it in allocatedRiderList)
        }
    }


    LazyColumn{
        if(availableRidersCheckboxList.isNotEmpty()) {
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(nonePadding, smallPadding)
                ) {
                    Text(text = stringResource(R.string.available))
                    Divider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .width(2.dp)
                            .padding(start = smallPadding)
                    )
                }
            }
            item {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20))
                        .background(color = Color.LightGray)
                        .fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(smallPadding),
                        verticalArrangement = Arrangement.spacedBy(0.dp)
                    ) {
                        availableRidersCheckboxList.forEach {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(5.dp)
                                    .height(20.dp)
                                    .clickable { it.isChecked = !it.isChecked },
                            ) {
                                Checkbox(
                                    checked = it.isChecked,
                                    onCheckedChange = { isChecked -> it.isChecked = isChecked },
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
        }

        if(ifNeededRidersCheckboxList.isNotEmpty()) {
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(nonePadding, smallPadding)
                ) {
                    Text(text = "If needed")
                    Divider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .width(2.dp)
                            .padding(start = smallPadding)
                    )
                }
            }
            item {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20))
                        .background(color = Color.LightGray)
                        .fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(smallPadding),
                        verticalArrangement = Arrangement.spacedBy(0.dp)
                    ) {
                        ifNeededRidersCheckboxList.forEach {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(5.dp)
                                    .height(20.dp)
                                    .clickable { it.isChecked = !it.isChecked },
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = it.isChecked,
                                    onCheckedChange = { isChecked -> it.isChecked = isChecked },
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
        }

        item{
            ButtonDraftAndSubmit({
                val listOfWorkingRiders =
                    availableRidersCheckboxList.filter { it.isChecked }.map{ it.user.id!! } +
                    ifNeededRidersCheckboxList.filter { it.isChecked }.map{ it.user.id!! }

                val workDay : WorkDay = WorkDay(selectedDate, listOfWorkingRiders)
                workDay.insertOrUpdate(context)
            }) {
                Message(
                    globalUser!!.id,
                    "0",
                    "Hi, a new calendar has been published",
                    Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                    Message.MessageType.NOTIFICATION.displayName
                ).send(context)
            }
        }
    }
}


@Composable
fun AdjustingButtons(){
    val value =  0

    Row{
        Button(onClick = {
            /*TODO*/
        },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.LightGray),
            shape= RectangleShape,
        ) {
            Text(text = "-")
        }
        Text(text = value.toString())
        Button(onClick = {
            /*TODO*/
        },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.LightGray),
            shape= RectangleShape,
        ) {
            Text(text = "+")
        }
    }
}


@Composable
fun ButtonDraftAndSubmit(updateServer: () -> Unit, notifyRiders: () -> Unit) {
    Row( horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        Button(
            onClick = {
                updateServer()
            },
            shape = RoundedCornerShape(30),
            border = BorderStroke(1.dp, Color.Red),
            modifier = Modifier
                .weight(1f)
                .padding(32.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFBF3604))
        ) {

            Text(text = "Save Draft", color = Color.Red)
        }

        Button(
            onClick = {
                updateServer()
                notifyRiders()
            },
            shape = RoundedCornerShape(30),
            border = BorderStroke(1.dp, Color.Red),
            modifier = Modifier
                .weight(1f)
                .padding(32.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFBF3604))
        ) {

            Text(text = "Submit", color = Color.Red)
        }
    }
}




