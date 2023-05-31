package com.madm.deliverease.ui.screens.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.madm.deliverease.R
import com.madm.deliverease.ui.theme.nonePadding
import com.madm.deliverease.ui.theme.smallPadding
import com.madm.deliverease.ui.widgets.*
import java.util.*

val ridersAvailable = listOf("Massimo Buniy", "Damiano Imola", "Alessandro Finocchi", "Martina Lupini")
val ridersIfNeeded = listOf("Napoleone", "Giulio Cesare", "Matteo Giustini", "Topolino", "Falcao")

@Preview
@Composable
fun ShiftsScreen() {
    var selectedDate = 0
    val currentMonth = Calendar.getInstance().get(Calendar.MONTH)
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)

    val months = (currentMonth..currentMonth + 11).map{i -> i%12}.toList().toIntArray()
    var selectedMonth by remember { mutableStateOf(months[0]) }
    var selectedYear by remember { mutableStateOf(currentYear) }


    Column {
        MyPageHeader()
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
            Text(text = stringResource(id = R.string.available))
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
            Text(text = stringResource(id = R.string.If_needed))
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

            Text(text = stringResource(id = R.string.saveDraft), color = Color.Red)
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

            Text(text = stringResource(id = R.string.submit), color = Color.Red)
        }
    }
}




