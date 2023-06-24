package com.madm.deliverease.ui.screens.admin

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.madm.deliverease.*
import com.madm.deliverease.R
import com.madm.deliverease.ui.theme.*
import com.madm.deliverease.ui.widgets.PreferencesSetting

@Composable
fun SettingScreen(logoutCallback: () -> Unit) {
    val sharedPreferences = LocalContext.current.getSharedPreferences(SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()

    val minRiderPerWeek = remember { mutableStateOf(sharedPreferences.getInt(ADMIN_MIN_WEEK, 0)) }
    val maxRiderPerWeek = remember { mutableStateOf(sharedPreferences.getInt(ADMIN_MAX_WEEK, 0)) }
    val minRiderPerDay = remember { mutableStateOf(sharedPreferences.getInt(ADMIN_MIN_DAY, 0)) }
    val maxRiderPerDay = remember { mutableStateOf(sharedPreferences.getInt(ADMIN_MAX_DAY, 0)) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        ConstraintsCard(minRiderPerWeek, maxRiderPerWeek, minRiderPerDay, maxRiderPerDay, editor)
        PreferencesSetting(logoutCallback)
    }
}

@Composable
private fun ConstraintsCard(
    minRiderPerWeek: MutableState<Int>,
    maxRiderPerWeek: MutableState<Int>,
    minRiderPerDay: MutableState<Int>,
    maxRiderPerDay: MutableState<Int>,
    editor: SharedPreferences.Editor,
) {
    Card(
        elevation = mediumCardElevation,
        shape = Shapes.medium,
        modifier = Modifier
            .fillMaxWidth()
            .padding(nonePadding, smallPadding),
        backgroundColor = CustomTheme.colors.surface,
        contentColor = CustomTheme.colors.onSurface
    ) {
        Column(
            Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ConstraintSection(stringResource(R.string.num_riders_for_week)) {
                ConstraintItem(
                    middleText = "Min",
                    amount = minRiderPerWeek.value,
                    onAddBtnClick = {
                        if (minRiderPerWeek.value == maxRiderPerWeek.value) {
                            minRiderPerWeek.value++
                            maxRiderPerWeek.value++
                            editor.putInt(ADMIN_MIN_WEEK, minRiderPerWeek.value)
                            editor.putInt(ADMIN_MAX_WEEK, maxRiderPerWeek.value)
                            editor.apply()
                        } else if (minRiderPerWeek.value < maxRiderPerWeek.value) {
                            minRiderPerWeek.value++
                            editor.putInt(ADMIN_MIN_WEEK, minRiderPerWeek.value)
                            editor.apply()
                        }
                    },
                    onRemoveBtnClick = {
                        if (minRiderPerWeek.value > 0) {
                            minRiderPerWeek.value--
                            editor.putInt(ADMIN_MIN_WEEK, minRiderPerWeek.value)
                            editor.apply()
                        }
                    }
                )
                ConstraintItem(
                    middleText = "Max",
                    amount = maxRiderPerWeek.value,
                    onAddBtnClick = {
                        maxRiderPerWeek.value++
                        editor.putInt(ADMIN_MAX_WEEK, maxRiderPerWeek.value)
                        editor.apply()
                    },
                    onRemoveBtnClick = {
                        if (maxRiderPerWeek.value == minRiderPerWeek.value && minRiderPerWeek.value != 0) {
                            maxRiderPerWeek.value--
                            minRiderPerWeek.value--
                            editor.putInt(ADMIN_MIN_WEEK, minRiderPerWeek.value)
                            editor.putInt(ADMIN_MAX_WEEK, maxRiderPerWeek.value)
                            editor.apply()
                        } else if (maxRiderPerWeek.value > minRiderPerWeek.value) {
                            maxRiderPerWeek.value--
                            editor.putInt(ADMIN_MAX_WEEK, maxRiderPerWeek.value)
                            editor.apply()
                        }
                    }
                )
            }
            ConstraintSection(stringResource(R.string.num_riders_for_day)) {
                ConstraintItem(
                    middleText = "Min",
                    amount = minRiderPerDay.value,
                    onAddBtnClick = {
                        if (minRiderPerDay.value == maxRiderPerDay.value) {
                            minRiderPerDay.value++
                            maxRiderPerDay.value++
                            editor.putInt(ADMIN_MIN_DAY, minRiderPerDay.value)
                            editor.putInt(ADMIN_MAX_DAY, maxRiderPerDay.value)
                            editor.apply()
                        } else if (minRiderPerDay.value < maxRiderPerDay.value) {
                            minRiderPerDay.value++
                            editor.putInt(ADMIN_MIN_DAY, minRiderPerDay.value)
                            editor.apply()
                        }
                    }
                ) {
                    if (minRiderPerDay.value > 0) {
                        minRiderPerDay.value--
                        editor.putInt(ADMIN_MIN_DAY, minRiderPerDay.value)
                        editor.apply()
                    }
                }
                ConstraintItem(
                    middleText = "Max",
                    amount = maxRiderPerDay.value,
                    onAddBtnClick = {
                        maxRiderPerDay.value++
                        editor.putInt(ADMIN_MAX_DAY, maxRiderPerDay.value)
                        editor.apply()
                    }
                ) {
                    if (maxRiderPerDay.value == minRiderPerDay.value && minRiderPerDay.value != 0) {
                        maxRiderPerDay.value--
                        minRiderPerDay.value--
                        editor.putInt(ADMIN_MIN_DAY, minRiderPerDay.value)
                        editor.putInt(ADMIN_MAX_DAY, maxRiderPerDay.value)
                        editor.apply()
                    } else if (maxRiderPerDay.value > minRiderPerDay.value) {
                        maxRiderPerDay.value--
                        editor.putInt(ADMIN_MAX_DAY, maxRiderPerDay.value)
                        editor.apply()
                    }
                }
            }
        }
    }
}

@Composable
fun ConstraintSection(title: String, content: @Composable () -> Unit){
    Column(
        verticalArrangement = Arrangement.spacedBy(smallPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(mediumPadding)
    ) {
        Text(
            text = title,
            style = CustomTheme.typography.h3
        )
        content()
    }
}


@Composable
fun ConstraintItem(
    middleText: String,
    amount: Int,
    onAddBtnClick: () -> Unit,
    onRemoveBtnClick: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        IconButton(
            onClick = onRemoveBtnClick,
            modifier = Modifier
                .clip(CircleShape)
                .background(CustomTheme.colors.primary)
                .size(40.dp)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.remove),
                contentDescription = stringResource(R.string.remove),
                tint = CustomTheme.colors.onPrimary
            )
        }

        Column(
            modifier = Modifier.height(IntrinsicSize.Min),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = middleText, style = CustomTheme.typography.body1)
            Text(text = amount.toString(), style = CustomTheme.typography.body1)
        }

        IconButton(
            onClick = onAddBtnClick,
            modifier = Modifier
                .clip(CircleShape)
                .background(CustomTheme.colors.primary)
                .size(40.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(R.string.remove),
                tint = CustomTheme.colors.onPrimary
            )
        }
    }
}