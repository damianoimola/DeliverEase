package com.madm.deliverease.ui.screens.riders

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.madm.deliverease.R
import com.madm.deliverease.ui.theme.*
import com.madm.deliverease.ui.widgets.*

data class ShiftRequest(var changerName: String, var offeredDay: String, var wantedDay: String)

@Composable
fun HomeScreen() {
    val communicationList = listOf(
        Communication("News 1! I have published calendar 1", "21/05/2023"),
        Communication("News 2! I have published calendar 2", "20/05/2023"),
        Communication("News 3! I have published calendar 3", "19/05/2023"),
        Communication("News 4! I have published calendar 4", "18/05/2023"),
        Communication("News 5! I have published calendar 5", "17/05/2023"),
        Communication("News 6! I have published calendar 6", "16/05/2023"),
        Communication("News 7! I have published calendar 7", "15/05/2023"),
        Communication("News 8! I have published calendar 8", "14/05/2023"),
        Communication("News 9! I have published calendar 9", "13/05/2023"),
    )

    val shiftRequestList = listOf(
        ShiftRequest("Mario Rossi", "23/12/2023", "22/12/2023"),
        ShiftRequest("Merlin Mango", "23/12/2023", "22/12/2023"),
        ShiftRequest("Giulia Schiff", "23/12/2023", "22/12/2023"),
        ShiftRequest("Nome Cognomen", "23/12/2023", "22/12/2023"),
        ShiftRequest("Azzo Corri", "23/12/2023", "22/12/2023"),
        ShiftRequest("Paracetamol Palazzo", "23/12/2023", "22/12/2023"),
        ShiftRequest("Francesco Virgolino", "23/12/2023", "22/12/2023"),
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        MyPageHeader()

        CommunicationCard(communicationList, false, Modifier.weight(1f))

        /* Communication
        Card(
            elevation = 10.dp,
            modifier = Modifier
                .padding(smallPadding)
        ){


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.LightGray)
            ) {
                MySeparator("Communications")

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.LightGray)
                        .size(200.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    repeat(10) {
                        MyCommunication()
                    }
                }
            }
        }
        */

        /* Shift Change Offers
        Column(Modifier.weight(1f)) {
            Card(
                elevation = 10.dp,
                modifier = Modifier
                    .padding(smallPadding)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.LightGray)
                ) {
                    MySeparator("Shift Change Offers")

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.LightGray)
                            .size(200.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        repeat(10) {
                            ShiftChangeOffer()
                        }
                    }
                }
            }
        } */

        ShiftChangeCard(shiftRequestList, Modifier.weight(1f))
    }
}

@Composable
fun ShiftChangeCard(
    shiftList: List<ShiftRequest>,
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
                .background(Color.LightGray)
                .padding(smallPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //Title
            Text(
                stringResource(R.string.shiftChangeOffers),
                style = TextStyle(fontSize = 22.sp), /* TODO: set to typography */
            )

            // List of shifts
            Column(Modifier.verticalScroll(rememberScrollState())) {
                shiftList.forEach { shift ->
                    CustomShiftChangeRequest(shift)
                }
            }
        }
    }
}

@Preview
@Composable
fun CustomShiftChangeRequest(
    shiftRequest: ShiftRequest = ShiftRequest("Alex Mariuolo", "dd/mm/yyyy", "dd/mm/yyyy"),
    onAccept: () -> Unit = {}
) {
    Row(
        Modifier
            .fillMaxWidth()
            .clip(Shapes.small),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.padding(smallPadding).weight(1f),horizontalAlignment = Alignment.Start) {
            Text(shiftRequest.changerName,fontSize = 20.sp, fontFamily = gilroy)
            Text(shiftRequest.offeredDay,fontSize = 15.sp, fontFamily = gilroy)
            Text(shiftRequest.wantedDay,fontSize = 15.sp, fontFamily = gilroy)
        }
        Row(Modifier.weight(1f).padding(smallPadding, nonePadding), horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.CenterVertically){
            IconButton(onClick = { onAccept }) {
                Icon(imageVector = ImageVector.vectorResource(id = R.drawable.accept), contentDescription = "accept")
            }
        }
        /*
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                //.fillMaxWidth()
                //.weight(1.0f)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = smallPadding,
                        top = smallPadding,
                        end = smallPadding,
                        bottom = smallPadding
                    )
            ) {
                Text(
                    text = shiftRequest.changerName,
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Start,
                    fontSize = 15.sp,
                    fontFamily = gilroy
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = smallPadding,
                        top = smallPadding,
                        end = smallPadding,
                        bottom = 0.dp
                    )
            ) {
                Text(
                    text = shiftRequest.offeredDay,
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Start,
                    fontSize = 10.sp,
                    fontFamily = gilroy
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = smallPadding,
                        top = smallPadding,
                        end = smallPadding,
                        bottom = smallPadding
                    )
            ) {
                Text(
                    text = shiftRequest.wantedDay,
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Start,
                    fontSize = 10.sp,
                    fontFamily = gilroy
                )
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.0f)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = smallPadding,
                        end = smallPadding,
                    )
            ) {
                Image(
                    painter = painterResource(id = R.drawable.accept_offer_button_icon),
                    contentDescription = "acc",
                    modifier = Modifier
                        .size(30.dp)
                        .clickable(onClick = onAccept),
                    contentScale = ContentScale.FillHeight
                )
            }
        }

         */
    }

}



