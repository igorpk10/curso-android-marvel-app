package com.example.marvelapp.framework.di

import com.igaopk10.core.usecase.base.AppCoroutinesDispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(SingletonComponent::class)
object CoroutinesModule {

    @Provides
    fun provideDispatchers() = AppCoroutinesDispatcher(
        Dispatchers.IO,
        Dispatchers.Default,
        Dispatchers.Main
    )
}