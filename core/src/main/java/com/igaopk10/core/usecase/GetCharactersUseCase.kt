package com.igaopk10.core.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.igaopk10.core.data.repository.CharactersRepository
import com.igaopk10.core.domain.model.Character
import com.igaopk10.core.usecase.base.PagingUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCharactersUseCase @Inject constructor(
    private val charactersRepository: CharactersRepository,
) : PagingUseCase<GetCharactersUseCase.GetCharactersParams, Character>() {

    data class GetCharactersParams(val query: String, val pagingConfig: PagingConfig)

    override fun createFlowObservable(params: GetCharactersParams): Flow<PagingData<Character>> {
        return Pager(config = params.pagingConfig){
            charactersRepository.getCharacters(params.query)
        }.flow
    }
}