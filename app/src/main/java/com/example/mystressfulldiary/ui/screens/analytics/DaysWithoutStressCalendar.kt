package com.example.mystressfulldiary.ui.screens.analytics

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.yml.charts.common.extensions.isNotNull
import com.example.mystressfulldiary.data.AppViewModel
import com.example.mystressfulldiary.data.StressCause
import com.example.mystressfulldiary.data.StressEntry
import io.github.boguszpawlowski.composecalendar.StaticCalendar
import io.github.boguszpawlowski.composecalendar.rememberCalendarState
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toKotlinLocalDate

@Composable
fun DaysWithoutStressCalendar(viewModel: AppViewModel, causes: List<StressCause>, entries: List<StressEntry>) {
    val calendarState = rememberCalendarState();
    var selectedCause by remember { mutableStateOf(causes[0]); };
    val context = LocalContext.current;

    val days = remember { mutableSetOf<LocalDate>(); };

    LaunchedEffect(selectedCause) {
        if (causes.isNotEmpty() && selectedCause.isNotNull()) {
            days.clear();
            days.addAll(
                viewModel.getDaysWithoutStressCause(selectedCause.name).map { entry -> entry.date }
            );
        }
    }

    if (causes.isNotEmpty()) {
        Column {
            Text(
                text = "Stress-free days",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                )
            );
            Spacer(modifier = Modifier.height(16.dp).fillMaxWidth());
            Text(
                text = "Find the days when you have not been stressed about one thing?",
                style = TextStyle(
                    fontWeight = FontWeight.Thin,
                    fontSize = 16.sp
                )
            );
            StaticCalendar(
                calendarState = calendarState,
                dayContent = { dayState ->
                    var color = Color.White;
                    if (dayState.date.toKotlinLocalDate() in days) {
                        color = Color.Red;
                    }
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = color,
                        ),
                        modifier = Modifier
                            .size(width = 240.dp, height = 100.dp)
                            .padding(2.dp)
                    ) {
                        Text(
                            text = dayState.date.dayOfMonth.toString(),
                            style = MaterialTheme.typography.headlineLarge,
                        )
                    }
                }
            )
            if (selectedCause.isNotNull()) {
                CausesDropDownMenu(
                    selectedCause,
                    onChangeCause = { newCause -> selectedCause = newCause },
                    causes,
                    context
                );
            }

        }
    }
}
