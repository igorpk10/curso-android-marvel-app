package com.example.marvelapp.framework.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.marvelapp.framework.db.dao.CharacterDAO
import com.example.marvelapp.framework.db.dao.FavoriteDAO
import com.example.marvelapp.framework.db.dao.RemoteKeyDAO
import com.example.marvelapp.framework.db.entity.CharacterEntity
import com.example.marvelapp.framework.db.entity.FavoriteEntity
import com.example.marvelapp.framework.db.entity.RemoteKeyEntity

@Database(
    entities = [
        FavoriteEntity::class,
        CharacterEntity::class,
        RemoteKeyEntity::class
    ], version = 2, exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun favoriteDAO(): FavoriteDAO

    abstract fun characterDao(): CharacterDAO

    abstract fun remoteKeyDao(): RemoteKeyDAO
}
