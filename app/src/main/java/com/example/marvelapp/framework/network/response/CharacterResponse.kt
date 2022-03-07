package com.example.marvelapp.framework.network.response

import com.igaopk10.core.domain.model.Character

data class CharacterResponse(
    val id: String,
    val name: String,
    val thumbNail: ThumbnailResponse
)

fun CharacterResponse.toCharacterModel(): Character{
    return Character(
        name = this.name,
        imageUrl = "${this.thumbNail.path}.${thumbNail.extension}"
    )
}
