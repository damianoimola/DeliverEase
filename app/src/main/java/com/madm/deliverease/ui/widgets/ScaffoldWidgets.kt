package com.madm.deliverease.ui.widgets

import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp


class CustomNavItem(
    val title: String,
    val icon: ImageVector,
    val function: () -> Unit
)

@Composable
fun CustomBottomAppBar(
    navItems: List<CustomNavItem>,
    selectedItem: CustomNavItem,
    onItemSelected: (CustomNavItem) -> Unit,
    modifier: Modifier = Modifier
) {
    BottomAppBar(
        modifier = modifier,
    ) {
        navItems.forEach { navItem : CustomNavItem ->
            BottomNavigationItem(
                icon = { Icon(navItem.icon, contentDescription = navItem.title) },
                label = { Text(text = navItem.title, color = Color.White, maxLines = 1) },
                selected = navItem == selectedItem,
                onClick = {
                    onItemSelected(navItem)
                    navItem.function()
                },
                alwaysShowLabel = false,
                selectedContentColor = Color.White,
                unselectedContentColor = Color.White.copy(alpha = 0.5f),
                modifier = Modifier.padding(vertical = 0.dp),
            )
        }
    }
}
