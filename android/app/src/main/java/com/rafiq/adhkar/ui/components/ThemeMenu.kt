package com.rafiq.adhkar.ui.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun ThemeMenu(
    currentTheme: String,
    onThemeChange: (String) -> Unit,
) {
    val systemDark = isSystemInDarkTheme()
    val isDark = when (currentTheme) {
        "dark" -> true
        "light" -> false
        else -> systemDark // system: reflect the actual current appearance
    }

    IconButton(onClick = {
        onThemeChange(if (isDark) "light" else "dark")
    }) {
        Icon(
            imageVector = if (isDark) {
                Icons.Outlined.LightMode
            } else {
                Icons.Outlined.DarkMode
            },
            contentDescription = if (isDark) "التبديل إلى المظهر المضيء" else "التبديل إلى المظهر المظلم",
            tint = MaterialTheme.colorScheme.onBackground,
        )
    }
}