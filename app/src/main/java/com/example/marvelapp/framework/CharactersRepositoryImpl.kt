package com.example.marvelapp.framework

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.Pager
import androidx.paging.ExperimentalPagingApi
import androidx.paging.map
import androidx.room.Index
import com.example.marvelapp.framework.db.AppDatabase
import com.example.marvelapp.framework.paging.CharactersRemoteMediator
import com.igaopk10.core.data.repository.CharactersRemoteDataSource
import com.igaopk10.core.data.repository.CharactersRepository
import com.igaopk10.core.domain.model.Character
import com.igaopk10.core.domain.model.Comic
import com.igaopk10.core.domain.model.Event
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class CharactersRepositoryImpl @Inject constructor(
    private val remoteDataSource: CharactersRemoteDataSource,
    private val database: AppDatabase
) : CharactersRepository {

    override fun getCachedCharacter(
        query: String,
        orderBy: String,
        pageConfig: PagingConfig
    ): Flow<PagingData<Character>> {
        return Pager(
            config = pageConfig,
            remoteMediator = CharactersRemoteMediator(
                query = query,
                database = database,
                remoteDataSource = remoteDataSource,
                orderBy = orderBy
            )
        ) {
            database.characterDao().pagingSource()
        }.flow.map { pagingData ->
            pagingData.map { characterEntity ->
                Character(
                    id = characterEntity.id,
                    name = characterEntity.name,
                    imageUrl = characterEntity.imageUrl
                )
            }
        }
    }

    override suspend fun getComics(characterId: Int): List<Comic> {
        return remoteDataSource.fetchComics(characterId)
    }

    override suspend fun getEvents(characterId: Int): List<Event> {
        return remoteDataSource.fetchEvents(characterId)
    }
}