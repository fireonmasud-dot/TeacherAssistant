package com.teacherassistant.app.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.teacherassistant.app.data.local.entity.Exam
import com.teacherassistant.app.data.local.entity.SubjectResult
import kotlinx.coroutines.flow.Flow

@Dao
interface AcademicDao {
    // --- Exams ---
    @Query("SELECT * FROM exams ORDER BY createdDate DESC")
    fun getAllExams(): Flow<List<Exam>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExam(exam: Exam): Long

    @Delete
    suspend fun deleteExam(exam: Exam)

    // --- Results ---
    @Query("SELECT * FROM subject_results WHERE studentId = :studentId AND examId = :examId")
    fun getResultsForStudentInExam(studentId: Long, examId: Long): Flow<List<SubjectResult>>
    
    @Query("SELECT * FROM subject_results WHERE studentId = :studentId")
    fun getAllResultsForStudent(studentId: Long): Flow<List<SubjectResult>>

    @Query("SELECT * FROM subject_results WHERE examId = :examId")
    fun getAllResultsForExam(examId: Long): Flow<List<SubjectResult>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubjectResult(result: SubjectResult): Long
    
    @Query("DELETE FROM subject_results WHERE studentId = :studentId AND examId = :examId AND subjectName = :subjectName")
    suspend fun deleteSubjectResult(studentId: Long, examId: Long, subjectName: String)
}
