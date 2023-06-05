package com.madm.deliverease.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.madm.deliverease.R
import com.madm.deliverease.ui.theme.*
import com.madm.deliverease.ui.widgets.MyPageHeader
import com.madm.deliverease.ui.widgets.PreferencesSetting

@Preview
@Composable
fun SettingScreen() {
    var minRiderPerWeek = remember { mutableStateOf(0) }
    var maxRiderPerWeek = remember { mutableStateOf(0) }
    var minRiderPerDay = remember { mutableStateOf(0) }
    var maxRiderPerDay = remember { mutableStateOf(0) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        MyPageHeader()
        SettingCard {
            SettingItem(stringResource(R.string.num_riders_for_week), false){
                SetRidersAmount(
                    middleText = "Min",
                    amount = minRiderPerWeek.value,
                    onAddBtnClick = {
                        if(minRiderPerWeek.value == maxRiderPerWeek.value) {
                            minRiderPerWeek.value++
                            maxRiderPerWeek.value++
                        } else if(minRiderPerWeek.value < maxRiderPerWeek.value) minRiderPerWeek.value++
                    },
                    onRemoveBtnClick = { if (minRiderPerWeek.value > 0) minRiderPerWeek.value-- }
                )
                SetRidersAmount(
                    middleText = "Max",
                    amount = maxRiderPerWeek.value,
                    onAddBtnClick = { maxRiderPerWeek.value++ },
                    onRemoveBtnClick = {
                        if (maxRiderPerWeek.value == minRiderPerWeek.value && minRiderPerWeek.value != 0) {
                            maxRiderPerWeek.value--
                            minRiderPerWeek.value--
                        } else if (maxRiderPerWeek.value > minRiderPerWeek.value) maxRiderPerWeek.value--
                    }
                )
            }
            SettingItem(stringResource(R.string.num_riders_for_day), false){
                SetRidersAmount(
                    middleText = "Min",
                    amount = minRiderPerDay.value,
                    onAddBtnClick = {
                        if(minRiderPerDay.value == maxRiderPerDay.value) {
                            minRiderPerDay.value++
                            maxRiderPerDay.value++
                        }  else if(minRiderPerDay.value < maxRiderPerDay.value) minRiderPerDay.value++
                    }
                ) { if (minRiderPerDay.value > 0) minRiderPerDay.value-- }
                SetRidersAmount(
                    middleText = "Max",
                    amount = maxRiderPerDay.value,
                    onAddBtnClick = { maxRiderPerDay.value++ }
                ) {
                    if (maxRiderPerDay.value == minRiderPerDay.value && minRiderPerDay.value != 0) {
                        maxRiderPerDay.value--
                        minRiderPerDay.value--
                    } else if (maxRiderPerDay.value > minRiderPerDay.value) maxRiderPerDay.value--
                }
            }
        }
        PreferencesSetting()
    }
}

@Composable
fun SettingCard(content: @Composable () -> Unit){
    Card(
        elevation = mediumCardElevation,
        shape = Shapes.medium,
        modifier = Modifier
            .fillMaxWidth()
            .padding(nonePadding, smallPadding)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
//            .background(
//                color = Color(0xFFFFF3F3), // TODO Ralisin: set theme color
//                shape = RoundedCornerShape(topEndPercent = 5, bottomStartPercent = 5)
//            ),
            ,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            content()
        }
    }

}


@Composable
fun SettingItem(title: String, inline: Boolean, content: @Composable () -> Unit){
    if(inline){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(mediumPadding)
        ) {
            Text(
                text = title,
//                style = TextStyle( // TODO Ralisin: set theme typography
//                    fontFamily = gilroy,
//                    fontSize = 20.sp,
//                    textAlign = TextAlign.Center,
//                    fontWeight = FontWeight.SemiBold
//                )
            )
            content()
        }
    } else {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(mediumPadding)
        ) {
            Text(
                text = title,
//                style = TextStyle( // TODO Ralisin: set theme typography
//                    fontFamily = gilroy,
//                    fontSize = 20.sp,
//                    textAlign = TextAlign.Center,
//                    fontWeight = FontWeight.SemiBold
//                )
            )
            content()
        }
    }
}


@Composable
fun SetRidersAmount(
    middleText: String,
    amount: Int,
    onAddBtnClick: () -> Unit,
    onRemoveBtnClick: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Button(
            onRemoveBtnClick,
        ) {
            Text(text = "-") //, Modifier.size(4.dp)) TODO Ralisin: understand if use typography theme
        }

        Column(
            modifier = Modifier.height(IntrinsicSize.Min),
//            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = middleText)
            Text(text = amount.toString())
        }

        Button(onClick = onAddBtnClick) {
            Text(text = "+") //  TODO Ralisin: understand if use typography theme
        }
    }
}

// TODO Ralisin to @Damiano: can it be removed?
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


// TODO Ralisin: I restructured it, can we delete?
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
            Text(text = "-", Modifier.size(4.dp))
        }

        Column(
            modifier = Modifier.height(IntrinsicSize.Min),
//            verticalArrangement = Arrangement.Top,
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


// TODO Ralisin: I restructured it, can we delete?
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












