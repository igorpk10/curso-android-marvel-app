package com.example.marvelapp.framework.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.igaopk10.core.data.repository.CharactersRemoteDataSource
import com.igaopk10.core.domain.model.Character

//Implementação antiga do mediator sem cache local
class CharactersPagingSource(
    private val remoteDataSource: CharactersRemoteDataSource,
    private val query: String
) : PagingSource<Int, Character>() {

    companion object {
        private const val LIMIT = 20
    }

    @Suppress("TooGenericExceptionCaught")
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Character> {
        return try {
            val offset = params.key ?: 0
            val queries = hashMapOf(
                "offset" to offset.toString()
            )

            if (query.isNotEmpty()) {
                queries["nameStartsWith"] = query
            }

            val characterPaging = remoteDataSource.fetchCharacters(queries)
            val responseOffset = characterPaging.offset
            val responseTotalCharacters = characterPaging.total

            LoadResult.Page(
                data = characterPaging.character,
                prevKey = null,
                nextKey = if (responseOffset < responseTotalCharacters) {
                    responseOffset + LIMIT
                } else {
                    null
                }
            )
        } catch (ex: Exception) {
            LoadResult.Error(ex)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Character>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(LIMIT) ?: anchorPage?.nextKey?.minus(LIMIT)
        }
    }
}