package com.example.mystressfulldiary.ui.screens.analytics

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mystressfulldiary.data.StressCause

@Composable
fun CausesDropDownMenu(
    selectedCause: StressCause,
    onChangeCause: (newCause: StressCause) -> Unit,
    causes: List<StressCause>,
    context: Context
) {
    var expanded by remember { mutableStateOf(false); };
    Column {
        Text(
            text = "Select a cause of stress",
            style = TextStyle(
                fontWeight = FontWeight.Normal,
                fontSize = 24.sp,
            )
        );
        Spacer(modifier = Modifier.height(16.dp).fillMaxWidth());
        Button(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(selectedCause.name)
        }
        Spacer(modifier = Modifier.height(16.dp).fillMaxWidth());
        DropdownMenu(
            expanded = expanded,
            modifier = Modifier.fillMaxWidth(),
            onDismissRequest = { expanded = false }
        ) {
            causes.forEach { cause ->
                DropdownMenuItem(
                    text = { Text(cause.name) },
                    onClick = {
                        onChangeCause(cause);
                        Toast.makeText(context, cause.name, Toast.LENGTH_SHORT).show()
                        expanded = false
                    }
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp).fillMaxWidth());
    }
}