package com.igaopk10.core.data.repository

import com.igaopk10.core.domain.model.CharacterPaging
import com.igaopk10.core.domain.model.Comic
import com.igaopk10.core.domain.model.Event

interface CharactersRemoteDataSource {

    suspend fun fetchCharacters(queries: Map<String, String>): CharacterPaging

    suspend fun fetchComics(characterId: Int): List<Comic>

    suspend fun fetchEvents(characterId: Int): List<Event>
}