package com.teacherassistant.app.ui.studentlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teacherassistant.app.data.local.entity.Student
import com.teacherassistant.app.domain.repository.StudentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class StudentListViewModel @Inject constructor(
    private val repository: StudentRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _uiState = MutableStateFlow<StudentListUiState>(StudentListUiState.Loading)
    val uiState: StateFlow<StudentListUiState> = _uiState.asStateFlow()

    init {
        // Debounce search query to avoid unnecessary DB queries (Rule 12)
        viewModelScope.launch {
            _searchQuery
                .debounce(300)
                .flatMapLatest { query ->
                    if (query.isBlank()) {
                        repository.getAllStudents()
                    } else {
                        repository.searchStudents(query)
                    }
                }
                .catch { e -> _uiState.value = StudentListUiState.Error(e.message ?: "Unknown Error") }
                .collect { students ->
                    if (students.isEmpty()) {
                        _uiState.value = StudentListUiState.Empty
                    } else {
                        _uiState.value = StudentListUiState.Success(students)
                    }
                }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun deleteStudent(student: Student) {
        // Rule 32: Soft Delete
        viewModelScope.launch {
            repository.softDeleteStudent(student.id)
        }
    }
}

sealed class StudentListUiState {
    object Loading : StudentListUiState()
    object Empty : StudentListUiState()
    data class Success(val students: List<Student>) : StudentListUiState()
    data class Error(val message: String) : StudentListUiState()
}
