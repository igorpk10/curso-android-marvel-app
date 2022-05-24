package com.example.marvelapp.framework

import androidx.paging.PagingSource
import com.example.marvelapp.framework.paging.CharactersPagingSource
import com.igaopk10.core.data.repository.CharactersRemoteDataSource
import com.igaopk10.core.data.repository.CharactersRepository
import com.igaopk10.core.domain.model.Character
import javax.inject.Inject

class CharactersRepositoryImpl @Inject constructor(
    private val remoteDataSource: CharactersRemoteDataSource
) : CharactersRepository {
    override fun getCharacters(query: String): PagingSource<Int, Character> {
        return CharactersPagingSource(remoteDataSource, query)
    }
}