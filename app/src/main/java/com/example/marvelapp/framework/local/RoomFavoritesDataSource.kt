package com.example.marvelapp.framework.local

import com.example.marvelapp.framework.db.dao.FavoriteDAO
import com.example.marvelapp.framework.db.entity.FavoriteEntity
import com.example.marvelapp.framework.db.entity.toCharacterModel
import com.igaopk10.core.data.repository.FavoritesLocalDataSource
import com.igaopk10.core.domain.model.Character
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RoomFavoritesDataSource @Inject constructor(
    private val favoriteDAO: FavoriteDAO
) : FavoritesLocalDataSource {
    override fun getAll(): Flow<List<Character>> = favoriteDAO.loadFavorites().map {
        it.toCharacterModel()
    }

    override suspend fun save(character: Character) {
        favoriteDAO.insertFavorite(character.toFavoriteEntity())
    }

    override suspend fun delete(character: Character) {
        favoriteDAO.deleteFavorite(character.toFavoriteEntity())
    }

    private fun Character.toFavoriteEntity() = FavoriteEntity(id, name, imageUrl)
}