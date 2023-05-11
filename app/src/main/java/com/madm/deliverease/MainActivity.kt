package com.madm.deliverease

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import com.madm.deliverease.ui.theme.DeliverEaseTheme
import com.madm.deliverease.ui.widgets.CustomBottomAppBar
import com.madm.deliverease.ui.widgets.CustomNavItem
import com.madm.deliverease.ui.widgets.DummyComposeFunction

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // bottom navigation bar icons
        val navItems = listOf(
            CustomNavItem("Home", Icons.Default.Home),
            CustomNavItem("Calendar", Icons.Default.DateRange),
            CustomNavItem("Messages", Icons.Default.Email)
        )

        setContent {
            var selectedItem by remember { mutableStateOf(CustomNavItem("Home", Icons.Default.Home)) }
            DeliverEaseTheme {
                Scaffold(
                    content = {
                        Box(modifier = Modifier.padding(it)){
                            DummyComposeFunction()
                        }
                    },
                    bottomBar = {
                        CustomBottomAppBar(
                            navItems = navItems,
                            selectedItem = selectedItem,
                            onItemSelected = { item -> selectedItem = item },
                            modifier = Modifier.fillMaxWidth()
                        )
                    },
                )


//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colors.background
//                ) {
//                    ModalNavigationDrawer()
//                }
            }
        }
    }
}
