package com.igaopk10.core.data.repository

import com.igaopk10.core.domain.model.Character
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {

    fun getAll(): Flow<List<Character>>

    suspend fun saveFavorite(character: Character)

    suspend fun deleteFavorite(character: Character)

    suspend fun isFavorite(characterId: Int): Boolean
}