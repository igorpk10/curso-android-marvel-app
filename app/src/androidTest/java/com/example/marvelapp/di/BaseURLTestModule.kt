package com.example.marvelapp.di

import com.example.marvelapp.framework.di.qualifier.BaseURLQualifier
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object BaseURLTestModule {

    @BaseURLQualifier
    @Provides
    fun provideBaseURL(): String = "http://localhost:8080/"
}