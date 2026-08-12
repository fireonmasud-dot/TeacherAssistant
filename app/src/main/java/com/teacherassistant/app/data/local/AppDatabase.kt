package com.teacherassistant.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.teacherassistant.app.data.local.dao.AcademicDao
import com.teacherassistant.app.data.local.dao.StudentDao
import com.teacherassistant.app.data.local.dao.TrackingDao
import com.teacherassistant.app.data.local.dao.UtilityDao
import com.teacherassistant.app.data.local.entity.Alert
import com.teacherassistant.app.data.local.entity.Attendance
import com.teacherassistant.app.data.local.entity.Exam
import com.teacherassistant.app.data.local.entity.Routine
import com.teacherassistant.app.data.local.entity.Student
import com.teacherassistant.app.data.local.entity.StudentTagCrossRef
import com.teacherassistant.app.data.local.entity.SubjectResult
import com.teacherassistant.app.data.local.entity.Tag
import com.teacherassistant.app.data.local.entity.TeacherNote
import com.teacherassistant.app.data.local.entity.TiffinIncident
import com.teacherassistant.app.data.local.entity.TodoItem

@Database(
    entities = [
        Student::class, 
        Tag::class, 
        StudentTagCrossRef::class, 
        Alert::class,
        TeacherNote::class,
        Attendance::class,
        TiffinIncident::class,
        Exam::class,
        SubjectResult::class,
        Routine::class,
        TodoItem::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract val studentDao: StudentDao
    abstract val trackingDao: TrackingDao
    abstract val academicDao: AcademicDao
    abstract val utilityDao: UtilityDao
    
    companion object {
        const val DATABASE_NAME = "teacher_assistant_db"
    }
}
