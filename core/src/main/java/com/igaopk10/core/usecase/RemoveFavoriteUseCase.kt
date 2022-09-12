package com.igaopk10.core.usecase

import com.igaopk10.core.data.repository.FavoritesRepository
import com.igaopk10.core.domain.model.Character
import com.igaopk10.core.usecase.base.CoroutinesDispatchers
import com.igaopk10.core.usecase.base.ResultStatus
import com.igaopk10.core.usecase.base.UseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface RemoveFavoriteUseCase {

    operator fun invoke(params: Params): Flow<ResultStatus<Unit>>

    data class Params(
        val characterId: Int,
        val characterName: String,
        val characterImageURL: String
    )
}

class RemoveFavoriteUseCaseImpl @Inject constructor(
    private val repository: FavoritesRepository,
    private val dispatchers: CoroutinesDispatchers
) : UseCase<RemoveFavoriteUseCase.Params, Unit>(), RemoveFavoriteUseCase {

    override suspend fun doWork(params: RemoveFavoriteUseCase.Params): ResultStatus<Unit> {
        return withContext(dispatchers.io()) {
            repository.deleteFavorite(
                Character(
                    id = params.characterId,
                    name = params.characterName,
                    imageUrl = params.characterImageURL
                )
            )
            ResultStatus.Success(Unit)
        }
    }

}