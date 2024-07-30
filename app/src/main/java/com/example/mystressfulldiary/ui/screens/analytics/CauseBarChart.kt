package com.example.mystressfulldiary.ui.screens.analytics

import android.util.Log
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.yml.charts.common.extensions.isNotNull
import com.example.mystressfulldiary.data.StressCause
import com.example.mystressfulldiary.data.StressEntry
import com.example.mystressfulldiary.data.calculateBarChartData
import me.bytebeats.views.charts.bar.BarChart
import me.bytebeats.views.charts.bar.BarChartData
import me.bytebeats.views.charts.bar.render.bar.SimpleBarDrawer
import me.bytebeats.views.charts.bar.render.label.SimpleLabelDrawer
import me.bytebeats.views.charts.bar.render.xaxis.SimpleXAxisDrawer
import me.bytebeats.views.charts.bar.render.yaxis.SimpleYAxisDrawer
import me.bytebeats.views.charts.simpleChartAnimation
import kotlin.math.min

@Composable
fun CauseBarChart(causes: List<StressCause>, entries: List<StressEntry>) {
    val bars = remember { mutableListOf<BarChartData.Bar>(); };
    var startIndex by remember { mutableIntStateOf(0); };
    var endIndex by remember { mutableIntStateOf(0); };
    var selectedCause by remember { mutableStateOf(causes[0]); };
    val context = LocalContext.current;

    if (selectedCause.isNotNull()) {
        CausesDropDownMenu(
            selectedCause,
            onChangeCause = { newCause ->
                selectedCause = newCause
            },
            causes,
            context
        );
    }

    LaunchedEffect(entries.size, selectedCause) {
        if (selectedCause.isNotNull()) {
            bars.clear();
            bars.addAll(
                calculateBarChartData(
                    entries,
                    selectedCause,
                )
            );
            startIndex = 0;
            endIndex = min(30, bars.size);
        }
    }

    Text(
        text = "Stress cause trend",
        style = TextStyle(
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
        )
    );
    Spacer(modifier = Modifier.height(16.dp).fillMaxWidth());
    Text(
        text = "Look at the trend of a streess cause?",
        style = TextStyle(
            fontWeight = FontWeight.Thin,
            fontSize = 16.sp
        )
    );
    Spacer(modifier = Modifier.height(16.dp).fillMaxWidth());

    if (bars.isNotEmpty()) {
        BarChart(
            barChartData = BarChartData(
                bars = bars.subList(startIndex, endIndex).toList()
            ),
            modifier = Modifier.fillMaxWidth().height(400.dp),
            animation = simpleChartAnimation(),
            barDrawer = SimpleBarDrawer(),
            xAxisDrawer = SimpleXAxisDrawer(),
            yAxisDrawer = SimpleYAxisDrawer(),
            labelDrawer = SimpleLabelDrawer(SimpleLabelDrawer.DrawLocation.Outside)
        );
        Row(
            modifier = Modifier.fillMaxWidth().padding(0.dp, 4.dp)
        ) {
            Button(
                onClick = {
                    if (startIndex - 30 >= 0) {
                        startIndex -= 30;
                        endIndex -= 30;
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("<");
            };
            Button(
                onClick = {
                    if (endIndex + 30 <= bars.size) {
                        startIndex += 30;
                        endIndex += 30;
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text(">");
            }
        };
        Spacer(modifier = Modifier.height(16.dp).fillMaxWidth());
    }
}