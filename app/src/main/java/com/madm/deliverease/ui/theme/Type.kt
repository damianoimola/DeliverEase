package com.madm.deliverease.ui.theme

import androidx.compose.material.Typography
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.madm.deliverease.R


val gilroy = FontFamily(
    Font(R.font.gilroy_thin, FontWeight.Thin),
    Font(R.font.gilroy_ultralight, FontWeight.ExtraLight),
    Font(R.font.gilroy_light, FontWeight.Light),
    Font(R.font.gilroy_medium, FontWeight.Medium),
    Font(R.font.gilroy_regular, FontWeight.Normal),
    Font(R.font.gilroy_semibold, FontWeight.SemiBold),
    Font(R.font.gilroy_bold, FontWeight.Bold),
    Font(R.font.gilroy_extrabold, FontWeight.ExtraBold),
    Font(R.font.gilroy_black, FontWeight.Black),
)



/*
    h1 is the largest headline, reserved for short and important text.
    h2 is the second-largest headline, reserved for short and important text.
    h3 is the third-largest headline, reserved for short and important text.
    h4 is the fourth-largest headline, reserved for short and important text.
    h5 is the fifth-largest headline, reserved for short and important text.
    h6 is the sixth-largest headline, reserved for short and important text.
    subtitle1 is the largest subtitle and is typically reserved for medium-emphasis text that is shorter in length.
    subtitle2 is the smallest subtitle and is typically reserved for medium-emphasis text that is shorter in length.
    body1 is the largest body and is typically reserved for a long-form text that is shorter in length.
    body2 is the smallest body and is typically reserved for a long-form text that is shorter in length.
    button is reserved for a button text.
    caption is one of the smallest font sizes it reserved for annotating imagery or introduce a headline.
    over-line is one of the smallest font sizes.
 */


val Typography = Typography(
    defaultFontFamily = gilroy,
    body1 = TextStyle(
        fontFamily = gilroy,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
    ),
    h4 = TextStyle(
        fontFamily = gilroy,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp,
    ),
    h5 = TextStyle(
        fontFamily = gilroy,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
    ),
    h6 = TextStyle(
        fontFamily = gilroy,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
    ),
    subtitle1 = TextStyle(
        fontFamily = gilroy,
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp,
    )
)

val LocalTypography = staticCompositionLocalOf { Typography }