package com.example.mystressfulldiary.ui.screens.analytics

import com.example.mystressfulldiary.data.AppViewModel
import com.example.mystressfulldiary.data.AppDatabase
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import co.yml.charts.common.extensions.isNotNull
import com.example.downloadfile.DownloadDBSection
import com.example.mystressfulldiary.data.StressCause
import com.example.mystressfulldiary.data.StressEntry

@Composable
fun Analytics (viewModel: AppViewModel, navController: NavController, database: AppDatabase) {
    val causes = remember { mutableStateListOf<StressCause>(); };
    val entries = remember { mutableStateListOf<StressEntry>(); };

    LaunchedEffect(Unit) {
        viewModel
            .causes
            .observeForever { newCauses ->
                causes.clear();
                causes.addAll(newCauses);
            }
        viewModel
            .entries
            .observeForever { newEntries ->
                entries.clear();
                entries.addAll(newEntries);
            }
    }

    if (entries.isNotEmpty() && causes.isNotEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            DaysWithoutStressCalendar(viewModel, causes, entries);
            PieChartSection(viewModel, causes, entries);
            BarChartSection(viewModel, causes, entries);
            DownloadDBSection(viewModel, causes, entries);
        }
    }
}
