package com.madm.deliverease.ui.widgets

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.madm.deliverease.R
import com.madm.deliverease.ui.theme.*

data class ShiftRequest(var changerName: String, var offeredDay: String, var wantedDay: String)

@Composable
fun ShiftChangeCard(
    shiftsList: MutableList<ShiftRequest>,
    modifier: Modifier = Modifier,
) {
    Card(
        elevation = mediumCardElevation,
        shape = Shapes.medium,
        modifier = modifier
            .fillMaxSize()
            .padding(nonePadding, smallPadding)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(smallPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //Title
            Text(
                stringResource(R.string.shiftChangeOffers),
                style = TextStyle(fontSize = 22.sp), /* TODO: set to typography */
                modifier = Modifier.padding(nonePadding, nonePadding, nonePadding, smallPadding)
            )

            // List of customShift
            Column(Modifier.verticalScroll(rememberScrollState())) {
                shiftsList.forEach { shift ->
                    Card(Modifier.padding(nonePadding, smallPadding)) {
                        CustomShiftChangeRequest(shift) {
                            shiftsList.remove(shift)
                            println(shiftsList)
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun CustomShiftChangeRequest(
    shiftRequest: ShiftRequest = ShiftRequest("Francesco Virgulini", "dd/mm/yyyy", "dd/mm/yyyy"),
    onAccept: () -> Unit = {}
) {
    Row(
        Modifier
            .clip(Shapes.large)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Text column with name, offered day and wanted day
        Column(
            Modifier
                .padding(smallPadding)
                .weight(1f),horizontalAlignment = Alignment.Start) {
            Text(shiftRequest.changerName,fontSize = 20.sp, fontFamily = gilroy)
            Text("Offered: " + shiftRequest.offeredDay,fontSize = 15.sp, fontFamily = gilroy)
            Text("Wanted: " + shiftRequest.wantedDay,fontSize = 15.sp, fontFamily = gilroy)
        }

        // Icon button accept
        Row(
            Modifier.weight(0.2f).padding(smallPadding, nonePadding),
            Arrangement.End,
            Alignment.CenterVertically
        ){
            IconButton(onClick = { onAccept() }) {
                Icon(ImageVector.vectorResource(id = R.drawable.accept), "accept")
            }
        }
    }
}
