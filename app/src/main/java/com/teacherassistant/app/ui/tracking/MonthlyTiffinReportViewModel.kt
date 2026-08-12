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
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class MonthlyTiffinReportViewModel @Inject constructor(
    private val trackingRepository: TrackingRepository,
    private val studentRepository: StudentRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<MonthlyTiffinReportUiState>(MonthlyTiffinReportUiState.Loading)
    val uiState: StateFlow<MonthlyTiffinReportUiState> = _uiState.asStateFlow()

    init {
        // Load current month by default
        val cal = Calendar.getInstance()
        generateReport(cal.get(Calendar.MONTH), cal.get(Calendar.YEAR))
    }

    fun generateReport(month: Int, year: Int) {
        viewModelScope.launch {
            _uiState.value = MonthlyTiffinReportUiState.Loading
            try {
                val cal = Calendar.getInstance()
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, month)
                
                cal.set(Calendar.DAY_OF_MONTH, 1)
                cal.set(Calendar.HOUR_OF_DAY, 0)
                cal.set(Calendar.MINUTE, 0)
                val startOfMonth = cal.timeInMillis
                
                cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH))
                cal.set(Calendar.HOUR_OF_DAY, 23)
                cal.set(Calendar.MINUTE, 59)
                val endOfMonth = cal.timeInMillis

                val allStudents = studentRepository.getAllStudents().firstOrNull() ?: emptyList()
                val incidents = trackingRepository.getAllTiffinIncidentsForMonth(startOfMonth, endOfMonth).firstOrNull() ?: emptyList()

                val studentIncidentCounts = mutableMapOf<Long, Int>()
                incidents.forEach { incident ->
                    studentIncidentCounts[incident.studentId] = studentIncidentCounts.getOrDefault(incident.studentId, 0) + 1
                }

                val totalStudents = allStudents.size
                val totalIncidents = incidents.size
                val studentsWithIncidents = studentIncidentCounts.size
                val studentsWithoutIncidents = totalStudents - studentsWithIncidents

                val reportItems = allStudents.filter { studentIncidentCounts.containsKey(it.id) }.map { student ->
                    StudentIncidentSummary(student, studentIncidentCounts[student.id] ?: 0)
                }.sortedByDescending { it.incidentCount }

                _uiState.value = MonthlyTiffinReportUiState.Success(
                    totalStudents = totalStudents,
                    studentsWithIncidents = studentsWithIncidents,
                    studentsWithoutIncidents = studentsWithoutIncidents,
                    totalIncidents = totalIncidents,
                    reportItems = reportItems
                )
            } catch (e: Exception) {
                _uiState.value = MonthlyTiffinReportUiState.Error(e.message ?: "Error generating report")
            }
        }
    }
}

data class StudentIncidentSummary(
    val student: Student,
    val incidentCount: Int
)

sealed class MonthlyTiffinReportUiState {
    object Loading : MonthlyTiffinReportUiState()
    data class Success(
        val totalStudents: Int,
        val studentsWithIncidents: Int,
        val studentsWithoutIncidents: Int,
        val totalIncidents: Int,
        val reportItems: List<StudentIncidentSummary>
    ) : MonthlyTiffinReportUiState()
    data class Error(val message: String) : MonthlyTiffinReportUiState()
}
