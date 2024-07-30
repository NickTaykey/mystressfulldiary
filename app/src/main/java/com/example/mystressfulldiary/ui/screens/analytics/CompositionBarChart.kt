package com.example.mystressfulldiary.ui.screens.analytics

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mystressfulldiary.data.StressCause
import com.example.mystressfulldiary.data.StressEntry
import com.mahmoud.composecharts.columnchart.ColumnChart
import com.mahmoud.composecharts.columnchart.SeriesData

@Composable
fun CompositionBarChart(causes: List<StressCause>, entries: List<StressEntry>) {
    var startIndex by remember { mutableIntStateOf(0); };
    var endIndex by remember { mutableIntStateOf(3); };
    val colorsMap = mutableMapOf<String, Color>();

    causes.forEach { cause ->
        val (a, b, c) = cause.color.split('-');
        colorsMap[cause.name] = Color.hsv(a.toFloat(), b.toFloat(), c.toFloat());
    };

    val entriesGroupedByCause = entries.groupBy { it.cause }.toMutableMap();

    val columnChartSeriesData = causes.map {
        SeriesData(
            entriesGroupedByCause[it.name]!!.subList(startIndex, endIndex).map { entry -> entry.intensity.toFloat() },
            colorsMap[it.name]!!,
            it.name
        );
    }


    Text(
        text = "Daily stress composition",
        style = TextStyle(
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
        )
    );
    Spacer(modifier = Modifier.height(16.dp).fillMaxWidth());
    Text(
        text = "Looking for the daily composition of the stress?",
        style = TextStyle(
            fontWeight = FontWeight.Thin,
            fontSize = 16.sp
        )
    );
    Spacer(modifier = Modifier.height(16.dp).fillMaxWidth());

    ColumnChart(
        seriesData = columnChartSeriesData,
        categories = emptyList(),
    );

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 4.dp)
    ) {
        Button(
            onClick = {
                if (startIndex - 3 >= 0) {
                    startIndex -= 3;
                    endIndex -= 3;
                }
            },
            modifier = Modifier.weight(1f)
        ) {
            Text("<");
        };
        Button(
            onClick = {
                if (endIndex + 3 <= entries.size) {
                    startIndex += 3;
                    endIndex += 3;
                }
            },
            modifier = Modifier.weight(1f)
        ) {
            Text(">");
        }
    };
    Spacer(modifier = Modifier.height(16.dp).fillMaxWidth());
}

