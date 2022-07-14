package com.example.marvelapp.factory.response

import com.example.marvelapp.framework.network.response.CharacterResponse
import com.example.marvelapp.framework.network.response.DataContainerResponse
import com.example.marvelapp.framework.network.response.DataWrapperResponse
import com.example.marvelapp.framework.network.response.ThumbnailResponse
import com.example.testing.model.CharacterFactory
import com.igaopk10.core.domain.model.Character
import com.igaopk10.core.domain.model.CharacterPaging

class CharacterPagingFactory {

    fun create() = CharacterPaging(
        offset = 0,
        total = 2,
        character = listOf(
            Character(
                id = 0,
                name = "One Punch Man",
                imageUrl = "https://igaopk10.com.br/onepunchman.jpg"
            )
        )
    )
}