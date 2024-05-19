package com.example.mystressfulldiary.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn

class AppViewModel(private val repository: AppRepository): ViewModel() {
    private var _causes = MutableLiveData<List<StressCause>>();
    val causes: LiveData<List<StressCause>> get() = _causes;
    private var _entries = MutableLiveData<List<StressEntry>>();
    val entries: LiveData<List<StressEntry>> get() = _entries;

    init {
        viewModelScope.launch {
            val fetchedCauses = repository.getAllCauses()
            var todayEntries = repository.getEntriesByDate(Clock.System.now().toLocalDateTime(TimeZone.UTC).date)
            // Log.d("test", Clock.System.todayIn(TimeZone.currentSystemDefault()).toString())
            // Log.d("test", Clock.System.todayIn(TimeZone.currentSystemDefault()).toJavaLocalDate().toString())
            Log.d("test",todayEntries.size.toString())
            if (fetchedCauses.isNotEmpty() && todayEntries.isEmpty()) {
                todayEntries = fetchedCauses
                    .map { cause -> StressEntry(cause.name, 0.0f) }
                todayEntries.map() { entry -> async { repository.registerEntry(entry) } }
            }

            _entries.value = todayEntries;
            _causes.value = fetchedCauses;
        }
    }
    /*

    // Function to update the LiveData state
    fun updateState(newValue: String) {
        _causes.value = newValue
    }*/

    /*fun getAllCauses() {
        // return repository.getAllCauses().asLiveData(viewModelScope.coroutineContext)
        _causes.value = repository.getAllCauses().asLiveData()

    }*/
    fun addCause(stressCause: StressCause) {
        viewModelScope.launch {
            repository.addCause(stressCause);
            val newCauses = causes.value.orEmpty().toMutableList()
            newCauses.add(stressCause)
            _causes.value = newCauses;
        }
    }
    suspend fun deleteCause(stressCause: StressCause) {
        repository.deleteCause(stressCause)
    }
    suspend fun registerEntry(stressEntry: StressEntry) {
        repository.registerEntry(stressEntry)
    }
    fun updateEntry(stressEntry: StressEntry) {
        viewModelScope.launch {
            repository.updateEntry(stressEntry)
        }
    }
    fun deleteEntry(stressEntry: StressEntry) {
        viewModelScope.launch {
            repository.deleteEntry(stressEntry)
        }
    }
}
