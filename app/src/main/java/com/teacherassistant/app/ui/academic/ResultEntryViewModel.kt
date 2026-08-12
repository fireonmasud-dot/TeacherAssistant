package com.teacherassistant.app.ui.academic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teacherassistant.app.data.local.entity.Exam
import com.teacherassistant.app.data.local.entity.Student
import com.teacherassistant.app.data.local.entity.SubjectResult
import com.teacherassistant.app.domain.repository.AcademicRepository
import com.teacherassistant.app.domain.repository.StudentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResultEntryViewModel @Inject constructor(
    private val academicRepository: AcademicRepository,
    private val studentRepository: StudentRepository
) : ViewModel() {

    private val _exams = MutableStateFlow<List<Exam>>(emptyList())
    val exams = _exams.asStateFlow()

    private val _students = MutableStateFlow<List<Student>>(emptyList())
    val students = _students.asStateFlow()

    private val _saveState = MutableStateFlow<SaveState>(SaveState.Idle)
    val saveState: StateFlow<SaveState> = _saveState.asStateFlow()

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            academicRepository.getAllExams().collect { _exams.value = it }
        }
        viewModelScope.launch {
            // Placeholder: Assume all students for now. In real app, getAllActiveStudents().
            val activeStudents = studentRepository.getAllStudents().firstOrNull() ?: emptyList()
            _students.value = activeStudents
        }
    }

    fun saveResult(
        studentId: Long,
        examId: Long,
        subjectName: String,
        marksObtained: String,
        fullMarks: String,
        passMarks: String
    ) {
        viewModelScope.launch {
            _saveState.value = SaveState.Loading
            try {
                val marks = marksObtained.toDoubleOrNull() ?: throw IllegalArgumentException("Invalid marks format")
                val full = fullMarks.toDoubleOrNull() ?: throw IllegalArgumentException("Invalid full marks format")
                val pass = passMarks.toDoubleOrNull() ?: throw IllegalArgumentException("Invalid pass marks format")

                val result = SubjectResult(
                    studentId = studentId,
                    examId = examId,
                    subjectName = subjectName,
                    marksObtained = marks,
                    fullMarks = full,
                    passMarks = pass
                )
                // This call internally calculates GPA and validates Rule 22 (marks <= fullMarks)
                academicRepository.insertSubjectResult(result)
                _saveState.value = SaveState.Success
            } catch (e: Exception) {
                _saveState.value = SaveState.Error(e.message ?: "Unknown Error")
            }
        }
    }

    fun createExam(name: String, year: String) {
        viewModelScope.launch {
            academicRepository.insertExam(Exam(examName = name, year = year))
        }
    }
    
    fun resetSaveState() {
        _saveState.value = SaveState.Idle
    }
}

sealed class SaveState {
    object Idle : SaveState()
    object Loading : SaveState()
    object Success : SaveState()
    data class Error(val message: String) : SaveState()
}
