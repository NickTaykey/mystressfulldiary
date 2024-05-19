package com.example.mystressfulldiary.data

import androidx.room.Insert
import androidx.room.Update
import androidx.room.Query
import androidx.room.Dao
import kotlinx.coroutines.flow.Flow

@Dao
interface StressCauseDao {
    @Query("SELECT * FROM `stress-causes`")
    suspend fun getAll(): List<StressCause>

    @Insert
    suspend fun insert(cause: StressCause)

    @Update
    suspend fun update(cause: StressCause)
}