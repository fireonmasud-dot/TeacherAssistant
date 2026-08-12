package com.teacherassistant.app.di

import com.teacherassistant.app.data.ai.AiServiceImpl
import com.teacherassistant.app.domain.ai.AiService
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AiModule {

    @Binds
    @Singleton
    abstract fun bindAiService(
        aiServiceImpl: AiServiceImpl
    ): AiService
}
