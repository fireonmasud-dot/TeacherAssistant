package com.teacherassistant.app.ui.utility

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teacherassistant.app.data.local.entity.Routine
import com.teacherassistant.app.domain.repository.UtilityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClassRoutineViewModel @Inject constructor(
    private val utilityRepository: UtilityRepository
) : ViewModel() {

    private val _selectedDay = MutableStateFlow("Monday")
    val selectedDay = _selectedDay.asStateFlow()

    private val _routines = MutableStateFlow<List<Routine>>(emptyList())
    val routines = _routines.asStateFlow()

    init {
        loadRoutineForDay("Monday")
    }

    fun selectDay(day: String) {
        _selectedDay.value = day
        loadRoutineForDay(day)
    }

    private fun loadRoutineForDay(day: String) {
        viewModelScope.launch {
            utilityRepository.getRoutineForDay(day).collect {
                _routines.value = it
            }
        }
    }

    fun addRoutine(dayOfWeek: String, timeSlot: String, subject: String, note: String?) {
        viewModelScope.launch {
            val routine = Routine(dayOfWeek = dayOfWeek, timeSlot = timeSlot, subject = subject, note = note)
            utilityRepository.insertRoutine(routine)
        }
    }
    
    fun deleteRoutine(routine: Routine) {
        viewModelScope.launch {
            utilityRepository.deleteRoutine(routine)
        }
    }
}
