package com.example.mystressfulldiary

import androidx.activity.compose.setContent
import androidx.activity.ComponentActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mystressfulldiary.data.AppDatabase
import com.example.mystressfulldiary.data.AppRepository
import com.example.mystressfulldiary.data.AppViewModel
import com.example.mystressfulldiary.data.ViewModelFactory
import com.example.mystressfulldiary.ui.screens.Dictionary
import com.example.mystressfulldiary.ui.screens.Home.Home
import com.example.mystressfulldiary.ui.screens.analytics.Analytics
import com.example.mystressfulldiary.ui.screens.registration.Registration

class MainActivity : ComponentActivity() {
    private val appViewModel: AppViewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelFactory(
                AppRepository(
                    AppDatabase.getDatabaseInstance(this)
                )
            )
        ).get(AppViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController();
            val database = AppDatabase.getDatabaseInstance(this);
            NavHost(navController = navController, startDestination = "home") {
                composable("analytics") {
                    Analytics(appViewModel, navController, database)
                }
                composable("dictionary") {
                    Dictionary(appViewModel, navController)
                }
                composable("registration") {
                    Registration(appViewModel, navController)
                }
                composable("home") {
                    Home(appViewModel, navController)
                }
            }
        }
    }
}
