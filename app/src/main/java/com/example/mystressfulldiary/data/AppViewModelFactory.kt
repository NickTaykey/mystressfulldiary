package com.example.mystressfulldiary.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory(private val appRepository: AppRepository) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AppViewModel::class.java)) {
            return AppViewModel(appRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
