package com.teacherassistant.app.domain.repository

import com.teacherassistant.app.data.local.entity.Exam
import com.teacherassistant.app.data.local.entity.SubjectResult
import kotlinx.coroutines.flow.Flow

interface AcademicRepository {
    fun getAllExams(): Flow<List<Exam>>
    suspend fun insertExam(exam: Exam): Long
    suspend fun deleteExam(exam: Exam)

    fun getResultsForStudentInExam(studentId: Long, examId: Long): Flow<List<SubjectResult>>
    fun getAllResultsForStudent(studentId: Long): Flow<List<SubjectResult>>
    fun getAllResultsForExam(examId: Long): Flow<List<SubjectResult>>
    
    // Rule 22: Marks Validation & Calculation
    suspend fun insertSubjectResult(result: SubjectResult): Long
    suspend fun deleteSubjectResult(studentId: Long, examId: Long, subjectName: String)

    fun calculateGPA(marks: Double, fullMarks: Double): Pair<String, Double> {
        val percentage = (marks / fullMarks) * 100
        return when {
            percentage >= 80 -> Pair("A+", 5.0)
            percentage >= 70 -> Pair("A", 4.0)
            percentage >= 60 -> Pair("A-", 3.5)
            percentage >= 50 -> Pair("B", 3.0)
            percentage >= 40 -> Pair("C", 2.0)
            percentage >= 33 -> Pair("D", 1.0)
            else -> Pair("F", 0.0)
        }
    }
}
