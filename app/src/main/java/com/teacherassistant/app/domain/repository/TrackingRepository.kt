package com.teacherassistant.app.domain.repository

import com.teacherassistant.app.data.local.entity.Attendance
import com.teacherassistant.app.data.local.entity.TiffinIncident
import kotlinx.coroutines.flow.Flow

interface TrackingRepository {
    // Attendance
    suspend fun insertAttendance(attendance: Attendance): Long
    fun getAttendanceByDate(date: Long): Flow<List<Attendance>>
    fun getAttendanceForStudent(studentId: Long): Flow<List<Attendance>>
    
    // Tiffin Incidents
    suspend fun insertTiffinIncident(incident: TiffinIncident): Long
    suspend fun deleteTiffinIncident(incident: TiffinIncident)
    fun getTiffinIncidentsForStudent(studentId: Long): Flow<List<TiffinIncident>>
    
    // Monthly calculation (Rule 7)
    fun getMonthlyTiffinIncidentCount(studentId: Long, startOfMonth: Long, endOfMonth: Long): Flow<Int>
    
    // Monthly Report (Rule 27)
    fun getAllTiffinIncidentsForMonth(startOfMonth: Long, endOfMonth: Long): Flow<List<TiffinIncident>>
    
    // Date history (Rule 8)
    fun getTiffinIncidentsByDate(date: Long): Flow<List<TiffinIncident>>
}
