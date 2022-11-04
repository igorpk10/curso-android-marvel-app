package com.igaopk10.core.usecase

import com.igaopk10.core.data.mapper.SortingMapper
import com.igaopk10.core.data.repository.FavoritesRepository
import com.igaopk10.core.data.repository.StorageRepository
import com.igaopk10.core.domain.model.Character
import com.igaopk10.core.usecase.base.CoroutinesDispatchers
import com.igaopk10.core.usecase.base.ResultStatus
import com.igaopk10.core.usecase.base.UseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface SaveCharactersSortingUseCase {

    operator fun invoke(params: Params): Flow<ResultStatus<Unit>>

    data class Params(
        val sortingPair: Pair<String, String>
    )
}

class SaveCharactersSortingUseCaseImpl @Inject constructor(
    private val repository: StorageRepository,
    private val sortingMapper: SortingMapper,
    private val dispatchers: CoroutinesDispatchers
) : UseCase<SaveCharactersSortingUseCase.Params, Unit>(), SaveCharactersSortingUseCase {


    override suspend fun doWork(params: SaveCharactersSortingUseCase.Params): ResultStatus<Unit> {
        return withContext(dispatchers.io()){
            repository.saveSorting(sortingMapper.mapFromPair(params.sortingPair))
            ResultStatus.Success(Unit)
        }
    }
}