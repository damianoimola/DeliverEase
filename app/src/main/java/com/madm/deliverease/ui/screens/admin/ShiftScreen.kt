package com.madm.deliverease.ui.screens.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.madm.deliverease.R
import com.madm.deliverease.ui.theme.nonePadding
import com.madm.deliverease.ui.theme.smallPadding
import com.madm.deliverease.ui.widgets.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

val ridersAvailable = listOf("Massimo Buniy", "Damiano Imola", "Alessandro Finocchi", "Martina Lupini")
val ridersIfNeeded = listOf("Napoleone", "Giulio Cesare", "Matteo Giustini", "Topolino", "Falcao")



fun getNext90Days(): List<LocalDate> {
    val currentDate = LocalDate.now()
    val next90Days: ArrayList<LocalDate> = arrayListOf()

    for (i in 0 until 90) {
        val date = currentDate.plusDays(i.toLong())
        next90Days.add(date)
    }

    return next90Days.toList()
}





@Composable
fun ShiftsScreen() {
    var selectedDay : Int by remember { mutableStateOf(0) }
    var selectedDate = 0
    val currentMonth = Calendar.getInstance().get(Calendar.MONTH)
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)

    val months = (currentMonth..currentMonth + 11).map{i -> i%12}.toList().toIntArray()
    var selectedMonth by remember { mutableStateOf(months[0]) }
    var selectedYear by remember { mutableStateOf(currentYear) }


    Column(
        modifier = Modifier.fillMaxHeight()
    ) {
        MyPageHeader()

        DaysList(selectedDay){ dayNumber -> selectedDay = dayNumber}

        RidersAvailabilities()
    }

}

@Composable
fun DaysList(selectedDay: Int, function: (Int) -> Unit) {
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
                    function(days.indexOf(it) + 1)
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


@Composable
fun RidersAvailabilities(){

    LazyColumn{
        item{Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(nonePadding, smallPadding)
        ) {
            Text(text = stringResource(R.string.available))
            Divider(modifier = Modifier
                .fillMaxWidth()
                .width(2.dp)
                .padding(start = smallPadding))
        }}
        item{Row(
            modifier = Modifier
                .clip(RoundedCornerShape(20))
                .background(color = Color.LightGray)
                .fillMaxWidth()
        ){
            Column(
                modifier = Modifier.padding(smallPadding),
                verticalArrangement = Arrangement.spacedBy(0.dp)
            ){
                ridersAvailable.forEach{
                    val checkedState = remember { mutableStateOf(false) }

                    Row(verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp)
                            .height(20.dp)){
                        Checkbox(
                            checked = checkedState.value,
                            onCheckedChange = { checkedState.value = it },
                            modifier = Modifier.padding(start = 0.dp)
                        )
                        Text(
                            text = it,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                }
            }
        }}
        item{Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(nonePadding, smallPadding)
        ) {
            Text(text = "If needed")
            Divider(modifier = Modifier
                .fillMaxWidth()
                .width(2.dp)
                .padding(start = smallPadding))
        }}
        item{ Row(
            modifier = Modifier
                .clip(RoundedCornerShape(20))
                .background(color = Color.LightGray)
                .fillMaxWidth()
        ){
            Column(modifier = Modifier.padding(smallPadding),
                verticalArrangement = Arrangement.spacedBy(0.dp)){
                ridersIfNeeded.forEach{
                    val checkedState = remember { mutableStateOf(false) }

                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                        .height(20.dp),
                        verticalAlignment = Alignment.CenterVertically){
                        Checkbox(
                            checked = checkedState.value,
                            onCheckedChange = { checkedState.value = it },
                            modifier = Modifier.padding(start = 0.dp)
                        )
                        Text(
                            text = it,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                }
            }
        }}

        item{ButtonDraftAndSubmit()}
    }
}


@Preview
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

@Preview
@Composable
fun ButtonDraftAndSubmit() {
    Row( horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        Button(
            onClick = {},
            shape = RoundedCornerShape(30),
            border = BorderStroke(1.dp, Color.Red),
            modifier = Modifier
                .weight(1f)
                .padding(32.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xBF3604))
        ) {

            Text(text = "Save Draft", color = Color.Red)
        }

        Button(
            onClick = {},
            shape = RoundedCornerShape(30),
            border = BorderStroke(1.dp, Color.Red),
            modifier = Modifier
                .weight(1f)
                .padding(32.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xBF3604))
        ) {

            Text(text = "Submit", color = Color.Red)
        }
    }
}




