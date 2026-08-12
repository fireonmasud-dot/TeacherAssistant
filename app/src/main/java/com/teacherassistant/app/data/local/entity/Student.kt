package com.teacherassistant.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "students")
data class Student(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val studentName: String,
    val photoUri: String? = null,
    val rollNumber: Int,
    val className: String,
    val section: String,
    val dateOfBirth: String? = null,
    val gender: String? = null,
    val studentIdNumber: String,
    val fatherName: String,
    val fatherPhone: String,
    val motherName: String,
    val motherPhone: String,
    val presentAddress: String? = null,
    val permanentAddress: String? = null,
    val isFavorite: Boolean = false,
    val isDeleted: Boolean = false,
    val deletedAt: Long? = null,
    val createdDate: Long = System.currentTimeMillis(),
    val updatedDate: Long = System.currentTimeMillis()
)
