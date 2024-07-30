package com.example.mystressfulldiary.data

import androidx.room.Insert
import androidx.room.Update
import androidx.room.Query
import androidx.room.Dao
import androidx.room.Delete
import kotlinx.datetime.LocalDate

@Dao
interface StressEntryDao {
    @Insert
    suspend fun insert(cause: StressEntry)

    @Update
    suspend fun update(cause: StressEntry)

    @Delete
    suspend fun delete(cause: StressEntry)

    @Query("SELECT * FROM `stress-entries` WHERE date = :date")
    suspend fun getEntriesByDate(date: LocalDate): List<StressEntry>

    @Query("SELECT date as date, sum(intensity) as intensity FROM `stress-entries` GROUP BY date")
    suspend fun aggregateByDateAndSumIntensity(): List<StressDay>

    @Query("SELECT * FROM `stress-entries` WHERE date NOT IN (SELECT date FROM `stress-entries` WHERE cause = :cause)")
    suspend fun getDaysWithoutStressCause(cause: String): List<StressEntry>

    @Query("DELETE FROM `stress-entries`")
    suspend fun deleteAll();

    @Query("SELECT * FROM `stress-entries` ORDER BY date")
    suspend fun getAll(): List<StressEntry>;
}