package com.example.mystressfulldiary.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.mystressfulldiary.data.AppViewModel
import com.example.mystressfulldiary.data.StressCause
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun getDefaultColor(): FloatArray {
    val hsv = floatArrayOf(0f, 0f, 0f)

    android.graphics.Color.colorToHSV(Color.Blue.toArgb(), hsv)

    return hsv;
}

@Composable
fun Dictionary(viewModel: AppViewModel, navController: NavController) {
    val causes = remember { mutableStateListOf<StressCause>() }
    var name by remember { mutableStateOf(""); };
    var hsv by remember {
        val defaultColor = getDefaultColor();
        mutableStateOf(
            Triple(defaultColor[0], defaultColor[1], defaultColor[2])
        );
    };

    LaunchedEffect(Unit) {
        Log.d("color", hsv.toString());
        viewModel
            .causes
            .observeForever { newCauses ->
                causes.addAll(
                    newCauses.subList(causes.size, newCauses.size)
                )
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Text(
            text = "Your own stress dictionary",
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )
        );
        Spacer(modifier = Modifier.height(16.dp).fillMaxWidth());
        Text(
            text = "Define the causes of your stress",
            style = TextStyle(
                fontWeight = FontWeight.Thin,
                fontSize = 16.sp,
            )
        );
        Spacer(modifier = Modifier.height(16.dp).fillMaxWidth());
        Column {
            TextField(
                value = name,
                placeholder = { Text("Stress name") },
                onValueChange = { name = it },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                )
            )
            Box(
                modifier = Modifier.height(8.dp)
            )
            HueBar { hue ->
                hsv = Triple(hue, hsv.second, hsv.third)
            }
            Box(
                modifier = Modifier.height(8.dp)
            )
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    viewModel.addCause(
                        StressCause(
                            name,
                            "${hsv.first}-${hsv.second}-${hsv.third}",
                        )
                    );
                    name = ""
                    val defaultColor = getDefaultColor();
                    hsv = Triple(defaultColor[0], defaultColor[1], defaultColor[2]);
                },
            ) {
                Text("Save")
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())

        ) {
            causes.forEach { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val codes = item.color.split('-').map { v -> v.toFloat() }
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .padding(5.dp)
                                .background(Color.hsv(codes[0], codes[1], codes[2]))
                        )
                        Text(item.name)
                    }
                }
            }
        }
    }
}