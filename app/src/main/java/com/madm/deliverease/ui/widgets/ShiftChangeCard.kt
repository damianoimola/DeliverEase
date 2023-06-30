package com.madm.deliverease.ui.widgets

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.madm.common_libs.model.Message
import com.madm.deliverease.R
import com.madm.deliverease.globalAllUsers
import com.madm.deliverease.globalUser
import com.madm.deliverease.ui.theme.*

/**
 * Card containing the requests of shift changes
 * @param shiftsList list of shift changes filtered for the user
 * @param modifier card base modifier
 * @param updateList lambda to remove the shift change from the list
 * @param isPortrait checks the orientation of the device
 * @param isLoading manages the placeholder when data isn't still loaded
 */
@Composable
fun ShiftChangeCard(
    shiftsList: List<Message>,
    modifier: Modifier = Modifier,
    updateList: (Message) -> Unit,
    isPortrait: Int,
    isLoading: Boolean = false,
) {
    val context = LocalContext.current

    Card(
        elevation = mediumCardElevation,
        shape = Shapes.medium,
        modifier = modifier
            .fillMaxSize()
            .padding(smallPadding * (1 - isPortrait), smallPadding * isPortrait),
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

            if (shiftsList.isEmpty() && !isLoading) Text(
                stringResource(R.string.no_shift_change_requests),
                style = CustomTheme.typography.body1,
                color = CustomTheme.colors.onSurface
            )

            LazyColumn(
                content = {
                    items(if(isLoading) listOf(1,2,3,4) else shiftsList) {shift ->
                        val isVisible = remember { mutableStateOf(true) }
                        Card(
                            Modifier.padding(nonePadding, smallPadding),
                            backgroundColor = CustomTheme.colors.surface,
                            contentColor = CustomTheme.colors.onSurface,
                            elevation = extraSmallCardElevation
                        ) {
                            if (isVisible.value) {
                                if(isLoading)
                                    ShimmerShiftChangeRequest()
                                else {
                                    ShiftChangeRequest(shift as Message) {
                                        Message(
                                            senderID = globalUser!!.id,
                                            receiverID = shift.senderID,
                                            body = shift.id,
                                            type = Message.MessageType.ACCEPTANCE.displayName
                                        ).send(context) { if (it) updateList(shift) }
                                        isVisible.value = false
                                    }
                                }
                            }
                        }
                    }
                }
            )
        }
    }
}

/**
 * Element of the shift change card in the rider home page
 * @param shiftRequest message of shift request
 * @param onAccept lambda to run when shift change is accepted
 */
@Composable
fun ShiftChangeRequest(
    shiftRequest: Message,
    onAccept: () -> Unit = {},
) {
    val (offeredDay, wantedDay) = shiftRequest.body!!.split("#")

    val requestingUserFullName = with(
        globalAllUsers.first { user -> user.id == shiftRequest.senderID!! }
    ) { "$name $surname" }

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
                .weight(1f), horizontalAlignment = Alignment.Start
        ) {
            Text(requestingUserFullName, style = CustomTheme.typography.h5)
            Text(
                stringResource(R.string.offered) + offeredDay,
                style = CustomTheme.typography.body2
            )
            Text(stringResource(R.string.wanted) + wantedDay, style = CustomTheme.typography.body2)
        }

        // Icon button accept
        Row(
            Modifier
                .weight(0.2f)
                .padding(smallPadding, nonePadding),
            Arrangement.End,
            Alignment.CenterVertically
        ) {
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

/**
 * Placeholder effect when data is still not loaded
 */
@Composable
fun ShimmerShiftChangeRequest() {
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
                .weight(1f), horizontalAlignment = Alignment.Start
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(14.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .shimmerEffect()
            )
            Divider(Modifier.width(2.dp), Color.Transparent)
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(12.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .shimmerEffect()
            )
            Divider(Modifier.width(2.dp), Color.Transparent)
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(12.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .shimmerEffect()
            )
        }

        // Icon button accept
        Row(
            Modifier
                .weight(0.2f)
                .padding(smallPadding, nonePadding),
            Arrangement.End,
            Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .height(20.dp)
                    .width(20.dp)
                    .clip(CircleShape)
                    .shimmerEffect()
            )
        }
    }
}
