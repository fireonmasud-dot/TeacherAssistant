package com.teacherassistant.app.ui.escapechecker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teacherassistant.app.data.local.entity.Student
import com.teacherassistant.app.domain.repository.StudentRepository
import com.teacherassistant.app.domain.repository.TrackingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EscapeCheckerViewModel @Inject constructor(
    private val studentRepository: StudentRepository,
    private val trackingRepository: TrackingRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<EscapeCheckerUiState>(EscapeCheckerUiState.Loading)
    val uiState: StateFlow<EscapeCheckerUiState> = _uiState.asStateFlow()

    init {
        loadMonthlyEscapeData()
    }

    private fun loadMonthlyEscapeData() {
        viewModelScope.launch {
            try {
                // Fetch all students
                val students = studentRepository.getAllActiveStudents().firstOrNull() ?: emptyList()
                val escapeDataList = mutableListOf<EscapeData>()
                
                for (student in students) {
                    val escapeCount = trackingRepository.getTiffinEscapeCount(student.id).firstOrNull() ?: 0
                    if (escapeCount > 0) {
                        escapeDataList.add(EscapeData(student, escapeCount))
                    }
                }
                
                // Sort by count descending (highest escapers first)
                escapeDataList.sortByDescending { it.escapeCount }
                
                if (escapeDataList.isEmpty()) {
                    _uiState.value = EscapeCheckerUiState.Empty
                } else {
                    _uiState.value = EscapeCheckerUiState.Success(escapeDataList)
                }

            } catch (e: Exception) {
                _uiState.value = EscapeCheckerUiState.Error(e.message ?: "An error occurred")
            }
        }
    }
}

data class EscapeData(
    val student: Student,
    val escapeCount: Int
)

sealed class EscapeCheckerUiState {
    object Loading : EscapeCheckerUiState()
    object Empty : EscapeCheckerUiState()
    data class Success(val escapeDataList: List<EscapeData>) : EscapeCheckerUiState()
    data class Error(val message: String) : EscapeCheckerUiState()
}
