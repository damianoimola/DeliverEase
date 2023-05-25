package com.madm.deliverease.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.madm.deliverease.R
import com.madm.deliverease.ui.theme.gilroy
import com.madm.deliverease.ui.theme.mediumPadding

@Composable
fun SettingScreen() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
    ) {
        SettingSection(stringResource(R.string.settings_constraints))
        SettingCard(){
            SettingItem("Number of rider per week", false){ RidersPerWeekConstraint() }
            SettingItem("Number of rider per Day", false){ RidersPerDayConstraint() }
        }


        SettingSection("Ciccio pasticcio")
        SettingCard(){
            SettingItem("Number of rider per week", false){ RidersPerWeekConstraint() }
            SettingItem("Number of rider per Day", false){ RidersPerDayConstraint() }
        }
    }
}




@Composable
fun SettingSection(sectionName : String){
    Text(
        sectionName,
        style = TextStyle(
            fontFamily = gilroy,
            fontSize = 30.sp,
            textAlign = TextAlign.Start,
            fontWeight = FontWeight.Bold
        )
    )
}


@Composable
fun SettingCard(content: @Composable () -> Unit){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color(0xFFFFF3F3),
                shape = RoundedCornerShape(topEndPercent = 5, bottomStartPercent = 5)
            )
    ) {
        content()
    }
}


@Composable
fun SettingItem(itemName: String, inline: Boolean, content: @Composable () -> Unit){
    if(inline){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(mediumPadding)
        ) {
            Text(
                text = itemName,
                style = TextStyle(
                    fontFamily = gilroy,
                    fontSize = 25.sp,
                    textAlign = TextAlign.Start,
                    fontWeight = FontWeight.SemiBold
                )
            )
            content()
        }
    } else {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(mediumPadding)
        ) {
            Text(
                text = itemName,
                style = TextStyle(
                    fontFamily = gilroy,
                    fontSize = 25.sp,
                    textAlign = TextAlign.Start,
                    fontWeight = FontWeight.SemiBold
                )
            )
            content()
        }
    }
}



@Composable
fun RidersPerWeekConstraint(){
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Button(
            onClick = { /*TODO*/ },
        ) {
            Text(text = "-")
        }

        Column(
            modifier = Modifier.height(IntrinsicSize.Min),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Min")
            Text(text = "XX")
        }

        Button(onClick = { /*TODO*/ }) {
            Text(text = "+")
        }
    }


    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Button(
            onClick = { /*TODO*/ },
        ) {
            Text(text = "-")
        }

        Column(
            modifier = Modifier.height(IntrinsicSize.Min),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Max")
            Text(text = "XX")
        }

        Button(onClick = { /*TODO*/ }) {
            Text(text = "+")
        }
    }
}



@Composable
fun RidersPerDayConstraint(){
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Button(
            onClick = { /*TODO*/ },
        ) {
            Text(text = "-")
        }

        Column(
            modifier = Modifier.height(IntrinsicSize.Min),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Min")
            Text(text = "XX")
        }

        Button(onClick = { /*TODO*/ }) {
            Text(text = "+")
        }
    }


    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Button(
            onClick = { /*TODO*/ },
        ) {
            Text(text = "-")
        }

        Column(
            modifier = Modifier.height(IntrinsicSize.Min),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Max")
            Text(text = "XX")
        }

        Button(onClick = { /*TODO*/ }) {
            Text(text = "+")
        }
    }
}












