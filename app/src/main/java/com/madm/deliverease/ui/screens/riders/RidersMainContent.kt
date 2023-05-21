package com.madm.deliverease.ui.screens.riders

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.madm.deliverease.R
import com.madm.deliverease.ui.theme.mediumPadding
import com.madm.deliverease.ui.widgets.CustomBottomAppBar
import com.madm.deliverease.ui.widgets.CustomNavItem

@Composable
fun RidersMainContent(){
    // manages the navigation between different destinations
    val navController = rememberNavController()

    // bottom navigation bar icons
    val navItems = listOf(
        CustomNavItem("Home", Icons.Default.Home) { navController.navigate("home") },
        CustomNavItem("Calendar", Icons.Default.DateRange) { navController.navigate("calendar") },
        CustomNavItem("Set shift", ImageVector.vectorResource(id = R.drawable.preference_icon)) { navController.navigate("preferences") }
    )

    // Set as navItems[0] cause it works with address, so at first launch of app home button wasn't set as default
    var selectedItem by remember { mutableStateOf(navItems[0]) }

    Scaffold(
        content = {
            Box(modifier = Modifier
                .padding(it)
                .background(Color(0xffffffff))
            ){
                // navigation host holds all of the navigation destinations within the app
                // calling "navController.navigate("home")" you can travel through app
                // It can handle parameters.
                NavHost(
                    navController = navController,
                    startDestination = "home",
                    modifier = Modifier.padding(mediumPadding)
                ) {
                    composable("home") { HomeScreen() }
                    composable("calendar") { CalendarScreen() }
                    composable("preferences") { PreferenceScreen()}
                }
            }
        },
        bottomBar = {
            CustomBottomAppBar(
                navItems = navItems,
                selectedItem = selectedItem,
                onItemSelected = { item -> selectedItem = item },
                modifier = Modifier.fillMaxWidth()
            )
        }
    )
}