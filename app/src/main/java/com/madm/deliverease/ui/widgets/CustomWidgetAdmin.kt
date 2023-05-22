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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.madm.deliverease.R
import com.madm.deliverease.ui.theme.gilroy
import com.madm.deliverease.ui.theme.smallPadding
import kotlin.math.roundToInt

/** Dialog to hire a new rider */
@Composable
fun HireNewRiderDialog(onDismiss: () -> Unit) {
    val context = LocalContext.current
    var riderName by rememberSaveable { mutableStateOf("") }
    var riderSurname by rememberSaveable { mutableStateOf("") }
    var riderUsername by rememberSaveable { mutableStateOf("") }
    var riderPassword by rememberSaveable { mutableStateOf("") }

    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = false
        )
    ) {
        Surface(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight(),
            shape = MaterialTheme.shapes.large
        ) {
            Column(modifier = Modifier.padding(16.dp)) {

                Row (modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Hire new Rider",
                        style = TextStyle(
                            fontFamily = gilroy,
                            fontWeight = FontWeight.Bold,
                            fontSize = 30.sp,
                            textAlign = TextAlign.Center
                        )
                    )
                }

                TextField(
                    value = riderName,
                    onValueChange = { riderName = it },
                    placeholder = { Text(text = "Name") },
                    modifier = Modifier.padding(smallPadding)
                )

                TextField(
                    value = riderSurname,
                    onValueChange = { riderSurname = it },
                    placeholder = { Text(text = "Surname") },
                    modifier = Modifier.padding(smallPadding)
                )

                TextField(
                    value = riderUsername,
                    onValueChange = { riderUsername = it },
                    placeholder = { Text(text = "Username") },
                    modifier = Modifier.padding(smallPadding)
                )

                TextField(
                    value = riderPassword,
                    onValueChange = { riderPassword = it },
                    placeholder = { Text(text = "Password") },
                    modifier = Modifier.padding(smallPadding)
                )

                Row (modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = { onDismiss() },
                        Modifier
                            .weight(.5f)
                            .padding(8.dp)
                    ) {
                        Text(text = "Close")
                    }

                    Button(
                        onClick = { Toast.makeText(context, "CLICKED ON HIRE", Toast.LENGTH_SHORT).show() },
                        Modifier
                            .weight(.5f)
                            .padding(8.dp)
                    ) {
                        Text(text = "Hire")
                    }
                }
            }
        }
    }
}

fun Float.dp(): Float = this * density + 0.5f

val density: Float get() = Resources.getSystem().displayMetrics.density

/** Custom list of swipable object with two buttons below */
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
                    onDelete = {
                        isRevealed = false
                        riderList.remove(rider)
                    },
                    onEdit = { /*TODO*/ },
                )

                DroppableListItemCard(
                    rider = rider,
                    isRevealed = isRevealed,
                    cardHeight = 56.dp,
                    cardOffset = 120f.dp(),
                    onExpand = {isRevealed = true},
                    onCollapse = { isRevealed = false }
                ) {
                    RiderRow(rider, true)
                }
            }
        }
    }
}

@Composable
fun ActionsRow(
    actionIconSize: Dp,
    onDelete: () -> Unit,
    onEdit: () -> Unit,
) {
    Row(
        Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp) /* TODO: Padding*/
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        IconButton(
            modifier = Modifier.size(actionIconSize),
            onClick = onDelete,
            content = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_bin),
                    tint = Color.Gray,
                    contentDescription = "delete action",
                )
            }
        )
        IconButton(
            modifier = Modifier.size(actionIconSize),
            onClick = onEdit,
            content = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_edit),
                    tint = Color.Gray,
                    contentDescription = "edit action",
                )
            },
        )
    }
}

@SuppressLint("UnusedTransitionTargetStateParameter")
@Composable
fun DroppableListItemCard(
    rider: Rider,
    cardHeight: Dp,
    isRevealed: Boolean,
    cardOffset: Float,
    onExpand: () -> Unit,
    onCollapse: () -> Unit,
    minDragAmount: Int = 6,
    colorBackgroundExpanded: Color = Color(0xFFD1A3FF),
    colorBackgroundCollapsed: Color = Color(0xFFBDE7EC),
    animationDuration: Int = 200,
    content: @Composable (Rider) -> Unit,
) {
    val transitionState = remember {
        MutableTransitionState(isRevealed).apply {
            targetState = !isRevealed
        }
    }
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
        targetValueByState = { if (isRevealed) 40.dp else 2.dp }
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp) /* TODO:set padding value */
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
        shape = remember {
            RoundedCornerShape(8.dp)
        },
        elevation = cardElevation,
        content = {
            content(rider)
        }
    )
}