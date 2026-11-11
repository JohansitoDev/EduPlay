package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = PrimaryBlue,
    onPrimary = Color.White,
    primaryContainer = PastelBlue,
    onPrimaryContainer = PrimaryBlueDark,
    secondary = PrimaryGreen,
    onSecondary = Color.White,
    secondaryContainer = PastelGreen,
    tertiary = PrimaryOrange,
    background = AppCreamBackground,
    onBackground = TextDark,
    surface = SurfaceCard,
    onSurface = TextDark,
    surfaceVariant = SurfaceMuted,
    onSurfaceVariant = TextMuted,
    outline = CardBorder
)

@Composable
fun EduPlayTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // For this kids educational design, we use the bright warm playful light scheme
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}
