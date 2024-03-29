package com.madm.deliverease.ui.screens.riders

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.madm.deliverease.R
import com.madm.deliverease.ui.theme.CustomTheme
import com.madm.deliverease.ui.theme.mediumPadding
import com.madm.deliverease.ui.widgets.ConfirmExitingApp
import com.madm.deliverease.ui.widgets.CustomBottomAppBar
import com.madm.deliverease.ui.widgets.CustomNavItem
import com.madm.deliverease.ui.widgets.CustomTopAppBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun RidersMainContent(logoutCallback : () -> Unit){
    val context = LocalContext.current
    // manages the navigation between different destinations
    val navController = rememberAnimatedNavController()

    navController.enableOnBackPressed(false)

    // bottom navigation bar icons
    val navItems = listOf(
        CustomNavItem("Home", Icons.Default.Home, 1) {
            if(navController.currentDestination?.route != context.getString(R.string.rider_home))
                navController.navigate(context.getString(R.string.rider_home))
        },
        CustomNavItem("Calendar", Icons.Default.DateRange, 2) {
            if(navController.currentDestination?.route != context.getString(R.string.rider_calendar))
                navController.navigate(context.getString(R.string.rider_calendar))
        },
        CustomNavItem("Set shift", ImageVector.vectorResource(id = R.drawable.preference_icon), 3) {
            if(navController.currentDestination?.route != context.getString(R.string.rider_preferences))
                navController.navigate(context.getString(R.string.rider_preferences))
        },
        CustomNavItem("Settings", Icons.Default.Settings, 4) {
            if(navController.currentDestination?.route != context.getString(R.string.rider_settings))
                navController.navigate(context.getString(R.string.rider_settings))
        }
    )

    // Set as navItems[0] cause it works with address, so at first launch of app home button wasn't set as default
    var selectedItem by rememberSaveable { mutableStateOf(navItems[0]) }
    var previousSelectedItem: CustomNavItem by rememberSaveable { mutableStateOf(navItems[0]) }
    //variable used to show the exiting dialog
    var showExitingDialog by rememberSaveable { mutableStateOf(false) }

    if(showExitingDialog)
        ConfirmExitingApp() { showExitingDialog = false }

    selectedItem = navItems[0]

    Scaffold(
        backgroundColor = CustomTheme.colors.background,
        topBar = { CustomTopAppBar() },
        content = {
            Box(Modifier.padding(it)){
                // navigation host holds all of the navigation destinations within the app
                // calling "navController.navigate("home")" you can travel through app
                // It can handle parameters.
                AnimatedNavHost(
                    navController = navController,
                    startDestination = context.getString(R.string.rider_home),
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
                            animationSpec = tween(500)
                        )
                    }
                ) {
                    composable(context.getString(R.string.rider_home)) {
                        CoroutineScope(Dispatchers.IO).launch {
                            if(selectedItem.position != 1)
                                selectedItem = navItems[0]
                        }
                        HomeScreen()
                    }
                    composable(context.getString(R.string.rider_calendar)) {
                        CoroutineScope(Dispatchers.IO).launch {
                            if(selectedItem.position != 2)
                                selectedItem = navItems[1]
                        }
                        CalendarScreen()
                    }
                    composable(context.getString(R.string.rider_preferences)) {
                        CoroutineScope(Dispatchers.IO).launch {
                            if(selectedItem.position != 3)
                                selectedItem = navItems[2]
                        }
                        ShiftPreferenceScreen()
                    }
                    composable(context.getString(R.string.rider_settings)) {
                        CoroutineScope(Dispatchers.IO).launch {
                            if(selectedItem.position != 4)
                                selectedItem = navItems[3]
                        }
                        SettingScreenRider(logoutCallback)
                    }
                }
            }
        },
        bottomBar = {
            CustomBottomAppBar(
                navItems = navItems,
                selectedItem = selectedItem,
                onItemSelected = { item ->
                    CoroutineScope(Dispatchers.IO).launch {
                        previousSelectedItem = selectedItem
                        if(item != previousSelectedItem){
                            selectedItem = item
                        }
                    }
                },
                modifier = Modifier
                    .clip(RoundedCornerShape(20, 20, 0, 0))
                    .fillMaxWidth()
            )
        },
    )
    BackHandler(enabled = true) {
        previousSelectedItem = selectedItem
        if(selectedItem.position != 1) {
            selectedItem = navItems[0]
            navController.navigate(context.getString(R.string.rider_home))
        }
        else{
            showExitingDialog = true
        }
    }
}