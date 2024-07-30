package com.example.mystressfulldiary.data

import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Color
import kotlinx.datetime.LocalDate
import me.bytebeats.views.charts.bar.BarChartData
import java.util.Calendar

fun getTodayTimestampLegacy(): Long {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    return calendar.timeInMillis
}

fun getTomorrowTimestampLegacy(): Long {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DAY_OF_YEAR, 1)
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    return calendar.timeInMillis
}

data class PieChartDataObject(
    val value: Float,
    val color: Color,
    val causeName: String
)

fun calculatePieChartPercentages(
    entries: List<StressEntry>,
    causes: List<StressCause>,
    startDate: LocalDate,
    endDate: LocalDate
): MutableList<PieChartDataObject> {
    val causeToCumulativeIntensity = entries
        .filter { it.date in startDate..endDate }
        .groupBy { it.cause }
        .map { e -> e.value.sumOf { it.intensity.toDouble() } }

    val totalIntensity = causeToCumulativeIntensity.sum();
    var totPercentage = 0f;
    val percentages = causeToCumulativeIntensity
        .mapIndexed { idx, int ->
            val percentage = ((int.toFloat() / totalIntensity) * 100.0f).toFloat();
            val codes = causes[idx].color
                .split('-')
                .map { v -> v.toFloat() }
            totPercentage += percentage;
            PieChartDataObject(percentage, Color.hsv(codes[0], codes[1], codes[2]), causes[idx].name)
        }
        .toMutableList();

    percentages += PieChartDataObject((100 - totPercentage), Color.Green, "wellness")

    return percentages
}

val stressColors = mapOf(
    0 to Color(92, 235, 52),
    1 to Color(235, 211, 52),
    2 to Color(235, 174, 52),
    3 to Color(235, 153, 52),
    4 to Color(235, 79, 52),
)

fun calculateBarChartData(
    entries: List<StressEntry>,
    cause: StressCause,
): List<BarChartData.Bar> {
    val codes = cause.color.split('-').map { v -> v.toFloat() }
    return entries
        .filter { it.cause == cause.name }
        .mapIndexed { idx, ent ->
            BarChartData.Bar(
                label = ent.date.dayOfMonth.toString(),
                value = ent.intensity * 100f,
                color = Color.hsv(codes[0], codes[1], codes[2])
            )
        }
}