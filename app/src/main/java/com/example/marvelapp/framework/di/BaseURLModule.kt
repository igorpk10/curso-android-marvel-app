package com.example.marvelapp.framework.di

import com.example.marvelapp.BuildConfig
import com.example.marvelapp.framework.di.qualifier.BaseURLQualifier
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object BaseURLModule {

    @BaseURLQualifier
    @Provides
    fun provideBaseURL(): String = BuildConfig.BASE_URL
}