package com.autoclicker.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

/**
 * Material 3 dark colour scheme built from the app's custom colour palette.
 * Maps the cyan/teal primary palette, red error colours, and dark surface
 * colours defined in Color.kt onto Material 3 semantic colour roles.
 */
private val DarkColorScheme = darkColorScheme(
    primary = Cyan40,
    onPrimary = DarkBg,
    primaryContainer = DarkSurfaceVariant,
    onPrimaryContainer = CyanAccent,
    secondary = Teal40,
    onSecondary = DarkBg,
    secondaryContainer = DarkSurfaceVariant,
    onSecondaryContainer = Teal80,
    tertiary = CyanAccent,
    error = RedAccent,
    onError = DarkBg,
    errorContainer = RedDark,
    background = DarkBg,
    onBackground = TextPrimary,
    surface = DarkSurface,
    onSurface = TextPrimary,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = TextSecondary,
    outline = TextMuted,
    outlineVariant = DarkSurfaceVariant
)

/**
 * App-wide Compose theme wrapper.
 *
 * Applies the dark colour scheme and custom [Typography] to all descendant
 * composables. The app uses a dark-only theme — no light variant is provided.
 */
@Composable
fun AutoClickerTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}

