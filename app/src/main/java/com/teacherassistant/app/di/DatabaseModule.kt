package com.teacherassistant.app.di

import android.app.Application
import androidx.room.Room
import com.teacherassistant.app.data.local.AppDatabase
import com.teacherassistant.app.data.local.dao.StudentDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(app: Application): AppDatabase {
        return Room.databaseBuilder(
            app,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideStudentDao(db: AppDatabase): StudentDao {
        return db.studentDao
    }

    @Provides
    @Singleton
    fun provideTrackingDao(db: AppDatabase): com.teacherassistant.app.data.local.dao.TrackingDao {
        return db.trackingDao
    }

    @Provides
    @Singleton
    fun provideAcademicDao(db: AppDatabase): com.teacherassistant.app.data.local.dao.AcademicDao {
        return db.academicDao
    }

    @Provides
    @Singleton
    fun provideUtilityDao(db: AppDatabase): com.teacherassistant.app.data.local.dao.UtilityDao {
        return db.utilityDao
    }
}
