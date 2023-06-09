package com.madm.deliverease.ui.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.madm.deliverease.R
import com.madm.deliverease.ui.theme.CustomTheme
import com.madm.deliverease.ui.theme.gilroy
import com.madm.deliverease.ui.theme.smallPadding


class CustomNavItem(
    val title: String,
    val icon: ImageVector,
    val position: Int,
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
        backgroundColor = CustomTheme.colors.primary,
        contentColor = CustomTheme.colors.onPrimary
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
                selectedContentColor = CustomTheme.colors.onPrimary,
                unselectedContentColor = CustomTheme.colors.onPrimary.copy(alpha = 0.5f),
                modifier = Modifier.padding(vertical = 0.dp),
            )
        }
    }
}




@Composable
fun CustomTopAppBar(){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(smallPadding),
    ){
        Image(
            painter = painterResource(id = R.drawable.logo_dark_icon_hd),
            contentDescription = "Logo", // stringResource(R.string.logo),
            modifier = Modifier
                .size((LocalConfiguration.current.screenWidthDp.dp), 50.dp)
                .padding(0.dp),
            contentScale = ContentScale.FillHeight,
            alignment = Alignment.CenterStart,
        ) // TODO Ralisin: set image with theme
        Text(
            text = stringResource(R.string.app_name),
            modifier = Modifier.fillMaxWidth(),
            style = CustomTheme.typography.h1,
            color = CustomTheme.colors.onBackground
        )
    }
}