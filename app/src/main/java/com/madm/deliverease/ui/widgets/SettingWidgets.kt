package com.madm.deliverease.ui.widgets


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.madm.deliverease.R
import com.madm.deliverease.ui.theme.gilroy

@Preview
@Composable
fun PreferencesSetting(){
    Column( verticalArrangement = Arrangement.spacedBy(10.dp)){
        Divider(stringResource(id = R.string.BottomBarSettings))
        Language()
        DarkMode()
        Divider(stringResource(id = R.string.general))
        ReportBug()
        TermsAndConditions()
        Row(Modifier.height(10.dp)){}
        LogOut()
    }
}

@Composable
fun Divider(text: String ){
    Row( modifier = Modifier
        .background(color = Color.LightGray)
        .height(40.dp)
        .fillMaxWidth()
        .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically){
        Text(text,
            style = TextStyle(
                fontFamily = gilroy,
                fontSize = 20.sp,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Bold
            )
        )
    }
}

@Preview
@Composable
fun Language(){
    var expanded by remember { mutableStateOf(false) }
    val items = listOf("English", "Italiano")
    var selectedIndex by remember { mutableStateOf(0) }

    Box(modifier = Modifier
        .fillMaxWidth()
        .height(40.dp)){
        Icon(painter = painterResource(id = R.drawable.languages), contentDescription = "language icon",
            modifier = Modifier
                .padding(start = 9.dp, top = 2.dp)
                .size(26.dp)
                .align(Alignment.CenterStart))
        Text(stringResource(id = R.string.language), modifier = Modifier
            .padding(start = 48.dp, top = 0.dp)
            .align(Alignment.CenterStart),
            style = TextStyle(
                fontFamily = gilroy,
                fontSize = 18.sp,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.SemiBold
            ))


        Box(modifier = Modifier
            .align(Alignment.CenterEnd)
            .width(100.dp)
            .padding(end = 10.dp, top = 8.dp))
        {
            Text(items[selectedIndex],
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = { expanded = true })
                    .background(Color.LightGray)
                    .padding(start = 20.dp))

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(Color.Gray)
            ) {
                items.forEachIndexed { index, s ->
                    DropdownMenuItem(onClick = {
                        selectedIndex = index
                        expanded = false
                    }) {
                        Text(text = s )
                    }
                }
            }
        }
    }
}

@Composable
fun DarkMode(){
    var switchCheckedState by remember { mutableStateOf(false) }

    Box(modifier = Modifier
        .fillMaxWidth()
        .height(40.dp)){
        Icon(painter = painterResource(id = R.drawable.dark_theme), contentDescription = "change theme",
            modifier = Modifier
                .padding(start = 8.dp, top = 1.dp)
                .size(31.dp)
                .align(Alignment.CenterStart))
        Text(stringResource(id = R.string.darkMode), modifier = Modifier
            .padding(start = 48.dp, top = 0.dp)
            .align(Alignment.CenterStart),
            style = TextStyle(
                fontFamily = gilroy,
                fontSize = 18.sp,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.SemiBold
            ))

        Switch(
            checked = switchCheckedState,
            onCheckedChange = { switchCheckedState = it },
            modifier = Modifier
                .padding(end = 16.dp, top = 10.dp)
                .align(Alignment.CenterEnd)
                .size(100.dp)
        )
    }
}

@Composable
fun ReportBug(){
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(40.dp).clickable { /*TODO*/ }){
        Icon(painter = painterResource(id = R.drawable.bug), contentDescription = "bug",
            modifier = Modifier
                .padding(start = 8.dp, top = 1.dp)
                .size(28.dp)
                .align(Alignment.CenterStart))
        Text(stringResource(id = R.string.bug), modifier = Modifier
            .padding(start = 48.dp, top = 0.dp)
            .align(Alignment.CenterStart),
            style = TextStyle(
                fontFamily = gilroy,
                fontSize = 18.sp,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.SemiBold
            ))

        Icon(painter = painterResource(id = R.drawable.next_icon), contentDescription = "next screen",
            modifier = Modifier
                .padding(end = 4.dp, top = 10.dp)
                .size(44.dp)
                .align(Alignment.CenterEnd))
    }
}

@Composable
fun TermsAndConditions(){
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(40.dp).clickable { /*TODO*/ }){
        Icon(painter = painterResource(id = R.drawable.terms_and_conditions), contentDescription = "terms and conditions",
            modifier = Modifier
                .padding(start = 12.dp, top = 1.dp)
                .size(28.dp)
                .align(Alignment.CenterStart))
        Text(stringResource(id = R.string.termsAndConditions), modifier = Modifier
            .padding(start = 48.dp, top = 0.dp)
            .align(Alignment.CenterStart),
            style = TextStyle(
                fontFamily = gilroy,
                fontSize = 18.sp,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.SemiBold
            ))

        Icon(painter = painterResource(id = R.drawable.next_icon), contentDescription = "next screen",
            modifier = Modifier
                .padding(end = 4.dp, top = 10.dp)
                .size(44.dp)
                .align(Alignment.CenterEnd))
    }
}

@Composable
fun LogOut() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .background(Color.Red).clickable { /*TODO*/ }
    ) {
        Icon(
            painter = painterResource(id = R.drawable.log_out),
            contentDescription = "logout",
            modifier = Modifier
                .padding(start = 140.dp, top = 1.dp)
                .size(28.dp)
                .align(Alignment.CenterStart),
            tint = Color.White
        )
        Text(
            stringResource(id = R.string.logout), modifier = Modifier
                .padding(start = 180.dp, top = 0.dp)
                .align(Alignment.CenterStart),
            style = TextStyle(
                fontFamily = gilroy,
                fontSize = 18.sp,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.SemiBold
            ),
            color = Color.White
        )
    }
}

