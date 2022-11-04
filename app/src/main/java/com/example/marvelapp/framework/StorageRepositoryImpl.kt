package com.example.marvelapp.framework

import com.igaopk10.core.data.repository.StorageLocalDataSource
import com.igaopk10.core.data.repository.StorageRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class StorageRepositoryImpl @Inject constructor(
    private val storage: StorageLocalDataSource
) : StorageRepository {
    override val sorting: Flow<String>
        get() = storage.sorting

    override suspend fun saveSorting(sorting: String) {
        storage.saveSorting(sorting)
    }
}