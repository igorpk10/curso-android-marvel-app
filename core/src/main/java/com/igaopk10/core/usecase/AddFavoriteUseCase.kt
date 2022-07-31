package com.igaopk10.core.usecase

import com.igaopk10.core.data.repository.FavoritesRepository
import com.igaopk10.core.domain.model.Character
import com.igaopk10.core.usecase.base.CoroutinesDispatchers
import com.igaopk10.core.usecase.base.ResultStatus
import com.igaopk10.core.usecase.base.UseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface AddFavoriteUseCase {

    operator fun invoke(params: Params): Flow<ResultStatus<Unit>>

    data class Params(
        val characterId: Int,
        val characterName: String,
        val characterImageURL: String
    )
}

class AddFavoriteUseCaseImpl @Inject constructor(
    private val repository: FavoritesRepository,
    private val dispatchers: CoroutinesDispatchers
) : UseCase<AddFavoriteUseCase.Params, Unit>(), AddFavoriteUseCase {

    override suspend fun doWork(params: AddFavoriteUseCase.Params): ResultStatus<Unit> {
        return withContext(dispatchers.io()) {
            repository.saveFavorite(
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