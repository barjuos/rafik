package com.rafiq.adhkar.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CustomDhikrDao {
    @Query("SELECT * FROM custom_adhkar ORDER BY createdAt ASC")
    fun observeAll(): Flow<List<CustomDhikr>>

    @Insert
    suspend fun insert(item: CustomDhikr): Long

    @Delete
    suspend fun delete(item: CustomDhikr)
}
