package com.example.marvelapp.framework.di

import com.example.marvelapp.framework.StorageRepositoryImpl
import com.example.marvelapp.framework.local.DataStoragePreferencesDataStore
import com.igaopk10.core.data.repository.StorageLocalDataSource
import com.igaopk10.core.data.repository.StorageRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface StorageRepositoryModule {

    @Binds
    fun bindStorageRepository(repository: StorageRepositoryImpl): StorageRepository

    @Singleton
    @Binds
    fun bindLocalDataSource(dataSource: DataStoragePreferencesDataStore): StorageLocalDataSource
}