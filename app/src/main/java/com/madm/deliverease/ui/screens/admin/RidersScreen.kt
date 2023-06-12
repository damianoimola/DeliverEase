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
import com.madm.deliverease.ui.theme.CustomTheme
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

    var showHireDialog by rememberSaveable { mutableStateOf(false) }
    var showEditDialog by rememberSaveable { mutableStateOf(false) }
    var selectedRider : User? by rememberSaveable { mutableStateOf(null) }

    if (showHireDialog) HireNewRiderDialog { showHireDialog = !showHireDialog }

    if (showEditDialog && selectedRider != null)
        EditRiderDialog(selectedRider!!) { showEditDialog = !showEditDialog }

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
                .padding(horizontal = nonePadding, vertical = smallPadding),
            backgroundColor = CustomTheme.colors.surface
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(nonePadding, smallPadding, nonePadding, nonePadding)
            ) {
                Text(
                    stringResource(R.string.list_of_your_riders),
                    style = CustomTheme.typography.h3,
                    color = CustomTheme.colors.onSurface
                )
                SwipeToRevealRiderList(
                    ArrayList(riderList.filter { it.id != "0" }),
                    520.dp,
                    editButtonClicked = { rider ->
                        showEditDialog = !showEditDialog
                        selectedRider = rider
                    }
                )
            }
        }

        Row {
            Button(
                onClick = { showHireDialog = !showHireDialog },
                modifier = Modifier
                    .wrapContentSize()
                    .weight(1f),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = CustomTheme.colors.primary,
                    contentColor = CustomTheme.colors.onPrimary,
                )
            ) {
                Text(
                    text = stringResource(R.string.hire_new_rider),
                    style = CustomTheme.typography.button
                )
            }
        }
    }
}
