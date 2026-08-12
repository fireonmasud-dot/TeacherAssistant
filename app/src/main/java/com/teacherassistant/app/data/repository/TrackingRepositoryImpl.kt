package com.teacherassistant.app.data.repository

import com.teacherassistant.app.data.local.dao.TrackingDao
import com.teacherassistant.app.data.local.entity.Attendance
import com.teacherassistant.app.data.local.entity.TiffinIncident
import com.teacherassistant.app.domain.repository.TrackingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TrackingRepositoryImpl @Inject constructor(
    private val trackingDao: TrackingDao
) : TrackingRepository {

    override suspend fun insertAttendance(attendance: Attendance): Long {
        return trackingDao.insertAttendance(attendance)
    }

    override fun getAttendanceByDate(date: Long): Flow<List<Attendance>> {
        return trackingDao.getAttendanceByDate(date)
    }

    override fun getAttendanceForStudent(studentId: Long): Flow<List<Attendance>> {
        return trackingDao.getAttendanceForStudent(studentId)
    }

    override suspend fun insertTiffinIncident(incident: TiffinIncident): Long {
        return trackingDao.insertTiffinIncident(incident)
    }

    override suspend fun deleteTiffinIncident(incident: TiffinIncident) {
        trackingDao.deleteTiffinIncident(incident)
    }

    override fun getTiffinIncidentsForStudent(studentId: Long): Flow<List<TiffinIncident>> {
        return trackingDao.getTiffinIncidentsForStudent(studentId)
    }

    override fun getMonthlyTiffinIncidentCount(
        studentId: Long,
        startOfMonth: Long,
        endOfMonth: Long
    ): Flow<Int> {
        return trackingDao.getMonthlyTiffinIncidentCount(studentId, startOfMonth, endOfMonth)
    }

    override fun getAllTiffinIncidentsForMonth(
        startOfMonth: Long,
        endOfMonth: Long
    ): Flow<List<TiffinIncident>> {
        return trackingDao.getAllTiffinIncidentsForMonth(startOfMonth, endOfMonth)
    }

    override fun getTiffinIncidentsByDate(date: Long): Flow<List<TiffinIncident>> {
        return trackingDao.getTiffinIncidentsByDate(date)
    }
}
