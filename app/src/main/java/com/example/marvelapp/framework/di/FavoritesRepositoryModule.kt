package com.example.marvelapp.framework.di

import androidx.room.Room
import com.example.marvelapp.framework.FavoritesRepositoryImpl
import com.example.marvelapp.framework.local.RoomFavoritesDataSource
import com.igaopk10.core.data.repository.FavoritesLocalDataSource
import com.igaopk10.core.data.repository.FavoritesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface FavoritesRepositoryModule {

    @Binds
    fun bindFavoritesRepository(
        repository: FavoritesRepositoryImpl
    ): FavoritesRepository

    @Binds
    fun bindLocalDataSource(
        dataSource: RoomFavoritesDataSource
    ): FavoritesLocalDataSource
}
