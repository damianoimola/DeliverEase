package com.madm.deliverease.ui.widgets

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.madm.common_libs.model.User
import com.madm.deliverease.R
import com.madm.deliverease.ui.theme.*

@Composable
fun TodayRidersCard(
    modifier: Modifier = Modifier,
    riderList: List<User>,
    columns: Int = 2,
    isPortrait: Int,
    isLoading: Boolean = false
) {
    Card(
        elevation = mediumCardElevation,
        shape = Shapes.medium,
        modifier = modifier
            .fillMaxSize()
            .padding(smallPadding * (1-isPortrait), smallPadding * isPortrait),
        backgroundColor = CustomTheme.colors.surface
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(nonePadding, smallPadding)
        ) {
            Text(
                stringResource(R.string.todays_rider),
                style = CustomTheme.typography.h3,
                color = CustomTheme.colors.onSurface,
            )
            
            if(riderList.isEmpty() && !isLoading)  Text(
                stringResource(R.string.no_riders_today),
                style = CustomTheme.typography.body1,
                color = CustomTheme.colors.onSurface
            )
            
            LazyVerticalGrid(
                columns = GridCells.Fixed(columns),
                content = {
                    items(if(isLoading) listOf(1,2,3,4,5,6,7,8) else riderList) {rider ->
                        Card(
                            Modifier.padding(smallPadding),
                            backgroundColor = CustomTheme.colors.surface,
                            contentColor = CustomTheme.colors.onSurface,
                            elevation = extraSmallCardElevation
                        ) {
                            if (isLoading)
                                ShimmerRiderRow()
                            else {
                                RiderRow(rider as User)
                            }
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun RiderRow(
    rider: User,
    inLine: Boolean = false, // Let you switch name from Column to Row structure
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(CustomTheme.shapes.large)
            .padding(mediumPadding)
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.rider),
            contentDescription = "rider",
            Modifier.size(30.dp)
        )
        if(inLine)
            Row(
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.weight(1f)
            ) {
                Divider(Modifier.width(smallPadding), Color.Transparent)
                Text(
                    rider.name!!,
                    style = CustomTheme.typography.body1,
                    color = CustomTheme.colors.onTertiary
                )
                Divider(Modifier.width(smallPadding), Color.Transparent)
                Text(
                    rider.surname!!,
                    style = CustomTheme.typography.body1,
                    color = CustomTheme.colors.onTertiary
                )
            }
        else
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    rider.name!!,
                    style = CustomTheme.typography.body1,
                    color = CustomTheme.colors.onSurface
                )
                Text(
                    rider.surname!!,
                    style = CustomTheme.typography.body1,
                    color = CustomTheme.colors.onSurface
                )
            }
    }
}

@Composable
fun ShimmerRiderRow(
    inLine: Boolean = false
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(CustomTheme.shapes.large)
            .padding(mediumPadding)
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.rider),
            contentDescription = "rider",
            Modifier.size(30.dp)
        )

        if(inLine)
            Row(
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .height(18.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .shimmerEffect()
                )
                Divider(Modifier.width(smallPadding), Color.Transparent)
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .height(18.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .shimmerEffect()
                )
            }
        else
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(18.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .shimmerEffect()
                )
                Divider(Modifier.width(2.dp), Color.Transparent)
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .height(18.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .shimmerEffect()
                )
            }
    }
}