package com.teacherassistant.app.data.repository

import com.teacherassistant.app.data.local.dao.StudentDao
import com.teacherassistant.app.data.local.entity.Alert
import com.teacherassistant.app.data.local.entity.Student
import com.teacherassistant.app.data.local.entity.StudentTagCrossRef
import com.teacherassistant.app.data.local.entity.Tag
import com.teacherassistant.app.data.local.entity.TeacherNote
import com.teacherassistant.app.domain.repository.StudentRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class StudentRepositoryImpl @Inject constructor(
    private val studentDao: StudentDao
) : StudentRepository {

    override fun getAllStudents(): Flow<List<Student>> {
        return studentDao.getAllStudents()
    }

    override suspend fun getStudentById(id: Long): Student? {
        return studentDao.getStudentById(id)
    }

    override fun searchStudents(query: String): Flow<List<Student>> {
        return studentDao.searchStudents(query)
    }

    override fun getFavoriteStudents(): Flow<List<Student>> {
        return studentDao.getFavoriteStudents()
    }

    override suspend fun insertStudent(student: Student): Long {
        return studentDao.insertStudent(student)
    }

    override suspend fun softDeleteStudent(studentId: Long) {
        studentDao.softDeleteStudent(studentId, System.currentTimeMillis())
    }

    override suspend fun restoreStudent(studentId: Long) {
        studentDao.restoreStudent(studentId)
    }

    override suspend fun hardDeleteStudent(student: Student) {
        studentDao.hardDeleteStudent(student)
    }

    override fun getDeletedStudents(): Flow<List<Student>> {
        return studentDao.getDeletedStudents()
    }

    override suspend fun updateFavoriteStatus(studentId: Long, isFavorite: Boolean) {
        studentDao.updateFavoriteStatus(studentId, isFavorite)
    }

    override suspend fun updateStudentRoll(studentId: Long, newRoll: String) {
        studentDao.updateStudentRoll(studentId, newRoll)
    }

    override suspend fun insertTag(tag: Tag): Long {
        return studentDao.insertTag(tag)
    }

    override fun getAllTags(): Flow<List<Tag>> {
        return studentDao.getAllTags()
    }

    override suspend fun assignTagToStudent(studentId: Long, tagId: Long) {
        studentDao.insertStudentTagCrossRef(StudentTagCrossRef(studentId, tagId))
    }

    override suspend fun removeTagFromStudent(studentId: Long, tagId: Long) {
        studentDao.deleteStudentTagCrossRef(studentId, tagId)
    }

    override fun getTagsForStudent(studentId: Long): Flow<List<Tag>> {
        return studentDao.getTagsForStudent(studentId)
    }

    override suspend fun insertAlert(alert: Alert): Long {
        return studentDao.insertAlert(alert)
    }

    override fun getAlertsForStudent(studentId: Long): Flow<List<Alert>> {
        return studentDao.getAlertsForStudent(studentId)
    }

    override suspend fun updateAlertResolution(alertId: Long, isResolved: Boolean) {
        studentDao.updateAlertResolution(alertId, isResolved)
    }

    override suspend fun insertTeacherNote(note: TeacherNote): Long {
        return studentDao.insertTeacherNote(note)
    }

    override fun getNotesForStudent(studentId: Long): Flow<List<TeacherNote>> {
        return studentDao.getNotesForStudent(studentId)
    }

    override suspend fun deleteTeacherNote(note: TeacherNote) {
        studentDao.deleteTeacherNote(note)
    }
}
