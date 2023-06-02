package com.madm.deliverease.ui.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.madm.deliverease.R

@Composable
fun PizzaLoader(isPlaying: MutableState<Boolean>){
    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.pizza_loading))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        isPlaying = isPlaying.value)
    LaunchedEffect(key1 = progress){
        if(progress == 1f)
            isPlaying.value = false
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        LottieAnimation(
            composition = composition,
            modifier = Modifier.size(300.dp),
            progress = { progress }
        )
    }
}

@Composable
fun PizzaLoaderDialog(isPlaying: MutableState<Boolean>){
    Dialog(
        onDismissRequest = { isPlaying.value != isPlaying.value },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = false
        )
    ) {
        PizzaLoader(isPlaying = isPlaying)
    }
}