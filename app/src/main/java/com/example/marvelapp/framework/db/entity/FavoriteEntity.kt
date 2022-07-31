package com.example.marvelapp.framework.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.igaopk10.core.data.DBConstants.FAVORITES_COLUMN_INFO_ID
import com.igaopk10.core.data.DBConstants.FAVORITES_COLUMN_INFO_IMAGE_URL
import com.igaopk10.core.data.DBConstants.FAVORITES_COLUMN_INFO_NAME
import com.igaopk10.core.data.DBConstants.FAVORITES_TABLE_NAME
import com.igaopk10.core.domain.model.Character


@Entity(tableName = FAVORITES_TABLE_NAME)
data class FavoriteEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = FAVORITES_COLUMN_INFO_ID)
    val id: Int,
    @ColumnInfo(name = FAVORITES_COLUMN_INFO_NAME)
    val name: String,
    @ColumnInfo(name = FAVORITES_COLUMN_INFO_IMAGE_URL)
    val imageUrl: String
)

fun List<FavoriteEntity>.toCharacterModel() = map {
    Character(it.id, it.name, it.imageUrl)
}
