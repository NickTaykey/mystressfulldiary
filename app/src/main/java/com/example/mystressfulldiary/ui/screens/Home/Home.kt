package com.example.mystressfulldiary.ui.screens.Home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.boguszpawlowski.composecalendar.StaticCalendar

@Composable
fun Home() {
    Text("Hello")
    StaticCalendar(
        dayContent = { dayState ->
            /*Box(
            modifier = Modifier
                .background(Color.Blue)
                .fillMaxWidth()
                .padding(2.dp)
                .fillMaxHeight()
            ) {*/
            Box(modifier = Modifier.background(Color.Blue))
            //}
        })
}