package com.example.marvelapp.framework.di

import com.igaopk10.core.usecase.base.AppCoroutinesDispatcher
import com.igaopk10.core.usecase.base.CoroutinesDispatchers
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface CoroutinesModule {

    @Binds
    fun bindDispatchers(dispatcher: AppCoroutinesDispatcher): CoroutinesDispatchers
}