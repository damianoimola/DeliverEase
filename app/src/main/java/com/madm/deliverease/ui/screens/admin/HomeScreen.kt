package com.madm.deliverease.ui.screens.admin

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import com.madm.deliverease.ui.widgets.NewsCard
import com.madm.deliverease.ui.widgets.RidersCard

@Preview
@Composable
fun HomeScreen() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        /* TODO: Add top bar with app_icon and app_name */
        RidersCard()
        NewsCard()
    }
}