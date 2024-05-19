package com.example.mystressfulldiary.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn

@Entity(
    tableName = "stress-entries",
    foreignKeys = [
        ForeignKey(
            entity = StressCause::class,
            parentColumns = ["name"],
            childColumns = ["cause"],
            onDelete = ForeignKey.CASCADE
        ),
    ]
)

data class StressEntry(
    val cause: String,
    var intensity: Float,
    val date: LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault()),
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
)