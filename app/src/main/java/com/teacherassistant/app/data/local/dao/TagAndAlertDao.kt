package com.teacherassistant.app.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.teacherassistant.app.data.local.entity.Alert
import com.teacherassistant.app.data.local.entity.StudentTagCrossRef
import com.teacherassistant.app.data.local.entity.Tag
import kotlinx.coroutines.flow.Flow

@Dao
interface TagAndAlertDao {
    // --- Tags ---
    @Query("SELECT * FROM tags")
    fun getAllTags(): Flow<List<Tag>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTag(tag: Tag): Long

    @Delete
    suspend fun deleteTag(tag: Tag)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun assignTagToStudent(crossRef: StudentTagCrossRef)

    @Query("DELETE FROM student_tags WHERE studentId = :studentId AND tagId = :tagId")
    suspend fun removeTagFromStudent(studentId: Long, tagId: Long)

    // --- Alerts ---
    @Query("SELECT * FROM alerts WHERE studentId = :studentId ORDER BY createdDate DESC")
    fun getAlertsForStudent(studentId: Long): Flow<List<Alert>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlert(alert: Alert): Long

    @Query("UPDATE alerts SET isResolved = 1 WHERE id = :alertId")
    suspend fun resolveAlert(alertId: Long)

    @Delete
    suspend fun deleteAlert(alert: Alert)
}
