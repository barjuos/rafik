package com.rafiq.adhkar

import android.app.Application
import com.rafiq.adhkar.data.AdhkarRepository
import com.rafiq.adhkar.data.db.AppDatabase
import com.rafiq.adhkar.data.settings.SettingsStore

/**
 * Small manual dependency container. The app has a single graph, so a DI
 * framework would add weight without any benefit.
 */
class RafiqApp : Application() {
    val database: AppDatabase by lazy { AppDatabase.getInstance(this) }
    val repository: AdhkarRepository by lazy { AdhkarRepository(this) }
    val settingsStore: SettingsStore by lazy { SettingsStore(this) }
}
