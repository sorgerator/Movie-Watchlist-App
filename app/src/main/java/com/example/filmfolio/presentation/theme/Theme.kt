package com.example.filmfolio.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = OffWhite,
    onPrimary = Black,
    secondary = Silver,
    onSecondary = Black,
    tertiary = LightGray,
    background = Black,
    onBackground = OffWhite,
    surface = DarkGray,
    onSurface = OffWhite,
    surfaceVariant = MediumGray,
    onSurfaceVariant = OffWhite,
    error = ErrorRed
)

private val LightColorScheme = lightColorScheme(
    primary = Black,
    onPrimary = OffWhite,
    secondary = MediumGray,
    onSecondary = OffWhite,
    tertiary = LightGray,
    background = OffWhite,
    onBackground = Black,
    surface = Color.White,
    onSurface = Black,
    surfaceVariant = Color(0xFFEEEEEE),
    onSurfaceVariant = Black,
    error = Color(0xFFB00020)
)

@Composable
fun FilmFolioTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // We force a dark-themed bold look if requested, but let's respect system for now
    // or just make it always dark for that "bold, modern" feel the user described.
    // Given the user asked for black, gray, offwhite, let's prioritize the dark scheme.
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
