package com.example.marvelapp.framework

import com.igaopk10.core.data.repository.FavoritesLocalDataSource
import com.igaopk10.core.data.repository.FavoritesRepository
import com.igaopk10.core.domain.model.Character
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FavoritesRepositoryImpl @Inject constructor(
    private val dataSource: FavoritesLocalDataSource
) : FavoritesRepository {
    override fun getAll(): Flow<List<Character>> = dataSource.getAll()

    override suspend fun saveFavorite(character: Character) = dataSource.save(character)

    override suspend fun deleteFavorite(character: Character) = dataSource.delete(character)
}