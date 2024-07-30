package com.example.mystressfulldiary.ui.screens.Home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mystressfulldiary.data.AppViewModel
import com.example.mystressfulldiary.data.StressDay
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Regular
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.regular.ChartBar
import compose.icons.fontawesomeicons.solid.Book
import compose.icons.fontawesomeicons.solid.PenAlt
import io.github.boguszpawlowski.composecalendar.StaticCalendar
import io.github.boguszpawlowski.composecalendar.rememberCalendarState
import kotlinx.datetime.LocalDate
import kotlinx.datetime.daysUntil

fun getDayColor(intValue: Int, floatValue: Float): Color {
    // Ensure the float value is within the range [0, intValue]
    val clampedFloat = floatValue.coerceIn(0f, intValue.toFloat())

    // Calculate the ratio of floatValue to intValue
    val ratio = clampedFloat / intValue

    // Interpolate between green and red
    val red = (255 * ratio).toInt()
    val green = (255 * (1 - ratio)).toInt()

    // Return the color
    return Color(red, green, 0)
}

@Composable
fun Home(viewModel: AppViewModel, navController: NavController) {
    val calendarState = rememberCalendarState();
    val currentMonthEntries = remember { mutableStateOf<List<StressDay>>(emptyList()) };
    val nCauses = remember { mutableIntStateOf(0) };
    val scrollState = rememberScrollState();

    LaunchedEffect(Unit) {
        viewModel
            .calendarEntriesAggregation
            .observeForever { newEntries -> currentMonthEntries.value = newEntries; }
        viewModel
            .causes
            .observeForever { causes -> nCauses.intValue = causes.size; }
    }

    Column(modifier = Modifier.padding(16.dp).verticalScroll(scrollState)) {
        Text(
            text = "Stress Calendar",
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
            )
        );
        Spacer(modifier = Modifier.height(16.dp).fillMaxWidth());
        Text(
            text = "Monitor and manage your stress levels daily with our intuitive calendar feature!",
            style = TextStyle(
                fontWeight = FontWeight.Thin,
                fontSize = 16.sp
            )
        );
        Column {
            StaticCalendar(
                calendarState = calendarState,
                dayContent = { dayState ->
                    if (currentMonthEntries.value.isNotEmpty() && nCauses.intValue > 0) {
                        val firstRegistrationDate = currentMonthEntries.value[0].date;
                        val lastRegistrationDate = currentMonthEntries.value.last().date;

                        val currentYear = dayState.date.year;
                        val currentMonth = dayState.date.monthValue;
                        val currentDay = dayState.date.dayOfMonth;

                        val paddedMonth = currentMonth.toString().padStart(2, '0');
                        val paddedDay = currentDay.toString().padStart(2,  '0');

                        val currentRegistrationDate = LocalDate.parse(
                            "${currentYear}-${paddedMonth}-${paddedDay}"
                        );

                        var color = Color.White;

                        if (currentRegistrationDate in firstRegistrationDate..lastRegistrationDate) {
                            val currentRegistrationIdx = firstRegistrationDate.daysUntil(currentRegistrationDate);
                            color = getDayColor(
                                nCauses.intValue,
                                currentMonthEntries.value[currentRegistrationIdx].intensity
                            );
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
                }
            );
            Row(modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = {
                        navController.navigate("analytics")
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(245, 66, 212),
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .padding(4.dp)
                ) {
                    Icon(
                        imageVector = FontAwesomeIcons.Regular.ChartBar,
                        contentDescription = "Analytics page",
                        modifier = Modifier.size(18.dp)
                    );
                }
                Button(
                    onClick = {
                        navController.navigate("dictionary")
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(245, 114, 66),
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .padding(4.dp)
                ) {
                    Icon(
                        imageVector = FontAwesomeIcons.Solid.Book,
                        contentDescription = "Dictionary page",
                        modifier = Modifier.size(18.dp)
                    );
                }
            }
            Button(
                onClick = {
                    navController.navigate("registration")
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(66, 69, 245),
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            ) {
                Icon(
                    imageVector = FontAwesomeIcons.Solid.PenAlt,
                    contentDescription = "Registration page",
                    modifier = Modifier.size(18.dp)
                );
            }
        }
    }
}