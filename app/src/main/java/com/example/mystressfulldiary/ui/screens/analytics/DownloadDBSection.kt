package com.example.downloadfile

import android.Manifest
import android.os.Environment
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mystressfulldiary.data.AppDatabase
import com.example.mystressfulldiary.data.AppViewModel
import com.example.mystressfulldiary.data.StressCause
import com.example.mystressfulldiary.data.StressEntry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

@Composable
fun DownloadDBSection(
    viewModel: AppViewModel,
    causes: List<StressCause>,
    entries: List<StressEntry>,
) {
    var hasPermission by remember { mutableStateOf(false) };
    val coroutineScope = rememberCoroutineScope();
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted: Boolean ->
            hasPermission = isGranted
        }
    )

    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    if (hasPermission) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Download you data as CSV",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                )
            );
            Spacer(modifier = Modifier.height(16.dp).fillMaxWidth());
            Text(
                text = "Would you like to do some analytics?",
                style = TextStyle(
                    fontWeight = FontWeight.Thin,
                    fontSize = 16.sp
                )
            );
            Spacer(modifier = Modifier.height(16.dp).fillMaxWidth());
            Button(
                modifier = Modifier.fillMaxSize(),
                onClick = {
                    coroutineScope.launch {
                        writeFileToDownloadFolder(causes, entries);
                    }
                }
            ) {
                Text("Create File")
            }
        }
    } else {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(text = "Requesting storage permission...")
        }
    }
}

fun writeFileToDownloadFolder(causes: List<StressCause>, entries: List<StressEntry>) {
    try {
        val downloadFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);

        if (!downloadFolder.exists()) {
            downloadFolder.mkdirs();
        }

        val causesFile = File(downloadFolder, "causes.csv");
        val entriesFile = File(downloadFolder, "entries.csv");

        if (!entriesFile.exists()) entriesFile.createNewFile()
        if (!causesFile.exists()) causesFile.createNewFile()

        FileOutputStream(causesFile).use { output ->
            val strCauses = causes
                .mapIndexed { i, c -> "${i};${c.name};${c.color}" }
                .joinToString("\n");

            output.write(strCauses.toByteArray());
            output.flush();

            Log.d("causes_csv_saved", "true");
        }

        FileOutputStream(entriesFile).use { output ->
            val strEntries = entries
                .mapIndexed { i, e -> "${i};${e.id};${e.cause};${e.intensity};${e.date};" }
                .joinToString("\n");

            output.write(strEntries.toByteArray());
            output.flush();

            Log.d("entries_csv_saved", "true");
        }
    } catch (e: Exception) {
        Log.d("error_on_file_save", e.toString())
    }
}
