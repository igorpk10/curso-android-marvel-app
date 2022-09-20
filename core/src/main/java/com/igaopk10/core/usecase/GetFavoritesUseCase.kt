package com.igaopk10.core.usecase

import com.igaopk10.core.data.repository.FavoritesRepository
import com.igaopk10.core.domain.model.Character
import com.igaopk10.core.usecase.base.CoroutinesDispatchers
import com.igaopk10.core.usecase.base.FlowUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface GetFavoritesUseCase {
    suspend operator fun invoke(params: Unit = Unit): Flow<List<Character>>
}

class GetFavoritesUseCaseImpl @Inject constructor(
    private val repository: FavoritesRepository,
    private val dispatchers: CoroutinesDispatchers
) : FlowUseCase<Unit, List<Character>>(), GetFavoritesUseCase {

    override suspend fun createFlowObservable(params: Unit): Flow<List<Character>> {
        return withContext(dispatchers.io()) {
            repository.getAll()
        }
    }
}