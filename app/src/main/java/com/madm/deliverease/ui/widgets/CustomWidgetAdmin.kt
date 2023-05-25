package com.madm.deliverease.ui.widgets

import android.annotation.SuppressLint
import android.content.res.Resources
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.madm.deliverease.R
import com.madm.deliverease.ui.theme.*
import kotlin.math.roundToInt

fun Float.dp(): Float = this * density + 0.5f
val density: Float get() = Resources.getSystem().displayMetrics.density

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun SwipeToRevealRiderList(
    riderList: MutableList<Rider>,
    maxHeight: Dp
) {
    LazyColumn(
        Modifier
            .padding(smallPadding)
            .heightIn(0.dp, maxHeight)
    ) {
        items(riderList) { rider ->
            Box(Modifier.fillMaxWidth()) {
                var isRevealed by remember { mutableStateOf(false) }

                ActionsRow(
                    actionIconSize = 56.dp,
                    onDelete = { isRevealed = false; riderList.remove(rider) },
                    onEdit = { /* TODO */ },
                )

                DroppableListItemCard(
                    rider = rider,
                    isRevealed = isRevealed,
                    minDragAmount = 16,
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
    rider: Rider,
    isRevealed: Boolean,
    onExpand: () -> Unit,
    onCollapse: () -> Unit,
    cardHeight: Dp = 56.dp,
    cardOffset: Float = 120f.dp(),
    minDragAmount: Int = 10,
    colorBackgroundExpanded: Color = Color(0xFFD1A3FF), /* TODO: Set color theme */
    colorBackgroundCollapsed: Color = Color(0xFFBDE7EC), /* TODO: Set color theme */
    animationDuration: Int = 200,
    content: @Composable (Rider) -> Unit,
) {
    val transitionState = remember { MutableTransitionState(isRevealed).apply { targetState = !isRevealed } }
    val transition = updateTransition(transitionState, "cardTransition")
    val cardBgColor by transition.animateColor(
        label = "cardBgColorTransition",
        transitionSpec = { tween(durationMillis = animationDuration) },
        targetValueByState = {
            if (isRevealed) colorBackgroundExpanded else colorBackgroundCollapsed
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
                        dragAmount <= minDragAmount -> onExpand()
                        dragAmount > -minDragAmount -> onCollapse()
                    }
                }
            },
        backgroundColor = cardBgColor,
        shape = remember { Shapes.small },
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
