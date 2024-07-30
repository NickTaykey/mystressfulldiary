package com.example.mystressfulldiary.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class AppViewModel(private val repository: AppRepository): ViewModel() {
    private var _causes = MutableLiveData<List<StressCause>>(emptyList());
    val causes: LiveData<List<StressCause>> get() = _causes;

    private var _todaysEntries = MutableLiveData<List<StressEntry>>(emptyList());
    val todaysEntries: LiveData<List<StressEntry>> get() = _todaysEntries;

    private var _calendarEntriesAggregation = MutableLiveData<List<StressDay>>(emptyList());
    val calendarEntriesAggregation: LiveData<List<StressDay>> get() = _calendarEntriesAggregation;

    private var _entries = MutableLiveData<List<StressEntry>>(emptyList());
    val entries: LiveData<List<StressEntry>> get() = _entries;

    init {
        viewModelScope.launch {
            val scope = CoroutineScope(Dispatchers.Default)
            val currentMoment = Clock.System.now().toLocalDateTime(TimeZone.UTC);
            val currentDate = LocalDate(currentMoment.date.year, currentMoment.date.month, currentMoment.date.dayOfMonth);

            val fetchedCauses = repository.getAllCauses();
            val fetchedEntries = repository.getAllEntries();

            val todayEntries = fetchedEntries.filter { e -> e.date == currentDate }.toMutableList();

            if (fetchedCauses.isNotEmpty() && todayEntries.isEmpty()) {
                fetchedCauses.forEach { cause ->
                    val entry = StressEntry(cause.name, 0, currentDate);
                    todayEntries += entry;
                    scope.launch {
                        repository.registerEntry(entry)
                    }
                }
            }

            _todaysEntries.value = todayEntries;
            _causes.value = fetchedCauses;
            _entries.value = fetchedEntries;
            _calendarEntriesAggregation.value = repository.aggregateByDateAndSumIntensity();

        }
    }
    fun addCause(stressCause: StressCause) {
        viewModelScope.launch {
            repository.addCause(stressCause);
            val newCauses = causes.value.orEmpty().toMutableList();
            newCauses.add(stressCause);
            _causes.value = newCauses;
        }
    }
    fun updateEntry(stressEntry: StressEntry) {
        viewModelScope.launch {
            repository.updateEntry(stressEntry)
        }
    }

    suspend fun getDaysWithoutStressCause(cause: String): List<StressEntry> {
        return repository.getDaysWithoutStressCause(cause);
    }
}
