package com.teacherassistant.app.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "tiffin_incidents",
    foreignKeys = [
        ForeignKey(
            entity = Student::class,
            parentColumns = ["id"],
            childColumns = ["studentId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("studentId"), Index("date")]
)
data class TiffinIncident(
    @PrimaryKey(autoGenerate = true)
    val incidentId: Long = 0,
    val studentId: Long,
    val date: Long, // e.g. Start of the day timestamp for querying
    val time: String, // e.g. "1:30 PM"
    val incidentType: String,
    val description: String,
    val teacherNote: String? = null,
    val voiceRecordingRef: String? = null, // URI or File Path
    val createdTimestamp: Long = System.currentTimeMillis()
)
