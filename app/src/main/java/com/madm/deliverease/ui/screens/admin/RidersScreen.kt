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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.madm.common_libs.model.*
import com.madm.deliverease.R
import com.madm.deliverease.globalAllUsers
import com.madm.deliverease.ui.theme.CustomTheme
import com.madm.deliverease.ui.theme.mediumCardElevation
import com.madm.deliverease.ui.theme.nonePadding
import com.madm.deliverease.ui.theme.smallPadding
import com.madm.deliverease.ui.widgets.*
import kotlin.collections.ArrayList

@Preview
@Composable
fun RidersScreen() {
    val copyOfGlobalUsers = globalAllUsers

    var riderList : ArrayList<User> by rememberSaveable { mutableStateOf(ArrayList(copyOfGlobalUsers)) }

    var showHireDialog by rememberSaveable { mutableStateOf(false) }
    var showEditDialog by rememberSaveable { mutableStateOf(false) }
    var selectedRider : User? by rememberSaveable { mutableStateOf(null) }

    if (showHireDialog) HireNewRiderDialog (
        {
            globalAllUsers.add(it)
            riderList = ArrayList(globalAllUsers)
        }
    ) { showHireDialog = !showHireDialog }

    if (showEditDialog && selectedRider != null)
        EditRiderDialog(selectedRider!!,
            { oldUser, newUser ->
                globalAllUsers.remove(oldUser)
                globalAllUsers.add(newUser)
                riderList = ArrayList(globalAllUsers)
            }
        ) { showEditDialog = !showEditDialog }

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
                    deleteButtonClicked = { rider ->
                        globalAllUsers.remove(rider)
                        riderList = ArrayList(globalAllUsers)
                    },
                    editButtonClicked = { rider ->
                        showEditDialog = !showEditDialog
                        selectedRider = rider
                    }
                )
            }
        }

        Row {
            DefaultButton(text = stringResource(R.string.hire_new_rider), modifier = Modifier
                .wrapContentSize()
                .weight(1f)) {
                showHireDialog = !showHireDialog
            }
        }
    }
}
