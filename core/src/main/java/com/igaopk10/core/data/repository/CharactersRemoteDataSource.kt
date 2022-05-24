package com.igaopk10.core.data.repository

import com.igaopk10.core.domain.model.CharacterPaging

interface CharactersRemoteDataSource {

    suspend fun fetchCharacters(queries: Map<String, String>): CharacterPaging
}