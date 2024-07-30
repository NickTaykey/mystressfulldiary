package com.example.mystressfulldiary.ui.screens.analytics

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DateRangePicker(
    startDateCb: (day: Int, month: Int, year: Int) -> Unit,
    endDateCb: (day: Int, month: Int, year: Int) -> Unit,
) {
    Column {
        Spacer(modifier = Modifier.height(16.dp).fillMaxWidth());
        Row {
            Column {
                Text("Start date");
                DatePicker(startDateCb);
            }
            Column {
                Text("End date");
                DatePicker(endDateCb);
            }
        }
        Spacer(modifier = Modifier.height(16.dp).fillMaxWidth());
    }
}