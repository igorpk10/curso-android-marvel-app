package com.igaopk10.core.usecase

import com.igaopk10.core.data.repository.FavoritesRepository
import com.igaopk10.core.usecase.base.CoroutinesDispatchers
import com.igaopk10.core.usecase.base.ResultStatus
import com.igaopk10.core.usecase.base.UseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject


interface CheckFavoriteUseCase {

    operator fun invoke(params: Params): Flow<ResultStatus<Boolean>>

    data class Params(
        val characterId: Int
    )
}

class CheckFavoriteUseCaseImpl @Inject constructor(
    private val repository: FavoritesRepository,
    private val dispatchers: CoroutinesDispatchers
) : UseCase<CheckFavoriteUseCase.Params, Boolean>(), CheckFavoriteUseCase {
    override suspend fun doWork(params: CheckFavoriteUseCase.Params): ResultStatus<Boolean> {
        return withContext(dispatchers.io()) {
            val isFavorite = repository.isFavorite(params.characterId)
            ResultStatus.Success(isFavorite)
        }
    }
}
