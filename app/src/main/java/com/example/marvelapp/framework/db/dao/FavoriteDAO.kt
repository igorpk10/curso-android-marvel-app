package com.example.marvelapp.framework.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Insert
import androidx.room.Delete
import androidx.room.OnConflictStrategy
import com.example.marvelapp.framework.db.entity.FavoriteEntity
import com.igaopk10.core.data.DBConstants.FAVORITES_TABLE_NAME
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDAO {

    @Query("SELECT * FROM $FAVORITES_TABLE_NAME")
    fun loadFavorites(): Flow<List<FavoriteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favoriteEntity: FavoriteEntity)

    @Delete
    suspend fun deleteFavorite(favoriteEntity: FavoriteEntity)

    @Query("SELECT * FROM $FAVORITES_TABLE_NAME WHERE id = :characterID")
    suspend fun hasFavorite(characterID: Int): FavoriteEntity?
}