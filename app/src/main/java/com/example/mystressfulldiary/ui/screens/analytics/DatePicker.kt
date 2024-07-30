package com.example.mystressfulldiary.ui.screens.analytics

import android.util.Log
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import com.chargemap.compose.numberpicker.NumberPicker
import androidx.compose.runtime.remember
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import java.time.YearMonth

@Composable
fun DatePicker(cb: (day: Int, month: Int, year: Int) -> Unit) {
    val currentDate = Clock.System.todayIn(TimeZone.currentSystemDefault())
    val dayValue = remember { mutableIntStateOf(currentDate.dayOfMonth) };
    val monthValue = remember { mutableIntStateOf(currentDate.monthNumber) };
    val yearValue = remember { mutableIntStateOf(currentDate.year) };
    val monthMaxDayValue = remember { mutableIntStateOf(
        YearMonth.of(currentDate.year, monthValue.intValue).lengthOfMonth()
    ) };

    LaunchedEffect(yearValue.intValue, monthValue.intValue) {
        monthMaxDayValue.intValue = YearMonth.of(currentDate.year, monthValue.intValue).lengthOfMonth();
    }

    LaunchedEffect(dayValue.intValue, monthValue.intValue, yearValue.intValue) {
        cb(dayValue.intValue, monthValue.intValue, yearValue.intValue)
    }

    Row {
        NumberPicker(
            value = dayValue.intValue,
            range = 1..monthMaxDayValue.intValue,
            onValueChange = {
                dayValue.intValue = it;
            }
        );
        NumberPicker(
            value = monthValue.intValue,
            range = 1..12,
            onValueChange = {
                monthValue.intValue = it;
            }
        );
        NumberPicker(
            value = yearValue.intValue,
            range = 0..currentDate.year,
            onValueChange = {
                yearValue.intValue = it;
            }
        );
    }
}