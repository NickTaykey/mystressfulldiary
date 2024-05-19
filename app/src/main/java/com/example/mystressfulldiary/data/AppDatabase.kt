package com.example.mystressfulldiary.data

import android.content.Context
import androidx.room.TypeConverters
import androidx.room.RoomDatabase
import androidx.room.Database
import androidx.room.Room

@Database(
    entities = [StressCause::class, StressEntry::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun stressCauseDao(): StressCauseDao
    abstract fun stressEntryDao(): StressEntryDao

    companion object {
        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "app.db6"
            )
                .addTypeConverter(Converters())
                .build()
        }

        @Volatile
        private var INSTANCE: AppDatabase? = null;

        fun getDatabaseInstance(context: Context): AppDatabase {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = buildDatabase(context)
                }
            }
            return INSTANCE!!
        }
    }
}