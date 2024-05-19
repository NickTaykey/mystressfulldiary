package com.example.mystressfulldiary.data

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayAt
import kotlinx.datetime.todayIn

class AppRepository(private val db: AppDatabase) {
    suspend fun getAllCauses() = db.stressCauseDao().getAll()

    suspend fun addCause(stressCause: StressCause) {
        db.stressCauseDao().insert(stressCause);
    }
    suspend fun deleteCause(stressCause: StressCause) {
        db.stressCauseDao().insert(stressCause);
    }
    suspend fun registerEntry(stressEntry: StressEntry) {
        db.stressEntryDao().insert(stressEntry)
    }
    suspend fun updateEntry(stressEntry: StressEntry) {
        db.stressEntryDao().update(stressEntry)
    }
    suspend fun deleteEntry(stressEntry: StressEntry) {
        db.stressEntryDao().delete(stressEntry)
    }
    suspend fun getEntriesByDate(date: LocalDate) = db.stressEntryDao().getEntriesByDate(date)
}
