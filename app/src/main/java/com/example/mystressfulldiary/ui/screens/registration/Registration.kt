package com.example.mystressfulldiary.ui.screens.registration

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.mystressfulldiary.data.AppViewModel
import com.example.mystressfulldiary.data.StressCause
import com.example.mystressfulldiary.data.StressEntry

@Composable
fun Registration(viewModel: AppViewModel) {
    val causes = remember { mutableStateListOf<StressCause>() }
    val entries = remember { mutableStateListOf<StressEntry>() }

    LaunchedEffect(Unit) {
        viewModel
            .causes
            .observeForever { newCauses ->
                causes.addAll(
                    newCauses.subList(causes.size, newCauses.size)
                )
            }
        viewModel
            .entries
            .observeForever { newEntries ->
                entries.addAll(
                    newEntries.subList(entries.size, newEntries.size)
                )
            }
    }

    if (causes.size > 0) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "How do you feel today?",
                style = MaterialTheme.typography.headlineLarge,
            )
            Text(entries.size.toString())
            Spacer(modifier = Modifier.height(8.dp))
            val stressLabels = mapOf(
                0 to "No stress",
                1 to "Light stress",
                2 to "Moderate stress",
                3 to "High stress (bearable)",
                4 to "Extreme stress (unbearable)"
            )
            val stressColors = mapOf(
                0 to Color(92, 235, 52),
                1 to Color(235, 211, 52),
                2 to Color(235, 174, 52),
                3 to Color(235, 153, 52),
                4 to Color(235, 79, 52),
            )
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    entries.forEach { entry ->
                        var selectedValue by remember {
                            mutableFloatStateOf(entry.intensity)
                        }
                        OutlinedCard(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface,
                            ),
                            border = BorderStroke(1.dp, Color.Black),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp) // Inner padding for the entire content inside the Card
                            ) {
                                Text(
                                    text = entry.cause,
                                    style = MaterialTheme.typography.displaySmall
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                OutlinedCard(
                                    colors = CardDefaults.cardColors(
                                        containerColor = stressColors[selectedValue.toInt()]!!
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(32.dp)
                                ) {
                                    Box(
                                        modifier = Modifier.height(32.dp),
                                        contentAlignment = Alignment.Center,
                                    ) {
                                        Text(
                                            text = stressLabels[selectedValue.toInt()]!!,
                                            style = MaterialTheme.typography.titleSmall,
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Slider(
                                    value = selectedValue,
                                    onValueChange = { value ->
                                        selectedValue = value
                                        entry.intensity = value;
                                        viewModel.updateEntry(entry);
                                    },
                                    valueRange = 0f..4f,
                                    steps = 5,
                                    modifier = Modifier.fillMaxWidth(),
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    } else {
        Column(
            modifier = Modifier.fillMaxWidth().fillMaxHeight(),
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Create your stress causes to start tracking your stress",
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}