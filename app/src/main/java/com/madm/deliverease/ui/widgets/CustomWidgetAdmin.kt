package com.madm.deliverease.ui.widgets

import android.annotation.SuppressLint
import android.content.res.Resources
import android.widget.Toast
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.madm.common_libs.model.User
import com.madm.deliverease.R
import com.madm.deliverease.ui.theme.*
import kotlin.math.roundToInt

fun Float.dp(): Float = this * density + 0.5f
val density: Float get() = Resources.getSystem().displayMetrics.density

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun SwipeToRevealRiderList(
    riderList: ArrayList<User>,
    maxHeight: Dp,
    deleteButtonClicked: (User) -> Unit = {},
    editButtonClicked: (User) -> Unit = {}
) {
    val context = LocalContext.current

    LazyColumn(
        Modifier
            .padding(smallPadding)
            .heightIn(0.dp, maxHeight)
    ) {
        items(riderList) { rider ->
            Box(Modifier.fillMaxWidth()) {
                var isRevealed by remember { mutableStateOf(false) }

                // Button icons behind rider name
                ActionsRow(
                    actionIconSize = 56.dp, // TODO Ralisin: want we leave constant size here?
                    onDelete = {
                        isRevealed = false
                        rider.unregister(context)
                        deleteButtonClicked(rider)
                    }, // TODO Ralisin: remove rider from Rest API
                    onEdit = {
                        isRevealed = false
                        editButtonClicked(rider)
                        Toast.makeText(context, "CLICKED EDIT", Toast.LENGTH_SHORT).show()
                    } // TODO Ralisin: implement edit rider option
                )

                DroppableListItemCard(
                    rider = rider,
                    isRevealed = isRevealed,
                    minDragAmount = 16,
                    colorBackgroundItem = CustomTheme.colors.tertiary,
                    colorBackgroundItemExpanded = CustomTheme.colors.tertiaryVariant,
                    colorContent = CustomTheme.colors.onTertiary,
                    onExpand = {isRevealed = true},
                    onCollapse = { isRevealed = false }
                ) {
                    RiderRow(rider, true)
                }
            }
        }
    }
}

@SuppressLint("UnusedTransitionTargetStateParameter")
@Composable
fun DroppableListItemCard(
    rider: User,
    isRevealed: Boolean,
    onExpand: () -> Unit,
    onCollapse: () -> Unit,
    cardHeight: Dp = 56.dp,
    cardOffset: Float = 120f.dp(),
    minDragAmount: Int = 550,
    colorBackgroundItem: Color = Color(0xFFBDE7EC),
    colorBackgroundItemExpanded: Color = Color(0xFFD1A3FF),
    colorContent: Color = Color.White,
    animationDuration: Int = 200,
    content: @Composable (User) -> Unit,
) {
    val transitionState = remember { MutableTransitionState(isRevealed).apply { targetState = !isRevealed } }
    val transition = updateTransition(transitionState, "cardTransition")
    val cardBgColor by transition.animateColor(
        label = "cardBgColorTransition",
        transitionSpec = { tween(durationMillis = animationDuration) },
        targetValueByState = {
            if (isRevealed) colorBackgroundItemExpanded else colorBackgroundItem
        }
    )

    val offsetTransition by transition.animateFloat(
        label = "cardOffsetTransition",
        transitionSpec = { tween(durationMillis = animationDuration) },
        targetValueByState = { if (isRevealed) -cardOffset else 0f },
    )

    val cardElevation by transition.animateDp(
        label = "cardElevation",
        transitionSpec = { tween(durationMillis = animationDuration) },
        targetValueByState = { if (isRevealed) overCardElevation else smallCardElevation }
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = nonePadding, vertical = smallPadding)
            .height(cardHeight)
            .offset { IntOffset(offsetTransition.roundToInt(), 0) }
            .pointerInput(Unit) {
                detectHorizontalDragGestures { _, dragAmount ->
                    when {
                        dragAmount > minDragAmount -> onCollapse()
                        dragAmount < -minDragAmount -> onExpand()
                    }
                }
            },
        contentColor = colorContent,
        backgroundColor = cardBgColor,
        shape = CustomTheme.shapes.large,
        elevation = cardElevation,
        content = { content(rider) }
    )
}

@Composable
fun ActionsRow(
    actionIconSize: Dp,
    onDelete: () -> Unit,
    onEdit: () -> Unit,
    iconColor: Color = Color.Gray /* TODO: Use theme */
) {
    Row(
        Modifier
            .padding(horizontal = nonePadding, vertical = smallPadding)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        IconButton(
            modifier = Modifier.size(actionIconSize),
            onClick = onDelete,
            content = {
                Icon(
                    painter = painterResource(R.drawable.ic_bin),
                    tint = iconColor,
                    contentDescription = "delete action",
                )
            }
        )
        IconButton(
            modifier = Modifier.size(actionIconSize),
            onClick = onEdit,
            content = {
                Icon(
                    painter = painterResource(R.drawable.ic_edit),
                    tint = iconColor,
                    contentDescription = "edit action",
                )
            },
        )
    }
}
