package com.teacherassistant.app.ui.profile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teacherassistant.app.data.local.entity.Alert
import com.teacherassistant.app.data.local.entity.Student
import com.teacherassistant.app.data.local.entity.Tag
import com.teacherassistant.app.data.local.entity.TeacherNote
import com.teacherassistant.app.domain.repository.StudentRepository
import com.teacherassistant.app.domain.repository.TrackingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StudentProfileViewModel @Inject constructor(
    private val studentRepository: StudentRepository,
    private val trackingRepository: TrackingRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val studentId: Long = savedStateHandle.get<Long>("studentId") ?: 1L

    private val _uiState = MutableStateFlow<StudentProfileUiState>(StudentProfileUiState.Loading)
    val uiState: StateFlow<StudentProfileUiState> = _uiState.asStateFlow()

    init {
        loadStudentData()
    }

    private fun loadStudentData() {
        viewModelScope.launch {
            try {
                val student = studentRepository.getStudentById(studentId)
                if (student != null) {
                    // Fetch tags, alerts, notes, and later tiffin/results
                    studentRepository.getTagsForStudent(studentId).collect { tags ->
                        studentRepository.getAlertsForStudent(studentId).collect { alerts ->
                            studentRepository.getNotesForStudent(studentId).collect { notes ->
                                // Calculate start and end of current month for Rule 7
                                val cal = java.util.Calendar.getInstance()
                                cal.set(java.util.Calendar.DAY_OF_MONTH, 1)
                                cal.set(java.util.Calendar.HOUR_OF_DAY, 0)
                                cal.set(java.util.Calendar.MINUTE, 0)
                                val startOfMonth = cal.timeInMillis
                                
                                cal.set(java.util.Calendar.DAY_OF_MONTH, cal.getActualMaximum(java.util.Calendar.DAY_OF_MONTH))
                                cal.set(java.util.Calendar.HOUR_OF_DAY, 23)
                                cal.set(java.util.Calendar.MINUTE, 59)
                                val endOfMonth = cal.timeInMillis

                                trackingRepository.getMonthlyTiffinIncidentCount(studentId, startOfMonth, endOfMonth).collect { tiffinCount ->
                                    _uiState.value = StudentProfileUiState.Success(
                                        student = student,
                                        tags = tags,
                                        alerts = alerts,
                                        notes = notes,
                                        monthlyTiffinCount = tiffinCount
                                    )
                                }
                            }
                        }
                    }
                } else {
                    _uiState.value = StudentProfileUiState.Error("Student not found")
                }
            } catch (e: Exception) {
                _uiState.value = StudentProfileUiState.Error(e.message ?: "Unknown Error")
            }
        }
    }
}

sealed class StudentProfileUiState {
    object Loading : StudentProfileUiState()
    data class Success(
        val student: Student,
        val tags: List<Tag>,
        val alerts: List<Alert>,
        val notes: List<TeacherNote>,
        val monthlyTiffinCount: Int
    ) : StudentProfileUiState()
    data class Error(val message: String) : StudentProfileUiState()
}
