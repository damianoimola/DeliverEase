package com.madm.deliverease

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.madm.deliverease.ui.screens.CalendarScreen
import com.madm.deliverease.ui.screens.HomeScreen
import com.madm.deliverease.ui.screens.MessagesScreen
import com.madm.deliverease.ui.screens.access.LoginScreen
import com.madm.deliverease.ui.theme.DeliverEaseTheme
import com.madm.deliverease.ui.theme.mediumPadding
import com.madm.deliverease.ui.widgets.CustomBottomAppBar
import com.madm.deliverease.ui.widgets.CustomNavItem

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            // manages the navigation between different destinations
            val navController = rememberNavController()

            // bottom navigation bar icons
            val navItems = listOf(
                CustomNavItem("Home", Icons.Default.Home) { navController.navigate("home") },
                CustomNavItem("Calendar", Icons.Default.DateRange) { navController.navigate("calendar") },
                CustomNavItem("Messages", Icons.Default.Email) { navController.navigate("messages") }
            )

            // Set as navItems[0] cause it works with address, so at first launch of app home button wasn't set as default
            var selectedItem by remember { mutableStateOf(navItems[0]) }


            DeliverEaseTheme {
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
                                startDestination = "login",
                                modifier = Modifier.padding(mediumPadding)
                            ) {
                                composable("login") { LoginScreen()  }
                                composable("home") { HomeScreen() }
                                composable("calendar") { CalendarScreen() }
                                composable("messages") { MessagesScreen() }
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
        }
    }
}
