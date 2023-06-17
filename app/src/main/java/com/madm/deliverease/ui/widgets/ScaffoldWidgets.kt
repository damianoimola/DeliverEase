package com.madm.deliverease.ui.widgets

import android.os.Parcelable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.madm.deliverease.R
import com.madm.deliverease.ui.theme.CustomTheme
import com.madm.deliverease.ui.theme.mediumPadding
import com.madm.deliverease.ui.theme.smallPadding
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
class CustomNavItem(
    @IgnoredOnParcel val title: String = "",
    @IgnoredOnParcel val icon: ImageVector? = null,
    @IgnoredOnParcel val position: Int = -1,
    @IgnoredOnParcel val function: () -> Unit = {}
) : Parcelable

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
                icon = { Icon(navItem.icon!!, contentDescription = navItem.title) },
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
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(smallPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.deliverease_icon),
            contentDescription = "logo",
            modifier = Modifier
                .padding(smallPadding)
                .size(50.dp),
            tint = CustomTheme.colors.onBackground
        )
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(smallPadding),
        contentAlignment = Alignment.Center
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(smallPadding),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.deliverease_icon),
                contentDescription = "logo",
                modifier = Modifier.size(50.dp),
                tint = CustomTheme.colors.onBackground
            )
        }
        Text(
            text = stringResource(R.string.app_name),
            modifier = Modifier.fillMaxWidth(),
            style = CustomTheme.typography.h1,
            textAlign = TextAlign.Center,
            color = CustomTheme.colors.onBackground
        )
    }
}