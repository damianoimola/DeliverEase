package com.madm.deliverease.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember


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

var lightColorsPalette = CustomThemeColors(
    primary = lightPrimary,
    primaryVariant = lightPrimaryVariant,
    secondary = lightSecondary,
    secondaryVariant = lightSecondaryVariant,
    tertiary = lightTertiary,
    tertiaryVariant = lightTertiaryVariant,
    background = lightBackground,
    backgroundVariant = lightBackgroundVariant,
    surface = lightSurface,
    error = lightError,
    onPrimary = lightOnPrimary,
    onPrimaryVariant = lightOnPrimaryVariant,
    onSecondary = lightOnSecondary,
    onSecondaryVariant = lightOnSecondaryVariant,
    onTertiary = lightOnTertiary,
    onTertiaryVariant = lightOnTertiaryVariant,
    onBackground = lightOnBackground,
    onBackgroundVariant = lightOnBackgroundVariant,
    onSurface = lightOnSurface,
    onError = lightOnError,
)

var darkColorPalette = CustomThemeColors(
    primary = darkPrimary,
    primaryVariant = darkPrimaryVariant,
    secondary = darkSecondary,
    secondaryVariant = darkSecondaryVariant,
    tertiary = darkTertiary,
    tertiaryVariant = darkTertiaryVariant,
    background = darkBackground,
    backgroundVariant = darkBackgroundVariant,
    surface = darkSurface,
    error = darkError,
    onPrimary = darkOnPrimary,
    onPrimaryVariant = darkOnPrimaryVariant,
    onSecondary = darkOnSecondary,
    onSecondaryVariant = darkOnSecondaryVariant,
    onTertiary = darkOnTertiary,
    onTertiaryVariant = darkOnTertiaryVariant,
    onBackground = darkOnBackground,
    onBackgroundVariant = darkOnBackgroundVariant,
    onSurface = darkOnSurface,
    onError = darkOnError
)

@Composable
fun DeliverEaseTheme(
    shapes: Shapes = CustomTheme.shapes,
    typography: Typography = CustomTheme.typography,
    colors: CustomThemeColors = lightColorsPalette,
    darkColors: CustomThemeColors = darkColorPalette,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val currentColor = remember { if (darkTheme) darkColors else colors }
    val rememberedColors = remember { currentColor.copy() }.apply { updateColorsFrom(currentColor) }
    CompositionLocalProvider(
        LocalColors provides rememberedColors,
        LocalShapes provides shapes,
        LocalTypography provides typography,
    ) {
        // To provide text style to app
        ProvideTextStyle(
            typography.body1,
            content = content
        )
    }
}



/* Old MaterialTheme
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
 */