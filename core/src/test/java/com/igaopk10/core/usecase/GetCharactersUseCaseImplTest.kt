package com.igaopk10.core.usecase

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.testing.MainCoroutineRule
import com.example.testing.model.CharacterFactory
import com.example.testing.pagingSource.PagingConfigFactory
import com.example.testing.pagingSource.PagingSourceFactory
import com.igaopk10.core.data.repository.CharactersRepository
import com.igaopk10.core.data.repository.StorageRepository
import com.igaopk10.core.domain.model.Character
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class GetCharactersUseCaseImplTest() {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Mock
    lateinit var repository: CharactersRepository

    @Mock
    lateinit var storageRepository: StorageRepository

    lateinit var getCharactersUseCase: GetCharactersUseCase

    private val hero = CharacterFactory().create(CharacterFactory.Hero.OnePunchMan)

    private val fakePagingSource = PagingSourceFactory().create(
        listOf(
            hero
        )
    )

    private val pagingConfig = PagingConfigFactory().build()

    private val pagingData = PagingData.from(
        listOf(
            CharacterFactory().create(CharacterFactory.Hero.OnePunchMan)
        )
    )

    @Before
    fun setup() {
        getCharactersUseCase = GetCharactersUseCaseImpl(repository, storageRepository)
    }

    @Test
    fun `should validate flow paging data creation when invoke from use case has called`() =
        runTest {
            val orderBy = "ascending"
            val query = "spider"

            whenever(repository.getCachedCharacter(query, orderBy, pagingConfig)).thenReturn(
                flowOf(pagingData)
            )

            whenever(storageRepository.sorting).thenReturn(
                flowOf(orderBy)
            )


            var result = getCharactersUseCase.invoke(
                GetCharactersUseCase.GetCharactersParams(
                    query,
                    pagingConfig
                )
            )

            verify(repository).getCachedCharacter(query, orderBy, pagingConfig)
            assertNotNull(result.first())
        }

}