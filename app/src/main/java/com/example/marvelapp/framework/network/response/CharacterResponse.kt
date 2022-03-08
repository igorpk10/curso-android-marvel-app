package com.example.marvelapp.framework.network.response

import com.google.gson.annotations.SerializedName
import com.igaopk10.core.domain.model.Character

data class CharacterResponse(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("thumbnail")
    val thumbNail: ThumbnailResponse
)

fun CharacterResponse.toCharacterModel(): Character{
    return Character(
        name = this.name,
        imageUrl = "${this.thumbNail.path}.${thumbNail.extension}".replace("http", "https")
    )
}
