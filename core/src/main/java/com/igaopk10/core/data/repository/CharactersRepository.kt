package com.igaopk10.core.data.repository

import androidx.paging.PagingSource
import com.igaopk10.core.domain.model.Character
import com.igaopk10.core.domain.model.Comic
import com.igaopk10.core.domain.model.Event

interface CharactersRepository {

    fun getCharacters(query: String): PagingSource<Int, Character>

    suspend fun getComics(characterId: Int): List<Comic>

    suspend fun getEvents(characterId: Int): List<Event>

}