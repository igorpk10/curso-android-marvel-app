package com.example.marvelapp.framework

import androidx.paging.PagingSource
import com.example.marvelapp.framework.paging.CharactersPagingSource
import com.igaopk10.core.data.repository.CharactersRemoteDataSource
import com.igaopk10.core.data.repository.CharactersRepository
import com.igaopk10.core.domain.model.Character
import com.igaopk10.core.domain.model.Comic
import com.igaopk10.core.domain.model.Event
import javax.inject.Inject

class CharactersRepositoryImpl @Inject constructor(
    private val remoteDataSource: CharactersRemoteDataSource
) : CharactersRepository {
    override fun getCharacters(query: String): PagingSource<Int, Character> {
        return CharactersPagingSource(remoteDataSource, query)
    }

    override suspend fun getComics(characterId: Int): List<Comic> {
        return remoteDataSource.fetchComics(characterId)
    }

    override suspend fun getEvents(characterId: Int): List<Event> {
        return remoteDataSource.fetchEvents(characterId)
    }
}