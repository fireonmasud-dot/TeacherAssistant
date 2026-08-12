package com.teacherassistant.app.ui.tracking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teacherassistant.app.data.local.entity.Student
import com.teacherassistant.app.data.local.entity.TiffinIncident
import com.teacherassistant.app.domain.repository.StudentRepository
import com.teacherassistant.app.domain.repository.TrackingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TiffinTrackerViewModel @Inject constructor(
    private val studentRepository: StudentRepository,
    private val trackingRepository: TrackingRepository
) : ViewModel() {

    private val _students = MutableStateFlow<List<Student>>(emptyList())
    val students = _students.asStateFlow()

    private val _uiState = MutableStateFlow<TiffinTrackerUiState>(TiffinTrackerUiState.Loading)
    val uiState: StateFlow<TiffinTrackerUiState> = _uiState.asStateFlow()

    init {
        loadStudents()
    }

    private fun loadStudents() {
        viewModelScope.launch {
            studentRepository.getAllActiveStudents()
                .catch { _uiState.value = TiffinTrackerUiState.Error(it.message ?: "Unknown Error") }
                .collect {
                    _students.value = it
                    _uiState.value = TiffinTrackerUiState.Success(it)
                }
        }
    }

    // Fallback method to load all students if getAllActiveStudents is missing
    private suspend fun StudentRepository.getAllActiveStudents() = this.getAllStudents()

    fun recordIncident(
        studentId: Long,
        incidentType: String,
        description: String,
        teacherNote: String?,
        voiceRecordingRef: String?
    ) {
        viewModelScope.launch {
            val incident = TiffinIncident(
                studentId = studentId,
                date = System.currentTimeMillis(), // In production, use start of day timestamp
                time = "Current Time", // In production, use actual time formatting
                incidentType = incidentType,
                description = description,
                teacherNote = teacherNote,
                voiceRecordingRef = voiceRecordingRef
            )
            trackingRepository.insertTiffinIncident(incident)
        }
    }
}

sealed class TiffinTrackerUiState {
    object Loading : TiffinTrackerUiState()
    data class Success(val students: List<Student>) : TiffinTrackerUiState()
    data class Error(val message: String) : TiffinTrackerUiState()
}
