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
import com.airbnb.lottie.compose.*
import com.madm.deliverease.R

@Composable
fun PizzaLoader(isPlaying: MutableState<Boolean>){
    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.pizza_loading))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        isPlaying = isPlaying.value,
        iterations = LottieConstants.IterateForever,
        reverseOnRepeat = false,
        cancellationBehavior = LottieCancellationBehavior.Immediately)

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        LottieAnimation(
            composition = composition,
            modifier = Modifier.size(200.dp),
            progress = { progress },
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