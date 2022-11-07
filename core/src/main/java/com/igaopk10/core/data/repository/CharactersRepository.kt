package com.igaopk10.core.data.repository

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.igaopk10.core.domain.model.Character
import com.igaopk10.core.domain.model.Comic
import com.igaopk10.core.domain.model.Event
import kotlinx.coroutines.flow.Flow

interface CharactersRepository {

    fun getCachedCharacter(query: String, orderBy: String ,pageConfig: PagingConfig): Flow<PagingData<Character>>

    suspend fun getComics(characterId: Int): List<Comic>

    suspend fun getEvents(characterId: Int): List<Event>

}