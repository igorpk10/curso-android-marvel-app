package com.example.marvelapp.framework.remote

import com.example.marvelapp.framework.network.MarvelAPI
import com.example.marvelapp.framework.network.response.DataWrapperResponse
import com.example.marvelapp.framework.network.response.toCharacterModel
import com.example.marvelapp.framework.network.response.toComicModel
import com.example.marvelapp.framework.network.response.toEventModel
import com.igaopk10.core.data.repository.CharactersRemoteDataSource
import com.igaopk10.core.domain.model.CharacterPaging
import com.igaopk10.core.domain.model.Comic
import com.igaopk10.core.domain.model.Event
import javax.inject.Inject

class RetrofitCharactersDataSource @Inject constructor(
    private val marvelAPI: MarvelAPI
) : CharactersRemoteDataSource {

    override suspend fun fetchCharacters(queries: Map<String, String>): CharacterPaging {
        val data = marvelAPI.getCharacters(queries).data
        val characters = data.results.map {
            it.toCharacterModel()
        }
        return CharacterPaging(
            offset = data.offset,
            total = data.total,
            character = characters
        )
    }

    override suspend fun fetchComics(characterId: Int): List<Comic> {
        return marvelAPI.getComics(characterId).data.results.map {
            it.toComicModel()
        }
    }

    override suspend fun fetchEvents(characterId: Int): List<Event> {
        return marvelAPI.getEvents(characterId).data.results.map {
            it.toEventModel()
        }
    }
}