package com.madm.deliverease.ui.screens.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
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
import com.madm.deliverease.ui.widgets.*
import java.util.*

val ridersAvailable = listOf("Massimo Buniy", "Damiano Imola", "Alessandro Finocchi", "Martina Lupini")
val ridersIfNeeded = listOf("Napoleone", "Giulio Cesare", "Matteo Giustini", "Topolino", "Falcao")

@Composable
fun ShiftsScreen() {
    var selectedDate = 0
    val currentMonth = Calendar.getInstance().get(Calendar.MONTH)
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    val currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

    val months = (currentMonth..currentMonth + 11).map{i -> i%12}.toList().toIntArray()
    var selectedMonth by remember { mutableStateOf(months[0]) }
    var selectedYear by remember { mutableStateOf(currentYear) }


    Column {
        MonthSelector(months, selectedMonth, currentYear) { month: Int, isNextYear: Boolean ->
            selectedYear = if (isNextYear)
                currentYear + 1
            else currentYear
            selectedMonth = month
        }
        WeeksList(selectedMonth, selectedYear, false) { daySelected: Int ->
            selectedDate = daySelected
        }

        RidersAvailabilities()
    }

}


@Composable
fun RidersAvailabilities(){

    LazyColumn{
        item{Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(nonePadding, smallPadding)
        ) {
            Text(text = "Availabile")
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
        item{Row(
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




