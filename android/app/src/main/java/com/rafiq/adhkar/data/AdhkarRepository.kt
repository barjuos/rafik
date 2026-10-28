package com.rafiq.adhkar.data

import android.content.Context
import com.rafiq.adhkar.data.model.Dhikr
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

/**
 * Reads the bundled adhkar asset. Everything is offline, so this is a one-shot
 * parse on app start.
 */
class AdhkarRepository(private val context: Context) {
    private val json = Json { ignoreUnknownKeys = true }

    suspend fun load(): Map<String, List<Dhikr>> = withContext(Dispatchers.IO) {
        val text = context.assets.open("adhkar.json").bufferedReader().use { it.readText() }
        json.decodeFromString<List<Dhikr>>(text).groupBy { it.category }
    }
}
