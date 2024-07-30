package com.example.mystressfulldiary.ui.screens.analytics

import android.icu.util.Calendar
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.yml.charts.common.model.PlotType
import co.yml.charts.ui.piechart.charts.PieChart
import co.yml.charts.ui.piechart.models.PieChartConfig
import co.yml.charts.ui.piechart.models.PieChartData
import com.example.mystressfulldiary.data.AppViewModel
import com.example.mystressfulldiary.data.PieChartDataObject
import com.example.mystressfulldiary.data.StressCause
import com.example.mystressfulldiary.data.StressEntry
import com.example.mystressfulldiary.data.calculatePieChartPercentages
import kotlinx.datetime.LocalDate


@Composable
fun PieChartSection(viewModel: AppViewModel, causes: List<StressCause>, entries: List<StressEntry>) {
    val startDate = remember {
        mutableStateOf(Triple(Calendar.YEAR, Calendar.MONTH + 1, Calendar.DAY_OF_MONTH));
    };
    val endDate = remember {
        mutableStateOf(Triple(Calendar.YEAR, Calendar.MONTH + 1, Calendar.DAY_OF_MONTH));
    };
    val percentages = remember {
        mutableListOf<PieChartDataObject>();
    };

    DateRangePicker(
        startDateCb = { day, month, year -> startDate.value = Triple(year, month, day) },
        endDateCb = { day, month, year -> endDate.value = Triple(year, month, day) }
    );

    LaunchedEffect(startDate.value, endDate.value, entries.size) {
        percentages.clear();
        percentages.addAll(
            calculatePieChartPercentages(
                entries,
                causes,
                LocalDate(startDate.value.first, startDate.value.second, startDate.value.third),
                LocalDate(endDate.value.first, endDate.value.second, endDate.value.third)
            )
        );
    }

    if (percentages.isNotEmpty()) {
        val pieChartData = PieChartData(
            slices = percentages.map { p -> PieChartData.Slice(p.causeName, p.value, p.color) },
            plotType = PlotType.Pie
        );

        val pieChartConfig = PieChartConfig(
            isAnimationEnable = true,
            showSliceLabels = true,
            animationDuration = 1500
        );

        Text(
            text = "Stress Pie",
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
            )
        );
        Spacer(modifier = Modifier.height(16.dp).fillMaxWidth());
        Text(
            text = "Looking for the composition of your stress?",
            style = TextStyle(
                fontWeight = FontWeight.Thin,
                fontSize = 16.sp
            )
        );

        PieChart(
            modifier = Modifier
                .width(400.dp)
                .height(400.dp),
            pieChartData,
            pieChartConfig
        )
    }
}

