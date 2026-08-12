package com.teacherassistant.app.ui.utility

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teacherassistant.app.data.local.entity.Student
import com.teacherassistant.app.domain.repository.StudentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecentlyDeletedViewModel @Inject constructor(
    private val studentRepository: StudentRepository
) : ViewModel() {

    private val _deletedStudents = MutableStateFlow<List<Student>>(emptyList())
    val deletedStudents: StateFlow<List<Student>> = _deletedStudents.asStateFlow()

    init {
        viewModelScope.launch {
            studentRepository.getDeletedStudents().collect {
                _deletedStudents.value = it
            }
        }
    }

    fun restoreStudent(student: Student) {
        viewModelScope.launch {
            studentRepository.restoreStudent(student.id)
        }
    }

    fun permanentlyDelete(student: Student) {
        viewModelScope.launch {
            studentRepository.hardDeleteStudent(student)
        }
    }
}
