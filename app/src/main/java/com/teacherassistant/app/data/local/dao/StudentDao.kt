package com.teacherassistant.app.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.teacherassistant.app.data.local.entity.Alert
import com.teacherassistant.app.data.local.entity.Student
import com.teacherassistant.app.data.local.entity.StudentTagCrossRef
import com.teacherassistant.app.data.local.entity.Tag
import com.teacherassistant.app.data.local.entity.TeacherNote
import kotlinx.coroutines.flow.Flow

@Dao
interface StudentDao {
    
    // --- Students ---
    @Query("SELECT * FROM students WHERE isDeleted = 0 ORDER BY rollNumber ASC")
    fun getAllStudents(): Flow<List<Student>>
    
    @Query("SELECT * FROM students WHERE id = :id AND isDeleted = 0")
    suspend fun getStudentById(id: Long): Student?

    @Query("SELECT * FROM students WHERE (studentName LIKE '%' || :query || '%' OR rollNumber = :query OR studentIdNumber = :query OR fatherPhone = :query OR motherPhone = :query) AND isDeleted = 0")
    fun searchStudents(query: String): Flow<List<Student>>

    @Query("SELECT * FROM students WHERE isFavorite = 1 AND isDeleted = 0")
    fun getFavoriteStudents(): Flow<List<Student>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudent(student: Student): Long

    // Rule 32: Soft Delete
    @Query("UPDATE students SET isDeleted = 1, deletedAt = :timestamp WHERE id = :studentId")
    suspend fun softDeleteStudent(studentId: Long, timestamp: Long)

    @Query("UPDATE students SET isDeleted = 0, deletedAt = NULL WHERE id = :studentId")
    suspend fun restoreStudent(studentId: Long)

    @Query("SELECT * FROM students WHERE isDeleted = 1 ORDER BY deletedAt DESC")
    fun getDeletedStudents(): Flow<List<Student>>

    // Hard delete for cleanup
    @Delete
    suspend fun hardDeleteStudent(student: Student)
    
    @Query("UPDATE students SET isFavorite = :isFavorite WHERE id = :studentId")
    suspend fun updateFavoriteStatus(studentId: Long, isFavorite: Boolean)

    @Query("UPDATE students SET rollNumber = :newRoll WHERE id = :studentId")
    suspend fun updateStudentRoll(studentId: Long, newRoll: String)

    // --- Tags ---
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTag(tag: Tag): Long
    
    @Query("SELECT * FROM tags")
    fun getAllTags(): Flow<List<Tag>>
    
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertStudentTagCrossRef(crossRef: StudentTagCrossRef)
    
    @Query("DELETE FROM student_tag_cross_ref WHERE studentId = :studentId AND tagId = :tagId")
    suspend fun deleteStudentTagCrossRef(studentId: Long, tagId: Long)
    
    @Query("SELECT t.* FROM tags t INNER JOIN student_tag_cross_ref st ON t.id = st.tagId WHERE st.studentId = :studentId")
    fun getTagsForStudent(studentId: Long): Flow<List<Tag>>

    // --- Alerts ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlert(alert: Alert): Long
    
    @Query("SELECT * FROM alerts WHERE studentId = :studentId ORDER BY createdDate DESC")
    fun getAlertsForStudent(studentId: Long): Flow<List<Alert>>
    
    @Query("UPDATE alerts SET isResolved = :isResolved WHERE id = :alertId")
    suspend fun updateAlertResolution(alertId: Long, isResolved: Boolean)

    // --- Teacher Notes ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTeacherNote(note: TeacherNote): Long
    
    @Query("SELECT * FROM teacher_notes WHERE studentId = :studentId ORDER BY updatedDate DESC")
    fun getNotesForStudent(studentId: Long): Flow<List<TeacherNote>>
    
    @Delete
    suspend fun deleteTeacherNote(note: TeacherNote)
}
