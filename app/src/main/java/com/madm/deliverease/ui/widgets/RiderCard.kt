package com.madm.deliverease.ui.widgets

import android.content.res.Resources.Theme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
    riderList: List<User>,
    columns: Int = 2,
    modifier: Modifier = Modifier
) {
    Card(
        elevation = mediumCardElevation,
        shape = Shapes.medium,
        modifier = modifier
            .fillMaxWidth()
            .padding(nonePadding, smallPadding)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(nonePadding, smallPadding)
        ) {
            Text(stringResource(R.string.todays_rider), style = TextStyle(fontSize = 22.sp)) // TODO Ralisin: set Theme text style
            
            if(riderList.isEmpty()) Text(stringResource(R.string.no_riders_today), style = TextStyle(fontSize = 18.sp))
            
            LazyVerticalGrid(
                columns = GridCells.Fixed(columns),
                content = {
                    items(riderList) {rider ->
                        Card(Modifier.padding(smallPadding)) {
                            RiderRow(rider)
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
    inLine: Boolean = false // Let you switch name from Column to Row structure
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(Shapes.medium)
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
                Divider(Modifier.width(smallPadding))
                Text(rider.name!!)
                Divider(Modifier.width(smallPadding))
                Text(rider.surname!!)
            }
        else
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Text(rider.name!!)
                Text(rider.surname!!)
            }
    }
}