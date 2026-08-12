package com.teacherassistant.app.di

import com.teacherassistant.app.data.repository.StudentRepositoryImpl
import com.teacherassistant.app.domain.repository.StudentRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindStudentRepository(
        studentRepositoryImpl: StudentRepositoryImpl
    ): StudentRepository

    @Binds
    @Singleton
    abstract fun bindTrackingRepository(
        trackingRepositoryImpl: com.teacherassistant.app.data.repository.TrackingRepositoryImpl
    ): com.teacherassistant.app.domain.repository.TrackingRepository

    @Binds
    @Singleton
    abstract fun bindAcademicRepository(
        academicRepositoryImpl: com.teacherassistant.app.data.repository.AcademicRepositoryImpl
    ): com.teacherassistant.app.domain.repository.AcademicRepository

    @Binds
    @Singleton
    abstract fun bindUtilityRepository(
        utilityRepositoryImpl: com.teacherassistant.app.data.repository.UtilityRepositoryImpl
    ): com.teacherassistant.app.domain.repository.UtilityRepository
}
