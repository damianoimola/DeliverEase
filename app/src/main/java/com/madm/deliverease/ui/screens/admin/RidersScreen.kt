package com.madm.deliverease.ui.screens.admin

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.madm.deliverease.ui.screens.riders.*
import com.madm.deliverease.ui.theme.Shapes
import com.madm.deliverease.ui.theme.mediumCardElevation
import com.madm.deliverease.ui.theme.nonePadding
import com.madm.deliverease.ui.theme.smallPadding
import com.madm.deliverease.ui.widgets.HireNewRiderDialog
import com.madm.deliverease.ui.widgets.MyPageHeader
import com.madm.deliverease.ui.widgets.Rider
import com.madm.deliverease.ui.widgets.SwipeToRevealRiderList
import java.util.*

val ridersAvailable = listOf("Massimo Buniy", "Damiano Imola", "Alessandro Finocchi", "Martina Lupini")
val ridersIfNeeded = listOf("Napoleone", "Giulio Cesare", "Matteo Giustini", "Topolino", "Falcao")
@Preview
@Composable
fun RidersScreen() {
    var selectedDate = 0
    val currentMonth = Calendar.getInstance().get(Calendar.MONTH)
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    val currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

    val months = (currentMonth..currentMonth + 11).toList().toIntArray()
    var selectedMonth by remember { mutableStateOf(months[0]) }
    var selectedYear by remember { mutableStateOf(currentYear) }



    val riderList = remember {
        mutableStateListOf(
            Rider("Name1", "Surname1"),
            Rider("Name2", "Surname2"),
            Rider("Name3", "Surname3"),
            Rider("Name4", "Surname4"),
            Rider("Name5", "Surname5"),
            Rider("Name6", "Surname6"),
            Rider("Name7", "Surname7"),
            Rider("Name8", "Surname8"),
            Rider("Name9", "Surname9"),
            Rider("Name10", "Surname10"),
            Rider("Name11", "Surname11"),
            Rider("Name12", "Surname12"),
            Rider("Name13", "Surname13"),
            Rider("Name14", "Surname14"),
            Rider("Name15", "Surname15"),
            Rider("Name16", "Surname16"),
        )
    }

    var showCustomDialog by rememberSaveable { mutableStateOf(false) }

    if (showCustomDialog) {
        HireNewRiderDialog { showCustomDialog = !showCustomDialog }
    }





    Column (modifier = Modifier.verticalScroll(rememberScrollState())) {
        MyPageHeader()

        // ########################################################
        // ########################################################
        // DAMIANO AND MASSIMO'S THINGS
        // ########################################################
        // ########################################################
        Card(
            elevation = mediumCardElevation,
            shape = Shapes.medium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = nonePadding, vertical = smallPadding)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Text("List of your riders", style = TextStyle(fontSize = 20.sp))
                SwipeToRevealRiderList(riderList = riderList, 520.dp)
            }
        }

        Row(){
            Button(
                onClick = { showCustomDialog = !showCustomDialog },
                modifier = Modifier
                    .wrapContentSize()
                    .weight(1f)
            ) {
                Text(text = "Show Alert Dialog")
            }
        }


        // ########################################################
        // ########################################################
        // MARTINA'S THINGS
        // ########################################################
        // ########################################################
        MonthSelector(months, selectedMonth, currentYear) { month: Int, isNextYear: Boolean ->
            selectedYear = if (isNextYear)
                currentYear + 1
            else currentYear
            selectedMonth = month
        }
        WeeksListUpdated(selectedMonth, selectedYear) {daySelected: Int -> selectedDate = daySelected }

        /*

        if(currentDay < selectedDate){
            ridersAvailabilities()
        }else{
            ridersPastShift()
        }

         */
        RidersAvailabilities()

    }
}

@Composable
fun RidersAvailabilities(){

    Column{
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(nonePadding, smallPadding)
        ) {
            Text(text = "Availabile")
            Divider(modifier = Modifier
                .fillMaxWidth()
                .width(2.dp)
                .padding(start = smallPadding))
        }
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(20))
                .background(color = Color.LightGray)
                .fillMaxWidth()
                .padding(smallPadding)
        ){
            Column{
                ridersAvailable.forEach{
                    val checkedState = remember { mutableStateOf(false) }

                    Row(verticalAlignment = Alignment.CenterVertically){
                        Checkbox(
                            checked = checkedState.value,
                            onCheckedChange = { checkedState.value = it },
                            modifier = Modifier.padding(start = 4.dp)
                        )
                        Text(
                            text = it,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                }
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(nonePadding, smallPadding)
        ) {
            Text(text = "If needed")
            Divider(modifier = Modifier
                .fillMaxWidth()
                .width(2.dp)
                .padding(start = smallPadding))
        }
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(20))
                .background(color = Color.LightGray)
                .fillMaxWidth()
                .padding(smallPadding)
        ){
            Column{
                ridersIfNeeded.forEach{
                    val checkedState = remember { mutableStateOf(false) }

                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically){
                        Checkbox(
                            checked = checkedState.value,
                            onCheckedChange = { checkedState.value = it },
                            modifier = Modifier.padding(start = 4.dp)
                        )
                        Text(
                            text = it,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                }
            }
        }

        MaxMinShift()

        ButtonDraftAndSubmit()
    }
}


@Composable 
fun MaxMinShift(){
    Card(
        elevation = mediumCardElevation,
        shape = Shapes.medium,
        modifier = Modifier
            .fillMaxSize()
            .padding(nonePadding, smallPadding)
    ) {
        Text("Number of riders")
        Row(horizontalArrangement = Arrangement.spacedBy(32.dp)){
            Column(modifier = Modifier
                .weight(1f)
                .padding(32.dp)) {
                Text(text = "Minumum:")
                AdjustingButtons()
            }
            Column(modifier = Modifier
                .weight(1f)
                .padding(32.dp)) {
                Text(text= "Maximum:")
                AdjustingButtons()
            }
        }
    }
}

@Preview
@Composable
fun AdjustingButtons(){
    var value =  0

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
    Row( horizontalArrangement = Arrangement.spacedBy(32.dp)) {
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


@Composable
fun RidersPastShift(){

}

@Composable
fun WeeksListUpdated(selectedMonth: Int, selectedYear: Int, function: (Int) -> Unit) {
    // list of all mondays (first day of week) of the selected month
    val mondaysList = getMondays(selectedYear, selectedMonth+1)
        .toList()
        .toIntArray()
        .map { i -> i.integerToTwoDigit() }

    // list of all days of the selected week
    var daysList by rememberSaveable { mutableStateOf(getWeekDays(selectedYear, selectedMonth+1, 1)) }

    // the selected week
    var selectedWeek by rememberSaveable { mutableStateOf(mondaysList[0]) }


    Row (
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
    ){
        mondaysList.forEach {
            Button(
                onClick = {
                    // function that updates other widgets bases on the selected week
                    function(it.toInt())
                    selectedWeek = it
                    // update the list of days of the selected week
                    daysList = getWeekDays(selectedYear, selectedMonth+1, mondaysList.indexOf(it) + 1)
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
                    if (selectedWeek == it) Color(0xFFFF9800)
                    else Color(0xFFFF5722)
                ),
                border = BorderStroke(width = 1.dp, color = Color.Red),
                shape = RoundedCornerShape(20)
            ) {
                Text(
                    text = "$it ${MonthMap[selectedMonth]}",
                    color = Color.White
                )
            }

        }
    }

    Row (
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.Start
    ){
        Text(
            text = "Week: ",
            style = TextStyle(
                fontSize = 25.sp,
                fontWeight = FontWeight.Normal
            )
        )
        Text(
            text = "${daysList.first().number} ${MonthMap[selectedMonth]} - ${daysList.last().number} ${ if(daysList.first().number>daysList.last().number) MonthMap[(selectedMonth + 1)%12] else MonthMap[selectedMonth]}",
            style = TextStyle(
                fontSize = 25.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFFFF9800)
            )
        )
    }


}



