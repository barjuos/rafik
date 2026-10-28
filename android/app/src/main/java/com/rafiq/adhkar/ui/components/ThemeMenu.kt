package com.rafiq.adhkar.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun ThemeMenu(
    currentTheme: String,
    onThemeChange: (String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        IconButton(onClick = { expanded = true }) {
            Icon(
                imageVector = Icons.Outlined.DarkMode,
                contentDescription = "المظهر",
                tint = MaterialTheme.colorScheme.onBackground,
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            DropdownMenuItem(
                text = { Text("مضيء") },
                onClick = {
                    onThemeChange("light")
                    expanded = false
                },
            )
            DropdownMenuItem(
                text = { Text("مظلم") },
                onClick = {
                    onThemeChange("dark")
                    expanded = false
                },
            )
            DropdownMenuItem(
                text = { Text("النظام") },
                onClick = {
                    onThemeChange("system")
                    expanded = false
                },
            )
        }
    }
}
