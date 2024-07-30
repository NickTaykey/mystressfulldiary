package com.example.mystressfulldiary.data

import kotlinx.datetime.LocalDate

class AppRepository(private val db: AppDatabase) {
    suspend fun getAllCauses() = db.stressCauseDao().getAll()
    suspend fun getAllEntries() = db.stressEntryDao().getAll()

    suspend fun addCause(stressCause: StressCause) {
        db.stressCauseDao().insert(stressCause);
    }
    suspend fun registerEntry(stressEntry: StressEntry) {
        db.stressEntryDao().insert(stressEntry)
    }
    suspend fun updateEntry(stressEntry: StressEntry) {
        db.stressEntryDao().update(stressEntry)
    }
    suspend fun getEntriesByDate(date: LocalDate) = db.stressEntryDao().getEntriesByDate(date);
    suspend fun aggregateByDateAndSumIntensity() = db.stressEntryDao().aggregateByDateAndSumIntensity();
    suspend fun getDaysWithoutStressCause(cause: String) = db.stressEntryDao().getDaysWithoutStressCause(cause);
}
