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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.madm.common_libs.model.*
import com.madm.deliverease.R
import com.madm.deliverease.globalAllUsers
import com.madm.deliverease.globalUser
import com.madm.deliverease.ui.theme.*

@Composable
fun ShiftChangeCard(
    shiftsList: List<Message>,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    Card(
        elevation = mediumCardElevation,
        shape = Shapes.medium,
        modifier = modifier
            .fillMaxSize()
            .padding(nonePadding, smallPadding),
        backgroundColor = CustomTheme.colors.surface
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
                style = CustomTheme.typography.h3,
                textAlign = TextAlign.Center,
                color = CustomTheme.colors.onSurface
            )

            // List of customShift
            Column(Modifier.verticalScroll(rememberScrollState())) {
                shiftsList.forEach { shift ->
                    Card(
                        Modifier.padding(nonePadding, smallPadding),
                        backgroundColor = CustomTheme.colors.surface,
                        contentColor = CustomTheme.colors.onSurface,
                        elevation = extraSmallCardElevation
                    ) {
                        CustomShiftChangeRequest(shift) {
                            Message(
                                senderID = globalUser!!.id,
                                receiverID = shift.senderID,
                                body = shift.id,
                                type = Message.MessageType.ACCEPTANCE.displayName
                            ).send(context)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CustomShiftChangeRequest(
    shiftRequest: Message,
    onAccept: () -> Unit = {}
) {
    val (offeredDay, wantedDay) = shiftRequest.body!!.split("#")

    val requestingUserFullName = with(
        globalAllUsers.first { user -> user.id == shiftRequest.senderID!! }
    ){ "$name $surname" }


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
            Text(requestingUserFullName, style = CustomTheme.typography.h5)
            Text(stringResource(R.string.offered) + offeredDay, style = CustomTheme.typography.body2)
            Text(stringResource(R.string.wanted) + wantedDay, style = CustomTheme.typography.body2)
        }

        // Icon button accept
        Row(
            Modifier.weight(0.2f).padding(smallPadding, nonePadding),
            Arrangement.End,
            Alignment.CenterVertically
        ){
            IconButton(onClick = { onAccept() }) {
                Icon(
                    ImageVector.vectorResource(id = R.drawable.accept),
                    contentDescription = "accept",
                    tint = CustomTheme.colors.primary
                )
            }
        }
    }
}
