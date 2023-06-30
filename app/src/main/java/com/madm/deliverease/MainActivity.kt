package com.madm.deliverease

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.madm.deliverease.localization.ContextWrapper
import com.madm.deliverease.ui.screens.access.LoginScreen
import com.madm.deliverease.ui.screens.admin.AdminsMainContent
import com.madm.deliverease.ui.screens.riders.RidersMainContent
import com.madm.deliverease.ui.theme.DeliverEaseTheme
import java.util.*

@OptIn(ExperimentalAnimationApi::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            // manages the navigation between different destinations
            val navController = rememberAnimatedNavController()

            val sharedPreferences = LocalContext.current.getSharedPreferences(SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE)
            val savedTheme = sharedPreferences.getString(SELECTED_THEME, if(isSystemInDarkTheme()) "dark" else "light")

            darkMode = savedTheme != "light"

            DeliverEaseTheme(darkTheme = darkMode) {
                // navigation host holds all of the navigation destinations within the app
                // calling "navController.navigate("home")" you can travel through app
                // It can handle parameters.
                AppNavigationGraph(navController = navController)
            }
        }
    }



    override fun attachBaseContext(newBaseContext: Context) {
        // open the shared prefs file
        val sharedPreferences = newBaseContext.getSharedPreferences(SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE)
        val language = sharedPreferences.getString(STARTUP_LANGUAGE_FIELD, "en")

        // setting up the locale
        val locale = Locale(language!!)
        Locale.setDefault(locale)

        // provide the locale to the ContextWrapper custom child
        val context: Context = ContextWrapper.wrap(newBaseContext, locale)

        // call the "attachBaseContext" method of the parent (super class)
        super.attachBaseContext(context)
    }


    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    fun AppNavigationGraph(navController: NavHostController){

        val context = LocalContext.current

        AnimatedNavHost(
            navController = navController,
            startDestination = context.getString(R.string.general_login),
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentScope.SlideDirection.Left,
                    animationSpec = tween(700))
            }
        ) {
            composable(context.getString(R.string.general_login)) {
                LoginScreen(
                    goToRiderHome = {
                        if (navController.currentDestination?.route != context.getString(R.string.general_rider_home)) {
                            navController.navigate(context.getString(R.string.general_rider_home)) {
                                popUpTo(context.getString(R.string.general_login)) { inclusive = true }
                            }
                        }
                    },
                    goToAdminHome = {
                        if (navController.currentDestination?.route != context.getString(R.string.general_admin_home)) {
                            navController.navigate(context.getString(R.string.general_admin_home)) {
                                popUpTo(context.getString(R.string.general_login)) { inclusive = true }
                            }
                        }
                    }
                )
            }
            composable(context.getString(R.string.general_rider_home)) {
                RidersMainContent {
                    if (navController.currentDestination?.route != context.getString(R.string.general_login)) {
                        navController.navigate(context.getString(R.string.general_login)) {
                            popUpTo(context.getString(R.string.general_rider_home)) { inclusive = true }
                            popUpTo(context.getString(R.string.general_admin_home)) { inclusive = true }
                        }

                        cleanGlobalAppData()
                        preferencesLogout(context)
                    }
                }
            }
            composable(context.getString(R.string.general_admin_home)) {
                AdminsMainContent {
                    if (navController.currentDestination?.route != context.getString(R.string.general_login)) {
                        navController.navigate(context.getString(R.string.general_login)) {
                            popUpTo(context.getString(R.string.general_rider_home)) { inclusive = true }
                            popUpTo(context.getString(R.string.general_admin_home)) { inclusive = true }
                        }

                        cleanGlobalAppData()
                        preferencesLogout(context)
                    }
                }
            }
        }
    }
}

fun preferencesLogout(context: Context){
    val sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()

    editor.putString(EMAIL_FIELD, "")
    editor.putString(PASSWORD_FIELD, "")

    editor.apply()
}