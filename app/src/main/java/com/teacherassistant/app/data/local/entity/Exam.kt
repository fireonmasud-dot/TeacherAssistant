package com.teacherassistant.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exams")
data class Exam(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val examName: String, // e.g., "Half-Yearly", "Annual", "Class Test 1"
    val year: String,
    val createdDate: Long = System.currentTimeMillis()
)
