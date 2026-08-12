package com.teacherassistant.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todo_items")
data class TodoItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val task: String,
    val priority: String, // "High", "Medium", "Low"
    val isCompleted: Boolean = false,
    val dueDate: Long? = null,
    val createdTimestamp: Long = System.currentTimeMillis()
)
