package com.rafiq.adhkar.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "custom_adhkar")
data class CustomDhikr(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val content: String,
    val count: Int,
    val description: String = "",
    val reference: String = "",
    val createdAt: Long = System.currentTimeMillis(),
)
