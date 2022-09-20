package com.example.marvelapp.framework.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.marvelapp.framework.db.dao.FavoriteDAO
import com.example.marvelapp.framework.db.entity.FavoriteEntity

@Database(entities = [FavoriteEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun favoriteDAO(): FavoriteDAO

}