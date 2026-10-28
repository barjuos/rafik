package com.rafiq.adhkar.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat

private val LightColors = lightColorScheme(
    primary = LightPrimary,
    onPrimary = LightBackground,
    primaryContainer = Color(0xFFDDE3D9),
    onPrimaryContainer = Color(0xFF333A31),
    secondary = LightSecondary,
    onSecondary = LightForeground,
    secondaryContainer = Color(0xFFEFEDEA),
    onSecondaryContainer = LightForeground,
    tertiary = LightAccent,
    onTertiary = LightForeground,
    background = LightBackground,
    onBackground = LightForeground,
    surface = LightCard,
    onSurface = LightForeground,
    surfaceVariant = LightSecondary,
    onSurfaceVariant = LightMutedForeground,
    outline = LightBorder,
    outlineVariant = Color(0xFFEDEAE7),
    error = Color(0xFFC0392B),
    onError = Color.White,
)

private val DarkColors = darkColorScheme(
    primary = DarkPrimary,
    onPrimary = DarkBackground,
    primaryContainer = Color(0xFF3D463A),
    onPrimaryContainer = Color(0xFFD9E2D5),
    secondary = DarkSecondary,
    onSecondary = DarkForeground,
    secondaryContainer = Color(0xFF2E2A28),
    onSecondaryContainer = DarkForeground,
    tertiary = DarkAccent,
    onTertiary = DarkBackground,
    background = DarkBackground,
    onBackground = DarkForeground,
    surface = DarkCard,
    onSurface = DarkForeground,
    surfaceVariant = DarkSecondary,
    onSurfaceVariant = DarkMutedForeground,
    outline = DarkBorder,
    outlineVariant = Color(0xFF2E2A28),
    error = Color(0xFFCF6679),
    onError = Color(0xFF1A1110),
)

private val RafiqShapes = Shapes(
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(14.dp),
    large = RoundedCornerShape(18.dp),
)

@Composable
fun RafiqTheme(
    themeMode: String = "system",
    content: @Composable () -> Unit,
) {
    val darkTheme = when (themeMode) {
        "light" -> false
        "dark" -> true
        else -> isSystemInDarkTheme()
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = !darkTheme
                isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = RafiqTypography,
        shapes = RafiqShapes,
        content = content,
    )
}
