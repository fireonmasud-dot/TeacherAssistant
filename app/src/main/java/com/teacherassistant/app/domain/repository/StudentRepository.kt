package com.teacherassistant.app.domain.repository

import com.teacherassistant.app.data.local.entity.Alert
import com.teacherassistant.app.data.local.entity.Student
import com.teacherassistant.app.data.local.entity.Tag
import com.teacherassistant.app.data.local.entity.TeacherNote
import kotlinx.coroutines.flow.Flow

interface StudentRepository {
    fun getAllStudents(): Flow<List<Student>>
    suspend fun getStudentById(id: Long): Student?
    fun searchStudents(query: String): Flow<List<Student>>
    fun getFavoriteStudents(): Flow<List<Student>>
    suspend fun insertStudent(student: Student): Long
    // Rule 32: Soft Delete
    suspend fun softDeleteStudent(studentId: Long)
    suspend fun restoreStudent(studentId: Long)
    suspend fun hardDeleteStudent(student: Student)
    fun getDeletedStudents(): Flow<List<Student>>

    suspend fun updateFavoriteStatus(studentId: Long, isFavorite: Boolean)
    suspend fun updateStudentRoll(studentId: Long, newRoll: String)

    suspend fun insertTag(tag: Tag): Long
    fun getAllTags(): Flow<List<Tag>>
    suspend fun assignTagToStudent(studentId: Long, tagId: Long)
    suspend fun removeTagFromStudent(studentId: Long, tagId: Long)
    fun getTagsForStudent(studentId: Long): Flow<List<Tag>>

    suspend fun insertAlert(alert: Alert): Long
    fun getAlertsForStudent(studentId: Long): Flow<List<Alert>>
    suspend fun updateAlertResolution(alertId: Long, isResolved: Boolean)

    suspend fun insertTeacherNote(note: TeacherNote): Long
    fun getNotesForStudent(studentId: Long): Flow<List<TeacherNote>>
    suspend fun deleteTeacherNote(note: TeacherNote)
}
