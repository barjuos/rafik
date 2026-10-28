package com.rafiq.adhkar.data.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.rafiqDataStore: DataStore<Preferences> by preferencesDataStore(name = "rafiq_settings")

class SettingsStore(private val context: Context) {
    private val themeKey = stringPreferencesKey("theme")

    val theme: Flow<String> = context.rafiqDataStore.data.map { preferences ->
        preferences[themeKey] ?: "system"
    }

    suspend fun setTheme(value: String) {
        context.rafiqDataStore.edit { preferences ->
            preferences[themeKey] = value
        }
    }
}
