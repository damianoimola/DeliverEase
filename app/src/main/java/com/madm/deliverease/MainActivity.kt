package com.madm.deliverease

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.madm.deliverease.ui.screens.access.LoginScreen
import com.madm.deliverease.ui.screens.admin.AdminsMainContent
import com.madm.deliverease.ui.screens.riders.RidersMainContent
import com.madm.deliverease.ui.theme.DeliverEaseTheme

@OptIn(ExperimentalAnimationApi::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            // manages the navigation between different destinations
            val navController = rememberAnimatedNavController()

            DeliverEaseTheme {
                // navigation host holds all of the navigation destinations within the app
                // calling "navController.navigate("home")" you can travel through app
                // It can handle parameters.
                AppNavigationGraph(navController = navController)
            }
        }
    }


    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    fun AppNavigationGraph(navController: NavHostController){
        AnimatedNavHost(
            navController = navController,
            startDestination = "login",
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentScope.SlideDirection.Left,
                    animationSpec = tween(700))
            }
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
