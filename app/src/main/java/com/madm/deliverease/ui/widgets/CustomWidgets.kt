package com.madm.deliverease.ui.widgets

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.madm.deliverease.R
import com.madm.deliverease.ui.theme.*

@Composable
fun MyPageHeader(){
    Box(
        modifier = Modifier
            .fillMaxWidth()){
        Image(
            painter = painterResource(id = R.drawable.logo_dark_icon),
            contentDescription = "logo",
            modifier = Modifier
                .size((LocalConfiguration.current.screenWidthDp.dp), 60.dp)
                .padding(0.dp),
            contentScale = ContentScale.FillHeight,
            alignment = Alignment.CenterStart
        )
        Text(
            text = stringResource(R.string.app_name),
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontSize = 30.sp,
            fontFamily = gilroy
        )
    }
}


@Composable
fun MyOutlinedTextField(
    field: MutableState<String>,
    isError: MutableState<Boolean>,
    label: String = "",
    onDone: () -> Unit = {}
){
    OutlinedTextField(
        value = field.value,
        onValueChange = { field.value = it },
        modifier = Modifier
            .padding(bottom = smallPadding)
            /*.border(
                BorderStroke(2.dp, Color(0xFFD8D8D8)),
                shape = RoundedCornerShape(50)
            )
            .clip(RoundedCornerShape(50))*/
            .fillMaxWidth(),
        label = {
            Text(text = label)
        },
        enabled = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Ascii,
            imeAction = ImeAction.Done // Done, not Enter
        ),
        keyboardActions = KeyboardActions(
            onDone = {onDone},
        ),
        singleLine = true,
        maxLines = 1,
        textStyle = TextStyle(
            fontFamily = gilroy,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xff131b31)
        ),
        shape = RoundedCornerShape(20.dp),
        colors = TextFieldDefaults.textFieldColors(
            textColor = Color(0xff131b31),
            backgroundColor = Color.Transparent,
            cursorColor = Color(0xff131b31),
            focusedIndicatorColor = Color.Gray
        ),
        isError = isError.value,
    )
}

@Composable
fun MyButton(
    onClick: () -> Unit = {},
    text: String,
    imgId: Int
){
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = mediumPadding, end = mediumPadding, bottom = mediumPadding)
            .border(BorderStroke(2.dp, Color(0xFFD8D8D8)), shape = RoundedCornerShape(50)),
        shape = RoundedCornerShape(6.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.Transparent,
            contentColor = Color(0xff131b31),

            ),
        elevation = ButtonDefaults.elevation(0.dp, 0.dp)
    ) {
        Image(
            painter = painterResource(id = imgId),
            contentDescription = text,
            modifier = Modifier.size(20.dp)
        )
        Text(text = text, modifier = Modifier.padding(6.dp))
    }
}

@Composable
fun LoginButton(
    username: MutableState<String>,
    password: MutableState<String>,
    isError: MutableState<Boolean>,
    goToRiderHome: () -> Unit,
    goToAdminHome: () -> Unit,
    onClick: () -> Unit
){
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = smallPadding)
            .clip(RoundedCornerShape(50))
            .border(
                BorderStroke(2.dp, Color(0xFFD8D8D8)),
                shape = RoundedCornerShape(50)
            ),
        shape = RoundedCornerShape(6.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.Transparent,
            contentColor = Color(0xff131b31)
        ),
        elevation = ButtonDefaults.elevation(0.dp, 0.dp),
        enabled = username.value.isNotBlank() && password.value.isNotBlank(),
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Login", modifier = Modifier.padding(6.dp))
            Icon(Icons.Default.ArrowForward, contentDescription = stringResource(R.string._continue))
        }
    }
}

@Composable
fun NewsCard(/* TODO: add parameters to pass news */) {
    val list = listOf("A", "B", "C", "D", "E")

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
                Text("Communications",
                    style = TextStyle(fontSize = 22.sp),
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
                        Button(onClick = { /*TODO: action send new communication to everyone */
                            /* TODO: Dummy */
                            textFieldValue.value = ""
                            showTextField.value = !showTextField.value
                        }) {
                            Text(stringResource(R.string.send))
                        }
                        Spacer(Modifier.width(4.dp))
                        Button(onClick = {
                            /* TODO: Dummy */
                            textFieldValue.value = ""
                            showTextField.value = !showTextField.value
                        }) {
                            Text(stringResource(R.string.cancel))
                        }
                    }
                }
            }

            LazyColumn(
                content = {
                    items(list) { item ->
                        Card(Modifier.padding(smallPadding)) {
                            CustomNewsRow("News! I have read published calendar letter $item")
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun CustomNewsRow(
    text: String,
    date: String /* TODO: check date with format on the server*/ = "19/05/2023"
) {
    Column(
        modifier = Modifier
            .clip(Shapes.small)
            .fillMaxWidth()
            .padding(smallPadding)
    ) {
        Text(text)
        Spacer(modifier = Modifier.height(2.dp))
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Published on: $date")
        }
    }
}

@Composable
fun RidersCard(/* TODO: add params to add list of riders */) {
    val list = listOf("A", "B", "C", "D")

    Card(
        elevation = mediumCardElevation,
        shape = Shapes.medium,
        modifier = Modifier
            .fillMaxWidth()
            .padding(nonePadding, smallPadding)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(stringResource(R.string.todays_rider), style = TextStyle(fontSize = 22.sp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                content = {
                    items(list) {item ->
                        Card(Modifier.padding(smallPadding)) {
                            CustomRiderRow("Name $item", "Surname $item")
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun CustomRiderRow(
    name: String = "Name",
    surname: String = "Surname",
    //backGroundColor: Color = Color.White /* TODO: check if really needed or use only Theme colors to set it */
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(Shapes.medium)
            //.background(backGroundColor) /* TODO: check if really needed or use only Theme colors to set it */
            .padding(mediumPadding)
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.rider),
            contentDescription = "rider",
            Modifier.size(30.dp)
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f)
        ) {
            Text(name)
            Text(surname)
        }
    }
}