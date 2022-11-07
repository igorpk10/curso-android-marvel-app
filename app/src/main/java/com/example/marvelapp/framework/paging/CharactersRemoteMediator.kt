package com.example.marvelapp.framework.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.marvelapp.framework.db.AppDatabase
import com.example.marvelapp.framework.db.entity.CharacterEntity
import com.example.marvelapp.framework.db.entity.RemoteKeyEntity
import com.igaopk10.core.data.repository.CharactersRemoteDataSource
import com.igaopk10.core.domain.model.Character
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class CharactersRemoteMediator @Inject constructor(
    private val query: String,
    private val database: AppDatabase,
    private val remoteDataSource: CharactersRemoteDataSource,
    private val orderBy: String
) : RemoteMediator<Int, CharacterEntity>() {

    private val characterDAO = database.characterDao()
    private val remoteKeyDAO = database.remoteKeyDao()

    @Suppress("ReturnCount")
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CharacterEntity>
    ): MediatorResult {
        return try {
            val offset = when (loadType) {
                LoadType.REFRESH -> 0
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val remoteKey = database.withTransaction {
                        remoteKeyDAO.remoteKey()
                    }

                    if (remoteKey.nextOffset == null) {
                        return MediatorResult.Success(endOfPaginationReached = true)
                    }

                    remoteKey.nextOffset
                }
            }

            val queries = hashMapOf(
                "offset" to offset.toString()
            )

            if (query.isNotEmpty()) {
                queries["nameStartsWith"] = query
            }

            if(orderBy.isNotEmpty()){
                queries["orderBy"] = orderBy
            }

            val characterPaging = remoteDataSource.fetchCharacters(queries)
            val responseOffset = characterPaging.offset
            val responseTotalCharacters = characterPaging.total

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    characterDAO.clearAll()
                    remoteKeyDAO.clearAll()
                }

                remoteKeyDAO.insertOrReplace(
                    RemoteKeyEntity(nextOffset = responseOffset + state.config.pageSize)
                )

                val charactersEntity = characterPaging.character.map {
                    CharacterEntity(
                        id = it.id,
                        name = it.name,
                        imageUrl = it.imageUrl
                    )
                }

                characterDAO.insertAll(charactersEntity)
            }


            MediatorResult.Success(endOfPaginationReached = responseOffset >= responseTotalCharacters)
        } catch (ex: IOException) {
            MediatorResult.Error(ex)
        } catch (ex: HttpException) {
            MediatorResult.Error(ex)
        }
    }
}
