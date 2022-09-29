package com.igaopk10.core.usecase

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.igaopk10.core.data.repository.CharactersRepository
import com.igaopk10.core.domain.model.Character
import com.igaopk10.core.usecase.base.PagingUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface GetCharactersUseCase {
    operator fun invoke(params: GetCharactersParams): Flow<PagingData<Character>>

    data class GetCharactersParams(val query: String, val pagingConfig: PagingConfig)

}

class GetCharactersUseCaseImpl @Inject constructor(
    private val charactersRepository: CharactersRepository,
) : PagingUseCase<GetCharactersUseCase.GetCharactersParams, Character>(), GetCharactersUseCase {

    override fun createFlowObservable(params: GetCharactersUseCase.GetCharactersParams): Flow<PagingData<Character>> {
        return charactersRepository.getCachedCharacter(params.query, params.pagingConfig)
    }
}