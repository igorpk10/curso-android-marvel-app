package com.example.marvelapp.framework.remote

import com.example.marvelapp.framework.network.MarvelAPI
import com.example.marvelapp.framework.network.response.DataWrapperResponse
import com.example.marvelapp.framework.network.response.toCharacterModel
import com.igaopk10.core.data.repository.CharactersRemoteDataSource
import com.igaopk10.core.domain.model.CharacterPaging
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

}