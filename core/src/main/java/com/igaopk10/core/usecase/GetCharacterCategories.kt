package com.igaopk10.core.usecase

import com.igaopk10.core.data.repository.CharactersRepository
import com.igaopk10.core.domain.model.Comic
import com.igaopk10.core.domain.model.Event
import com.igaopk10.core.usecase.base.AppCoroutinesDispatcher
import com.igaopk10.core.usecase.base.ResultStatus
import com.igaopk10.core.usecase.base.UseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface GetCharacterCategories {

    operator fun invoke(params: GetCharacterParams): Flow<ResultStatus<Pair<List<Comic>, List<Event>>>>

    data class GetCharacterParams(val chacterId: Int)
}

class GetCharacterCategoriesImpl @Inject constructor(
    private val repository: CharactersRepository,
    private val dispatchers: AppCoroutinesDispatcher
) : GetCharacterCategories,
    UseCase<GetCharacterCategories.GetCharacterParams, Pair<List<Comic>, List<Event>>>() {
    override suspend fun doWork(
        params: GetCharacterCategories.GetCharacterParams
    ): ResultStatus<Pair<List<Comic>, List<Event>>> {
        return withContext(dispatchers.io) {
            val comicsDeferred = async { repository.getComics(params.chacterId) }
            val eventDeferred = async { repository.getEvents(params.chacterId) }

            val comics = comicsDeferred.await()
            val events = eventDeferred.await()

            ResultStatus.Success(comics to events)
        }
    }
}