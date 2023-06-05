package com.madm.deliverease.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


/*
    Background colors
        Primary color displayed most frequently across your app's screens and components;
        Primary Variant color is used to distinguish elements using primary colors, such as top app bar and the system bar.
        Secondary color provides more ways to accent and distinguish your product. Having a secondary color is optional, and should be applied sparingly to accent select parts of your UI;
        Secondary Variant color is used to distinguish elements using secondary colours;
        Background color appears behind scrollable content;
        Surface color uses on surfaces of components, like cards and menus;
        Error color used for indicating an error.

    Typography and icon colors
        On Primary color of text and icons displayed on top of the primary color.
        On Secondary color of text and icons displayed on top of the secondary color;
        On Background color of text and icons displayed on top of the background color;
        On Surface color of text and icons displayed on top of the surface color;
        On Error color of text and icons displayed on top of the error color.
 */

/*
private val DarkColorPalette = darkColors(
    // background colors
    background = darkBackground,
    surface = darkBackground,
    primary = darkDetails,
    primaryVariant = darkDetails,
    secondary = darkEnhancedDetails,

    // typography and icon colors
    onBackground = darkTextColor,
    onSurface = darkTextColor,
    onPrimary = darkTextColor,
    onSecondary = darkTextColor,
)

private val LightColorPalette = lightColors(
    // background colors
    background = lightBackground,
    surface = lightBackground,
    primary = lightDetails,
    primaryVariant = lightDetails,
    secondary = lightEnhancedDetails,

    // typography and icon colors
    onBackground = lightTextColor,
    onSurface = lightTextColor,
    onPrimary = lightTextColor,
    onSecondary = lightTextColor,
)
 */

private val DarkColorPalette = darkColors()

private val LightColorPalette = lightColors(
    background = lightBackground,
    surface = lightSurface,
    primary = lightPrimary,
    primaryVariant = lightPrimaryVariant,

    onPrimary = lightOnPrimary,
)


@Composable
fun DeliverEaseTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}