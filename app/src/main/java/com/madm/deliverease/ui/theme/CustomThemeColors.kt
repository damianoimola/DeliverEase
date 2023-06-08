package com.madm.deliverease.ui.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color


class CustomThemeColors(
    primary: Color,
    primaryVariant: Color,
    secondary: Color,
    secondaryVariant: Color,
    tertiary: Color,
    tertiaryVariant: Color,
    background: Color,
    backgroundVariant: Color,
    surface: Color,
    error: Color,
    onPrimary: Color,
    onPrimaryVariant: Color,
    onSecondary: Color,
    onSecondaryVariant: Color,
    onTertiary: Color,
    onTertiaryVariant: Color,
    onBackground: Color,
    onBackgroundVariant: Color,
    onSurface: Color,
    onError: Color
) {
    var primary by mutableStateOf(primary)
        private set
    var primaryVariant by mutableStateOf(primaryVariant)
        private set
    var secondary by mutableStateOf(secondary)
        private set
    var secondaryVariant by mutableStateOf(secondaryVariant)
        private set
    var tertiary by mutableStateOf(tertiary)
        private set
    var tertiaryVariant by mutableStateOf(tertiaryVariant)
        private set
    var background by mutableStateOf(background)
        private set
    var backgroundVariant by mutableStateOf(backgroundVariant)
        private set
    var surface by mutableStateOf(surface)
        private set
    var error by mutableStateOf(error)
        private set
    var onPrimary by mutableStateOf(onPrimary)
        private set
    var onPrimaryVariant by mutableStateOf(onPrimaryVariant)
        private set
    var onSecondary by mutableStateOf(onSecondary)
        private set
    var onSecondaryVariant by mutableStateOf(onSecondaryVariant)
        private set
    var onTertiary by mutableStateOf(onTertiary)
        private set
    var onTertiaryVariant by mutableStateOf(onTertiaryVariant)
        private set
    var onBackground by mutableStateOf(onBackground)
        private set
    var onBackgroundVariant by mutableStateOf(onBackgroundVariant)
        private set
    var onSurface by mutableStateOf(onSurface)
        private set
    var onError by mutableStateOf(onError)
        private set

    fun copy(
        primary: Color = this.primary,
        primaryVariant: Color = this.primaryVariant,
        secondary: Color = this.secondary,
        secondaryVariant: Color = this.secondaryVariant,
        tertiary: Color = this.tertiary,
        tertiaryVariant: Color = this.tertiaryVariant,
        background: Color = this.background,
        backgroundVariant: Color = this.backgroundVariant,
        surface: Color = this.surface,
        error: Color = this.error,
        onPrimary: Color = this.onPrimary,
        onPrimaryVariant: Color = this.onPrimaryVariant,
        onSecondary: Color = this.onSecondary,
        onSecondaryVariant: Color = this.onSecondaryVariant,
        onTertiary: Color = this.onTertiary,
        onTertiaryVariant: Color = this.onTertiaryVariant,
        onBackground: Color = this.onBackground,
        onBackgroundVariant: Color = this.onBackgroundVariant,
        onSurface: Color = this.onSurface,
        onError: Color = this.onError
    ) = CustomThemeColors(
        primary = primary,
        primaryVariant = primaryVariant,
        secondary = secondary,
        secondaryVariant = secondaryVariant,
        tertiary = tertiary,
        tertiaryVariant = tertiaryVariant,
        background = background,
        backgroundVariant = backgroundVariant,
        surface = surface,
        error = error,
        onPrimary = onPrimary,
        onPrimaryVariant = onPrimaryVariant,
        onSecondary = onSecondary,
        onSecondaryVariant = onSecondaryVariant,
        onTertiary = onTertiary,
        onTertiaryVariant = onTertiaryVariant,
        onBackground = onBackground,
        onBackgroundVariant = onBackgroundVariant,
        onSurface = onSurface,
        onError = onError
    )

    fun updateColorsFrom(other: CustomThemeColors) {
        primary = other.primary
        primaryVariant = other.primaryVariant
        secondary = other.secondary
        secondaryVariant = other.secondaryVariant
        tertiary = other.tertiary
        tertiaryVariant = other.tertiaryVariant
        background = other.background
        backgroundVariant = other.backgroundVariant
        surface = other.surface
        error = other.error
        onPrimary = other.onPrimary
        onPrimaryVariant = other.onPrimaryVariant
        onSecondary = other.onSecondary
        onSecondaryVariant = other.onSecondaryVariant
        onTertiary = other.onTertiary
        onTertiaryVariant = other.onTertiaryVariant
        onBackground = other.onBackground
        onBackgroundVariant = other.onBackgroundVariant
        onSurface = other.onSurface
        onError = other.onError
    }
}

val LocalColors = staticCompositionLocalOf { lightColorsPalette }