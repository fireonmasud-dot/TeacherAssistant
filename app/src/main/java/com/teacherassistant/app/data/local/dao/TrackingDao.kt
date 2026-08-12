package com.teacherassistant.app.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.teacherassistant.app.data.local.entity.Attendance
import com.teacherassistant.app.data.local.entity.TiffinIncident
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackingDao {
    // --- Attendance ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttendance(attendance: Attendance): Long

    @Query("SELECT * FROM attendance WHERE date = :date")
    fun getAttendanceByDate(date: Long): Flow<List<Attendance>>

    @Query("SELECT * FROM attendance WHERE studentId = :studentId ORDER BY date DESC")
    fun getAttendanceForStudent(studentId: Long): Flow<List<Attendance>>
    
    // --- Tiffin Incidents ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTiffinIncident(incident: TiffinIncident): Long

    @Delete
    suspend fun deleteTiffinIncident(incident: TiffinIncident)

    @Query("SELECT * FROM tiffin_incidents WHERE studentId = :studentId ORDER BY createdTimestamp DESC")
    fun getTiffinIncidentsForStudent(studentId: Long): Flow<List<TiffinIncident>>

    // Rule 7: Monthly count calculation
    @Query("SELECT COUNT(*) FROM tiffin_incidents WHERE studentId = :studentId AND date >= :startOfMonth AND date <= :endOfMonth")
    fun getMonthlyTiffinIncidentCount(studentId: Long, startOfMonth: Long, endOfMonth: Long): Flow<Int>

    // Rule 27: Monthly Tiffin Report (all incidents in a month)
    @Query("SELECT * FROM tiffin_incidents WHERE date >= :startOfMonth AND date <= :endOfMonth ORDER BY date DESC")
    fun getAllTiffinIncidentsForMonth(startOfMonth: Long, endOfMonth: Long): Flow<List<TiffinIncident>>
    
    // Rule 8: Date-wise history
    @Query("SELECT * FROM tiffin_incidents WHERE date = :date")
    fun getTiffinIncidentsByDate(date: Long): Flow<List<TiffinIncident>>
}
