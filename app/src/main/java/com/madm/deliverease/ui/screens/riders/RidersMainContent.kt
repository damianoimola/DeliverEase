package com.madm.deliverease.ui.screens.riders

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.madm.deliverease.R
import com.madm.deliverease.ui.theme.mediumPadding
import com.madm.deliverease.ui.widgets.CustomBottomAppBar
import com.madm.deliverease.ui.widgets.CustomNavItem

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun RidersMainContent(){
    // manages the navigation between different destinations
    val navController = rememberAnimatedNavController()

    // bottom navigation bar icons
    val navItems = listOf(
        CustomNavItem("Home", Icons.Default.Home, 1) { navController.navigate("home") },
        CustomNavItem("Calendar", Icons.Default.DateRange, 2) { navController.navigate("calendar") },
        CustomNavItem("Set shift", ImageVector.vectorResource(id = R.drawable.preference_icon), 3) { navController.navigate("preferences") },
        CustomNavItem("Settings", Icons.Default.Settings, 4) { navController.navigate("settings") }
    )

    // Set as navItems[0] cause it works with address, so at first launch of app home button wasn't set as default
    var selectedItem by remember { mutableStateOf(navItems[0]) }
    var previousSelectedItem: CustomNavItem by remember{ mutableStateOf(navItems[0]) }

    Scaffold(
        content = {
            Box(modifier = Modifier
                .padding(it)
                .background(Color(0xffffffff))
            ){
                // navigation host holds all of the navigation destinations within the app
                // calling "navController.navigate("home")" you can travel through app
                // It can handle parameters.
                AnimatedNavHost(
                    navController = navController,
                    startDestination = "home",
                    modifier = Modifier.padding(mediumPadding),
                    enterTransition = {
                        slideIntoContainer(
                            towards =
                            if(previousSelectedItem.position > selectedItem.position)
                                AnimatedContentScope.SlideDirection.Right
                            else if(previousSelectedItem.position < selectedItem.position)
                                AnimatedContentScope.SlideDirection.Left
                            else
                                AnimatedContentScope.SlideDirection.Up,
                            animationSpec = tween(700)
                        )
                    }
                ) {
                    composable("home") { HomeScreen() }
                    composable("calendar") { CalendarScreen() }
                    composable("preferences") { ShiftPreferenceScreen()}
                    composable("settings") { SettingScreenRider() }
                }
            }
        },
        bottomBar = {
            CustomBottomAppBar(
                navItems = navItems,
                selectedItem = selectedItem,
                onItemSelected = { item ->
                    run {
                        previousSelectedItem = selectedItem
                        if(previousSelectedItem.position != item.position)
                            selectedItem = item
                    }
                },
                modifier = Modifier
                    .clip(RoundedCornerShape(20, 20, 0, 0))
                    .fillMaxWidth()
            )
        }
    )

    BackHandler {
        run {
            previousSelectedItem = selectedItem
            if(previousSelectedItem.position != navItems[0].position)
                selectedItem = navItems[0]
        }
    }
}