package com.madm.deliverease.ui.screens.admin

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.madm.deliverease.ui.theme.Shapes
import com.madm.deliverease.ui.theme.mediumCardElevation
import com.madm.deliverease.ui.theme.nonePadding
import com.madm.deliverease.ui.theme.smallPadding
import com.madm.deliverease.ui.widgets.*

@Preview
@Composable
fun RidersScreen() {
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
                SwipeToRevealRiderList(riderList, 520.dp)
            }
        }

        Row {
            Button(
                onClick = { showCustomDialog = !showCustomDialog },
                modifier = Modifier
                    .wrapContentSize()
                    .weight(1f)
            ) {
                Text(text = "Show Alert Dialog")
            }
        }
    }
}
