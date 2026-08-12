package com.teacherassistant.app.ui.attendance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teacherassistant.app.data.local.entity.Attendance
import com.teacherassistant.app.data.local.entity.Student
import com.teacherassistant.app.domain.repository.StudentRepository
import com.teacherassistant.app.domain.repository.TrackingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class AttendanceViewModel @Inject constructor(
    private val studentRepository: StudentRepository,
    private val trackingRepository: TrackingRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<AttendanceUiState>(AttendanceUiState.Loading)
    val uiState: StateFlow<AttendanceUiState> = _uiState.asStateFlow()
    
    // Store selected date, default to today
    private val _selectedDate = MutableStateFlow(getStartOfDay(System.currentTimeMillis()))
    val selectedDate = _selectedDate.asStateFlow()

    init {
        loadAttendanceData()
    }

    private fun loadAttendanceData() {
        viewModelScope.launch {
            studentRepository.getAllActiveStudents()
                .catch { e -> _uiState.value = AttendanceUiState.Error(e.message ?: "Unknown Error") }
                .collect { students ->
                    // In a real app we'd combine this with trackingRepository.getAttendanceByDate
                    // to show who is already marked, but for simplicity in this setup phase,
                    // we'll just emit the student list.
                    _uiState.value = AttendanceUiState.Success(students)
                }
        }
    }

    fun markAttendance(studentId: Long, status: String) {
        viewModelScope.launch {
            val attendance = Attendance(
                studentId = studentId,
                date = _selectedDate.value,
                status = status // "Present", "Absent", "Late"
            )
            trackingRepository.insertAttendance(attendance)
        }
    }
    
    private fun getStartOfDay(timeInMillis: Long): Long {
        val cal = Calendar.getInstance()
        cal.timeInMillis = timeInMillis
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }
}

sealed class AttendanceUiState {
    object Loading : AttendanceUiState()
    data class Success(val students: List<Student>) : AttendanceUiState()
    data class Error(val message: String) : AttendanceUiState()
}
