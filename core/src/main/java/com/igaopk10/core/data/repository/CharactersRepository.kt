package com.igaopk10.core.data.repository

import androidx.paging.PagingSource
import com.igaopk10.core.domain.model.Character

interface CharactersRepository {

    fun getCharacters(query: String): PagingSource<Int, Character>


}