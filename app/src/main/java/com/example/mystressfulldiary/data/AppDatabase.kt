package com.example.mystressfulldiary.data

import android.content.Context
import androidx.room.TypeConverters
import androidx.room.RoomDatabase
import androidx.room.Database
import androidx.room.Room
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

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
                "app.db897dd11"
            )
                .addTypeConverter(Converters())
                .addCallback(DatabaseCallback(context))
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

        private class DatabaseCallback(
            private val context: Context
        ) : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                CoroutineScope(Dispatchers.IO).launch {
                    seedDatabase(context)
                }
            }
        }

        suspend fun seedDatabase(context: Context) = coroutineScope {
            val db = getDatabaseInstance(context);
            val stressCauseDao = db.stressCauseDao()
            val stressEntryDao = db.stressEntryDao()
            seedUtil(stressCauseDao, stressEntryDao)
        }
    }
}