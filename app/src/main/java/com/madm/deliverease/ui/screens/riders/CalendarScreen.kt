package com.madm.deliverease.ui.screens.riders


import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import com.madm.deliverease.R
import com.madm.deliverease.ui.theme.smallPadding
import com.madm.deliverease.ui.widgets.MonthSelector
import com.madm.deliverease.ui.widgets.MyPageHeader
import com.madm.deliverease.ui.widgets.WeekContent
import com.madm.deliverease.ui.widgets.WeeksList
import java.util.*







@Composable
fun CalendarScreen(){
    var indexOfSelectedWeek : Int by remember { mutableStateOf(1) }
    val currentMonth = Calendar.getInstance().get(Calendar.MONTH)
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)

    val months = (currentMonth..currentMonth + 11).toList().map{i-> i%12}.toIntArray()
    var selectedMonth by remember { mutableStateOf(months[0]) }
    var selectedYear by remember { mutableStateOf(currentYear) }


    Column {
        MyPageHeader()
        MonthSelector(months, selectedMonth, currentYear) { month: Int, isNextYear: Boolean ->
            selectedYear = if (isNextYear)
                currentYear + 1
            else currentYear
            selectedMonth = month
        }
        WeeksList(selectedMonth, selectedYear, false) { weekNumber: Int -> indexOfSelectedWeek = weekNumber }
        WeekContent(indexOfSelectedWeek, selectedMonth, selectedYear) { ShiftRow() }
    }
}






@Composable
fun ShiftRow(){
    val currentRandomVal = (0..1).random()
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(20))
            .background(
                color = if (currentRandomVal == 0) Color.LightGray else Color(
                    0xFFFF9800
                )
            )
            .fillMaxWidth()
            .padding(smallPadding)
    ){
        Text(
            text = if (currentRandomVal == 0) stringResource(id = R.string.noShift) else stringResource(id = R.string.YouHaveAShift),
            style = TextStyle(color = Color.White)
        )
    }
}










