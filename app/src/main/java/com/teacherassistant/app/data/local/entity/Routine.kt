package com.teacherassistant.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "routines")
data class Routine(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val dayOfWeek: String, // e.g., "Monday"
    val timeSlot: String,  // e.g., "10:00 AM - 10:45 AM"
    val subject: String,
    val note: String? = null
)
