package com.example.mystressfulldiary.data

import androidx.room.Insert
import androidx.room.Update
import androidx.room.Query
import androidx.room.Dao
import androidx.room.Delete
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn

@Dao
interface StressEntryDao {
    @Insert
    suspend fun insert(cause: StressEntry)

    @Update
    suspend fun update(cause: StressEntry)

    @Delete
    suspend fun delete(cause: StressEntry)

    // for a time frame get days without stress cause
    @Query("SELECT * FROM `stress-entries` WHERE date = :date")
    suspend fun getEntriesByDate(date: LocalDate): List<StressEntry>

    // for a time frame get days without stress cause
    @Query("SELECT date FROM `stress-entries` WHERE date >= :fromDate AND date <= :toDate  EXCEPT SELECT date FROM `stress-entries` WHERE cause <> :cause")
    suspend fun getStressCauseFreeDays(cause: String, fromDate: LocalDate, toDate: LocalDate): List<LocalDate>

    // for a time frame get stress cause Pie Chart
    /*@Query("""
        SELECT
            COUNT(*) * 100.0 / (
                SELECT COUNT(*) FROM `stress-entries` WHERE date >= :fromDate AND date <= :toDate
            ) AS percentage
        FROM `stress-entries`
        WHERE date >= :fromDate AND date <= :toDate
        GROUP BY cause
    """)
    suspend fun getStressPieChart(fromDate: LocalDate, toDate: LocalDate): List<Double>*/

    // get stress cause Bar Charts data for a given time frame
    @Query("""
        SELECT * FROM `stress-entries` WHERE cause = :cause AND date >= :fromDate AND date <= :toDate GROUP BY date
    """)
    suspend fun getStressEntriesByCauseAndTime(cause: String, fromDate: LocalDate, toDate: LocalDate): List<StressEntry>


    // get stress Bar Chart data for a given time frame
    @Query("""
        SELECT * FROM `stress-entries` WHERE date >= :fromDate AND date <= :toDate GROUP BY date
    """)
    suspend fun getStressEntriesByTime(fromDate: LocalDate, toDate: LocalDate): List<StressEntry>
}