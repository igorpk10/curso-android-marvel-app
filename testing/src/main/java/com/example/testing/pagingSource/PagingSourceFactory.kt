package com.example.testing.pagingSource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.igaopk10.core.domain.model.Character

class PagingSourceFactory {

    fun create(heroes: List<Character>) = object : PagingSource<Int, Character>(){
        override fun getRefreshKey(state: PagingState<Int, Character>): Int = 1

        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Character> {
            return LoadResult.Page(
                data = heroes,
                prevKey = null,
                nextKey = 20
            )
        }

    }
}