package com.example.mystressfulldiary.data

import android.util.Log
import io.github.serpro69.kfaker.Faker
import kotlinx.coroutines.coroutineScope
import java.time.YearMonth
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.LocalDate
import kotlin.random.Random

suspend fun seedUtil(
    stressCauseDao: StressCauseDao,
    stressEntryDao: StressEntryDao
) = coroutineScope {
    val year = 2024;
    val createdCauses: MutableList<StressCause> = mutableListOf();

    val faker = Faker();
    val nCausesToCreate = 3;

    var causes: List<StressCause>;
    var entries: List<StressEntry>;

    runBlocking {
        causes = stressCauseDao.getAll();
        entries = stressEntryDao.getAll();
    }

    if (causes.isEmpty() && entries.isEmpty()) {
        for (i in 1..nCausesToCreate) {
            val cause = StressCause(
                faker.address.city(),
                "${(0..360).random()}-1-1"
            );
            createdCauses.add(cause)
            stressCauseDao.insert(cause);
        }

        var days = 0;
        var ce = 0;
        for (month in 1..12) {
            val yearMonth = YearMonth.of(year, month)
            for (day in 1..yearMonth.lengthOfMonth()) {
                for (cause in createdCauses) {
                    launch {
                        stressEntryDao.insert(
                            StressEntry(
                                cause.name,
                                faker.random.nextInt(0, 4),
                                date = LocalDate(2024, month, day)
                            )
                        );
                    }
                    ce++;
                }
                days++;
            }
        }
    }
}