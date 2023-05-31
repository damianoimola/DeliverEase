package com.madm.deliverease.ui.widgets

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.madm.deliverease.R
import com.madm.deliverease.ui.theme.gilroy
import com.madm.deliverease.ui.theme.smallPadding

@Composable
fun MySeparator(
    text: String = stringResource(id = R.string.app_name)
){
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically) {
        Divider(
            color = Color(0xFFD8D8D8),
            thickness = 1.dp,
            modifier = Modifier
                .padding(smallPadding)
                .weight(1.0F))
        Text(
            text = text,
            modifier = Modifier.padding(smallPadding),
            fontSize = 20.sp,
            fontFamily = gilroy)
        Divider(
            color = Color(0xFFD8D8D8),
            thickness = 1.dp,
            modifier = Modifier
                .padding(smallPadding)
                .weight(1.0F))

    }
}

