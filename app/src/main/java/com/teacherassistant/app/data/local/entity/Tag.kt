package com.teacherassistant.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tags")
data class Tag(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String, // e.g., "Good", "Needs Attention"
    val colorHex: String? = null
)
