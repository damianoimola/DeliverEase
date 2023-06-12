package com.madm.deliverease.ui.screens.admin

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.madm.deliverease.*
import com.madm.deliverease.R
import com.madm.deliverease.ui.theme.*
import com.madm.deliverease.ui.widgets.PreferencesSetting

@SuppressLint("CommitPrefEdits")
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
                SettingItem(stringResource(R.string.num_riders_for_week), false) {
                    SetRidersAmount(
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
                    SetRidersAmount(
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
                SettingItem(stringResource(R.string.num_riders_for_day), false) {
                    SetRidersAmount(
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
                    SetRidersAmount(
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
        PreferencesSetting(logoutCallback)
    }
}

@Composable
fun SettingItem(title: String, inline: Boolean, content: @Composable () -> Unit){
    if(inline){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
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
    } else {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(mediumPadding)
        ) {
            Text(
                text = title,
                style = CustomTheme.typography.h4
            )
            content()
        }
    }
}


@Composable
fun SetRidersAmount(
    middleText: String,
    amount: Int,
    onAddBtnClick: () -> Unit,
    onRemoveBtnClick: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Button(
            onRemoveBtnClick,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = CustomTheme.colors.primary,
                contentColor = CustomTheme.colors.onPrimary,
            )
        ) {
            Text(text = "-", style = CustomTheme.typography.body1)
        }

        Column(
            modifier = Modifier.height(IntrinsicSize.Min),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = middleText, style = CustomTheme.typography.body1)
            Text(text = amount.toString(), style = CustomTheme.typography.body1)
        }

        Button(
            onAddBtnClick,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = CustomTheme.colors.primary,
                contentColor = CustomTheme.colors.onPrimary,
            )
        ) {
            Text(text = "+", style = CustomTheme.typography.body1)
        }
    }
}