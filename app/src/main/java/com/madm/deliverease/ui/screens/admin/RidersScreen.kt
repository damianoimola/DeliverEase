package com.madm.deliverease.ui.screens.admin

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.madm.common_libs.model.*
import com.madm.deliverease.R
import com.madm.deliverease.globalAllUsers
import com.madm.deliverease.ui.theme.mediumCardElevation
import com.madm.deliverease.ui.theme.nonePadding
import com.madm.deliverease.ui.theme.smallPadding
import com.madm.deliverease.ui.widgets.*
import java.util.*

@Preview
@Composable
fun RidersScreen() {
    val copyOfGlobalUsers = globalAllUsers

    val riderList : List<User> by rememberSaveable { mutableStateOf(copyOfGlobalUsers) }

    var showCustomDialog by rememberSaveable { mutableStateOf(false) }

    if (showCustomDialog) HireNewRiderDialog { showCustomDialog = !showCustomDialog }

    Column (
        Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        Card(
            elevation = mediumCardElevation,
            shape = MaterialTheme.shapes.medium,
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
                Text(stringResource(R.string.list_of_your_riders), style = TextStyle(fontSize = 20.sp)) // TODO Ralisin: add theme typography
                SwipeToRevealRiderList(ArrayList(riderList), 520.dp)
            }
        }

        Row {
            Button(
                onClick = { showCustomDialog = !showCustomDialog },
                modifier = Modifier
                    .wrapContentSize()
                    .weight(1f)
            ) {
                Text(text = stringResource(R.string.hire_new_rider)) // TODO Ralisin: add theme typography
            }
        }
    }
}
