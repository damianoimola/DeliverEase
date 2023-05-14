package com.madm.deliverease

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.madm.deliverease.ui.screens.access.LoginScreen
import com.madm.deliverease.ui.screens.admin.AdminsMainContent
import com.madm.deliverease.ui.screens.riders.RidersMainContent
import com.madm.deliverease.ui.theme.DeliverEaseTheme
import com.madm.deliverease.ui.theme.mediumPadding
import com.madm.deliverease.ui.theme.nonePadding

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            // manages the navigation between different destinations
            val navController = rememberNavController()

            DeliverEaseTheme {
                // navigation host holds all of the navigation destinations within the app
                // calling "navController.navigate("home")" you can travel through app
                // It can handle parameters.
                NavHost(
                    navController = navController,
                    startDestination = "login",
                ) {
                    composable("login") {
                        LoginScreen(
                            goToRiderHome = {
                                navController.navigate("rider-home") {
                                    popUpTo("login") { inclusive = true }
                                }
                            },
                            goToAdminHome = {
                                navController.navigate("admin-home") {
                                    popUpTo("login") { inclusive = true }
                                }
                            }
                        )
                    }
                    composable("rider-home") { RidersMainContent() }
                    composable("admin-home") { AdminsMainContent() }
                }
            }
        }
    }
}
