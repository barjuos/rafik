package com.rafiq.adhkar

import com.rafiq.adhkar.data.model.Dhikr
import java.io.File
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AdhkarAssetTest {

    private val file = File("src/main/assets/adhkar.json")

    @Test
    fun `asset exists`() {
        assertTrue("Missing ${file.absolutePath}", file.exists())
    }

    @Test
    fun `asset contains all adhkar with valid fields`() {
        val list = Json { ignoreUnknownKeys = true }
            .decodeFromString<List<Dhikr>>(file.readText())

        assertEquals(115, list.size)
        assertEquals(13, list.map { it.category }.distinct().size)
        assertTrue(list.all { it.content.isNotBlank() })
        assertTrue(list.all { it.count >= 1 })
        assertEquals(115, list.map { it.key }.distinct().size)
    }

    @Test
    fun `every home category has content`() {
        val list = Json { ignoreUnknownKeys = true }
            .decodeFromString<List<Dhikr>>(file.readText())
        val expected = listOf(
            "morning", "evening", "sleep", "after-prayer", "home", "food",
            "mosque", "travel", "clothing", "toilet", "relief", "rizq", "advice",
        )
        expected.forEach { category ->
            assertTrue("No adhkar for $category", list.any { it.category == category })
        }
    }
}
