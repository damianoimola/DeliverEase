package com.madm.deliverease.ui.screens.riders

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.madm.deliverease.R
import com.madm.deliverease.ui.theme.gilroy
import com.madm.deliverease.ui.theme.mediumPadding
import com.madm.deliverease.ui.theme.smallPadding
import com.madm.deliverease.ui.widgets.MyCommunication
import com.madm.deliverease.ui.widgets.MyPageHeader
import com.madm.deliverease.ui.widgets.MySeparator
import com.madm.deliverease.ui.widgets.MyShiftChangeOffer

@Composable
fun HomeScreen(){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxSize()
    ) {

        MyPageHeader()

        Card(
            elevation = 10.dp,
            modifier = Modifier
                .padding(mediumPadding)
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
                MySeparator("Shift Change Offers")

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.LightGray)
                        .size(200.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    repeat(10) {
                        MyShiftChangeOffer()
                    }
                }
            }
        }

    }
}