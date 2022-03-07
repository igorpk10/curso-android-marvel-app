package com.example.marvelapp.framework.remote

import com.example.marvelapp.framework.network.MarvelAPI
import com.example.marvelapp.framework.network.response.DataWrapperResponse
import com.igaopk10.core.data.repository.CharactersRemoteDataSource
import javax.inject.Inject

class RetrofitCharactersDataSource @Inject constructor(
    private val marvelAPI: MarvelAPI
) : CharactersRemoteDataSource<DataWrapperResponse> {

    override suspend fun fetchCharacters(queries: Map<String, String>): DataWrapperResponse {
        return marvelAPI.getCharacters(queries)
    }

}