package com.teacherassistant.app.ui.studentlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teacherassistant.app.data.local.entity.Student
import com.teacherassistant.app.domain.repository.StudentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddStudentViewModel @Inject constructor(
    private val repository: StudentRepository
) : ViewModel() {

    fun addStudent(
        studentName: String,
        className: String,
        section: String,
        rollNumber: String,
        fatherName: String,
        fatherPhone: String,
        motherName: String,
        motherPhone: String,
        studentIdNumber: String
    ) {
        viewModelScope.launch {
            val newStudent = Student(
                studentName = studentName,
                className = className,
                section = section,
                rollNumber = rollNumber,
                fatherName = fatherName,
                fatherPhone = fatherPhone,
                motherName = motherName,
                motherPhone = motherPhone,
                studentIdNumber = studentIdNumber
            )
            repository.insertStudent(newStudent)
        }
    }
}
