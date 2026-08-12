package com.teacherassistant.app.data.repository

import com.teacherassistant.app.data.local.dao.AcademicDao
import com.teacherassistant.app.data.local.entity.Exam
import com.teacherassistant.app.data.local.entity.SubjectResult
import com.teacherassistant.app.domain.repository.AcademicRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AcademicRepositoryImpl @Inject constructor(
    private val dao: AcademicDao
) : AcademicRepository {

    override fun getAllExams(): Flow<List<Exam>> {
        return dao.getAllExams()
    }

    override suspend fun insertExam(exam: Exam): Long {
        return dao.insertExam(exam)
    }

    override suspend fun deleteExam(exam: Exam) {
        dao.deleteExam(exam)
    }

    override fun getResultsForStudentInExam(studentId: Long, examId: Long): Flow<List<SubjectResult>> {
        return dao.getResultsForStudentInExam(studentId, examId)
    }

    override fun getAllResultsForStudent(studentId: Long): Flow<List<SubjectResult>> {
        return dao.getAllResultsForStudent(studentId)
    }

    override fun getAllResultsForExam(examId: Long): Flow<List<SubjectResult>> {
        return dao.getAllResultsForExam(examId)
    }

    override suspend fun insertSubjectResult(result: SubjectResult): Long {
        // Rule 22: Marks Validation
        if (result.marksObtained < 0 || result.marksObtained > result.fullMarks) {
            throw IllegalArgumentException("Marks must be >= 0 and <= Full Marks")
        }
        
        val (grade, gradePoint) = calculateGPA(result.marksObtained, result.fullMarks)
        val resultWithGrade = result.copy(grade = grade, gradePoint = gradePoint)
        return dao.insertSubjectResult(resultWithGrade)
    }

    override suspend fun deleteSubjectResult(studentId: Long, examId: Long, subjectName: String) {
        dao.deleteSubjectResult(studentId, examId, subjectName)
    }
}
