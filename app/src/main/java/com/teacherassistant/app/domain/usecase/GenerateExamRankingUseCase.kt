package com.teacherassistant.app.domain.usecase

import com.teacherassistant.app.data.local.entity.Student
import com.teacherassistant.app.data.local.entity.SubjectResult
import com.teacherassistant.app.domain.repository.AcademicRepository
import com.teacherassistant.app.domain.repository.StudentRepository
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class GenerateExamRankingUseCase @Inject constructor(
    private val academicRepository: AcademicRepository,
    private val studentRepository: StudentRepository
) {

    // Rule 23 & 24: Fair Rank Generation and Error Checking
    suspend fun invoke(examId: Long, requiredSubjects: List<String>): ExamRankingResult {
        val allActiveStudents = studentRepository.getAllStudents().firstOrNull() ?: emptyList()
        val allResults = academicRepository.getAllResultsForExam(examId).firstOrNull() ?: emptyList()

        val errors = mutableListOf<String>()
        val studentTotalMarks = mutableMapOf<Long, Double>()

        for (student in allActiveStudents) {
            val studentResults = allResults.filter { it.studentId == student.id }
            
            // Rule 24: Error Checking
            if (studentResults.isEmpty()) {
                errors.add("Missing marks for ${student.studentName} (Roll: ${student.rollNumber})")
                continue
            }
            
            val enteredSubjects = studentResults.map { it.subjectName }
            val missingSubjects = requiredSubjects.filter { !enteredSubjects.contains(it) }
            if (missingSubjects.isNotEmpty()) {
                errors.add("${student.studentName} is missing subjects: ${missingSubjects.joinToString()}")
            }

            // Calculate total marks
            val total = studentResults.sumOf { it.marksObtained }
            studentTotalMarks[student.id] = total
        }

        if (errors.isNotEmpty()) {
            return ExamRankingResult.Error(errors)
        }

        // Rule 23: Fair Ranking
        val sortedStudentsByMarks = studentTotalMarks.entries.sortedByDescending { it.value }
        
        val ranking = mutableListOf<StudentRank>()
        var currentRank = 1
        var previousMarks = -1.0
        var rankOffset = 0

        for ((index, entry) in sortedStudentsByMarks.withIndex()) {
            val student = allActiveStudents.find { it.id == entry.key }!!
            val marks = entry.value

            if (marks == previousMarks) {
                // Tie: Keep the same rank, increment offset
                rankOffset++
            } else {
                // Different marks: Calculate new rank
                currentRank = currentRank + rankOffset
                if (index == 0) currentRank = 1 // First student is always 1
                rankOffset = 1
            }

            ranking.add(StudentRank(student, currentRank, marks))
            previousMarks = marks
        }

        return ExamRankingResult.Success(ranking)
    }
}

data class StudentRank(
    val student: Student,
    val rank: Int,
    val totalMarks: Double
)

sealed class ExamRankingResult {
    data class Success(val ranking: List<StudentRank>) : ExamRankingResult()
    data class Error(val errors: List<String>) : ExamRankingResult()
}
