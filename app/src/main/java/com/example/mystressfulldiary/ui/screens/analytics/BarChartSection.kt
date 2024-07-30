package com.example.mystressfulldiary.ui.screens.analytics

import androidx.compose.runtime.Composable
import com.example.mystressfulldiary.data.AppViewModel
import com.example.mystressfulldiary.data.StressCause
import com.example.mystressfulldiary.data.StressEntry

@Composable
fun BarChartSection(viewModel: AppViewModel, causes: List<StressCause>, entries: List<StressEntry>) {
    CauseBarChart(causes, entries);
    CompositionBarChart(causes, entries);
}
