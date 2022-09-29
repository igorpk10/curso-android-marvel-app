package com.example.marvelapp.framework.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.marvelapp.framework.db.entity.RemoteKeyEntity
import com.igaopk10.core.data.DBConstants

@Dao
interface RemoteKeyDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplace(remoteKey: RemoteKeyEntity)

    @Query("SELECT * FROM ${DBConstants.REMOTE_KEYS_TABLE_NAME}")
    suspend fun remoteKey(): RemoteKeyEntity

    @Query("DELETE FROM ${DBConstants.REMOTE_KEYS_TABLE_NAME}")
    suspend fun clearAll()
}