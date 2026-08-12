package com.teacherassistant.app.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "subject_results",
    foreignKeys = [
        ForeignKey(
            entity = Student::class,
            parentColumns = ["id"],
            childColumns = ["studentId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Exam::class,
            parentColumns = ["id"],
            childColumns = ["examId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["studentId", "examId", "subjectName"], unique = true)
    ]
)
data class SubjectResult(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val studentId: Long,
    val examId: Long,
    val subjectName: String,
    val marksObtained: Double,
    val fullMarks: Double = 100.0,
    val passMarks: Double = 33.0,
    val grade: String? = null, 
    val gradePoint: Double? = null,
    val note: String? = null
)
