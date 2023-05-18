package com.madm.deliverease.ui.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.madm.deliverease.R
import com.madm.deliverease.ui.theme.gilroy
import com.madm.deliverease.ui.theme.mediumPadding
import com.madm.deliverease.ui.theme.smallPadding

@Composable
fun MySeparator(
    text: String = "DeliverEasy"
){
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically) {
        Divider(
            color = Color(0xFFD8D8D8),
            thickness = 1.dp,
            modifier = Modifier
                .padding(smallPadding)
                .weight(1.0F))
        Text(
            text = text,
            modifier = Modifier.padding(smallPadding),
            fontSize = 20.sp,
            fontFamily = gilroy)
        Divider(
            color = Color(0xFFD8D8D8),
            thickness = 1.dp,
            modifier = Modifier
                .padding(smallPadding)
                .weight(1.0F))

    }
}

@Composable
fun MyCommunication(
    noticeText: String = "Notice!",
    publishDate: String = "2023/08/18"
){
    Card(
        elevation = 10.dp,
        modifier = Modifier
            .padding(mediumPadding)) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.fillMaxWidth()){
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
            ){
                Text(
                    text = noticeText,
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Start,
                    fontSize = 20.sp,
                    fontFamily = gilroy)
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.fillMaxWidth()
            ){
                Text(
                    text = publishDate,
                    modifier = Modifier
                        .padding(smallPadding)
                        .fillMaxWidth(),
                    textAlign = TextAlign.End,
                    fontSize = 15.sp,
                    fontFamily = gilroy)
            }
        }
    }
}

@Preview
@Composable
fun MyShiftChangeOffer(
    changerName: String = "Alex Mariuolo",
    offeredDayString: String = "2023/08/18",
    wantedDayString: String = "2023/08/29",
    onAccept: () -> Unit = {},
    onReject: () -> Unit = {}
){
    Card(
        elevation = 10.dp,
        modifier = Modifier
            .padding(mediumPadding)) {
        Row(verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth()) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1.0F)
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
                        text = changerName,
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
                        text = offeredDayString,
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
                        text = wantedDayString,
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
                    .weight(1.0F)
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
                    /*Image(
                        painter = painterResource(id = R.drawable.reject_offer_button_icon),
                        contentDescription = "rej",
                        modifier = Modifier
                            .size(30.dp)
                            .clickable(onClick = onReject),
                        contentScale = ContentScale.FillHeight
                    )*/
                }
            }
        }
    }
}