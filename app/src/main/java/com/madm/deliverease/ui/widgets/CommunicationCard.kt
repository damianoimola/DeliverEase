package com.madm.deliverease.ui.widgets

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.madm.deliverease.R
import com.madm.deliverease.ui.theme.Shapes
import com.madm.deliverease.ui.theme.mediumCardElevation
import com.madm.deliverease.ui.theme.nonePadding
import com.madm.deliverease.ui.theme.smallPadding

data class Communication(val text: String, val data: String)

@Composable
fun NewsCard(
    communicationList: MutableList<Communication>,
    sendCommunication: (String) -> Unit = {},
) {
    val showTextField = remember { mutableStateOf(false) }
    val textFieldValue = remember { mutableStateOf("") }

    Card(
        elevation = mediumCardElevation,
        shape = Shapes.medium,
        modifier = Modifier
            .fillMaxSize()
            .padding(nonePadding, smallPadding)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(smallPadding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    stringResource(R.string.communication_title),
                    style = TextStyle(fontSize = 22.sp), /* TODO: set to typography */
                    textAlign = TextAlign.Center
                )
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(onClick = { showTextField.value = !showTextField.value }) {
                        if(!showTextField.value) Icon(Icons.Default.Add, "Add")
                        else Icon(ImageVector.vectorResource(id = R.drawable.remove),"Remove")
                    }
                }
            }

            if(showTextField.value) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(smallPadding, nonePadding)
                ) {
                    TextField(
                        value = textFieldValue.value,
                        onValueChange = { textFieldValue.value = it },
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(onClick = {
                            sendCommunication(textFieldValue.value)
                            textFieldValue.value = ""
                            showTextField.value = !showTextField.value
                        }) {
                            Text(stringResource(R.string.send))
                        }
                        Spacer(Modifier.width(4.dp))
                        Button(onClick = {
                            textFieldValue.value = ""
                            showTextField.value = !showTextField.value
                        }) {
                            Text(stringResource(R.string.cancel))
                        }
                    }
                }
            }

            if(communicationList.isEmpty()) Text(stringResource(R.string.no_communications), style = TextStyle(fontSize = 18.sp))

            LazyColumn(
                content = {
                    items(communicationList) { item ->
                        Card(Modifier.padding(smallPadding)) {
                            CustomCommunication(item.text,item.data)
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun CustomCommunication(
    noticeText: String,
    publishDate: String
) {
    Column(
        modifier = Modifier
            .clip(Shapes.small)
            .fillMaxWidth()
            .padding(smallPadding)
    ) {
        Text(noticeText)
        Spacer(modifier = Modifier.height(2.dp))
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Published on: $publishDate") /* TODO: set to typography */
        }
    }
}