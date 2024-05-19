package com.example.mystressfulldiary.data

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime

@ProvidedTypeConverter
class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): LocalDate? {
        return value?.let { Instant.fromEpochMilliseconds(value).toLocalDateTime(TimeZone.UTC).date }
    }
    @TypeConverter
    fun dateToTimestamp(date: LocalDate?): Long? {
        return date?.atStartOfDayIn(TimeZone.UTC)?.toEpochMilliseconds()
    }
}