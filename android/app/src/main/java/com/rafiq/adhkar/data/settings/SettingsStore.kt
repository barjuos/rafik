package com.rafiq.adhkar.data.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
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

    // ---------------------------------------------------------------- bookmarks

    fun getBookmark(surahId: String): Flow<Int> = context.rafiqDataStore.data.map { preferences ->
        preferences[intPreferencesKey("bookmark_$surahId")] ?: -1
    }

    suspend fun saveBookmark(surahId: String, verseIndex: Int) {
        context.rafiqDataStore.edit { preferences ->
            preferences[intPreferencesKey("bookmark_$surahId")] = verseIndex
        }
    }

    // ------------------------------------------------- automatic reading position

    fun getReadingPosition(surahId: String): Flow<Int> = context.rafiqDataStore.data.map { preferences ->
        preferences[intPreferencesKey("reading_$surahId")] ?: -1
    }

    suspend fun saveReadingPosition(surahId: String, verseIndex: Int) {
        context.rafiqDataStore.edit { preferences ->
            preferences[intPreferencesKey("reading_$surahId")] = verseIndex
        }
    }

    // ------------------------------------------------------------- reader font size

    private val readerFontSizeKey = intPreferencesKey("reader_font_size")

    val readerFontSize: Flow<Int> = context.rafiqDataStore.data.map { preferences ->
        preferences[readerFontSizeKey] ?: com.rafiq.adhkar.ui.reader.DefaultReaderFontSize
    }

    suspend fun setReaderFontSize(value: Int) {
        context.rafiqDataStore.edit { preferences ->
            preferences[readerFontSizeKey] = value
        }
    }

    // ----------------------------------------------------------- tracker progress

    fun getTrackerProgress(key: String): Flow<TrackerProgress> = context.rafiqDataStore.data.map { p ->
        TrackerProgress(
            index = p[intPreferencesKey("tracker_index_$key")] ?: 0,
            count = p[intPreferencesKey("tracker_count_$key")] ?: 0,
        )
    }

    suspend fun saveTrackerProgress(key: String, index: Int, count: Int) {
        context.rafiqDataStore.edit { preferences ->
            preferences[intPreferencesKey("tracker_index_$key")] = index
            preferences[intPreferencesKey("tracker_count_$key")] = count
        }
    }

    suspend fun clearTrackerProgress(key: String) {
        context.rafiqDataStore.edit { preferences ->
            preferences.remove(intPreferencesKey("tracker_index_$key"))
            preferences.remove(intPreferencesKey("tracker_count_$key"))
        }
    }
}