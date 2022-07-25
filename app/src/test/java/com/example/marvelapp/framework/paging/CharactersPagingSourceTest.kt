package com.example.marvelapp.framework.paging

import androidx.paging.PagingSource
import com.example.marvelapp.factory.response.CharacterPagingFactory
import com.example.testing.MainCoroutineRule
import com.example.testing.model.CharacterFactory
import com.igaopk10.core.data.repository.CharactersRemoteDataSource
import com.igaopk10.core.domain.model.Character
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.lang.RuntimeException

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class CharactersPagingSourceTest() {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Mock
    lateinit var remoteDataSource: CharactersRemoteDataSource

    private lateinit var charactersPagingSource: CharactersPagingSource

    private val charactersFactory = CharacterFactory()

    private val dataWrapperResponseFactory = CharacterPagingFactory()

    @Before
    fun setup() {
        charactersPagingSource = CharactersPagingSource(remoteDataSource, "")
    }

    @Test
    fun `should return a sucess load result when load is called`() =
        runTest {
            whenever(remoteDataSource.fetchCharacters(any()))
                .thenReturn(dataWrapperResponseFactory.create())


            val result = charactersPagingSource.load(
                PagingSource.LoadParams.Refresh(
                    key = null,
                    loadSize = 2,
                    placeholdersEnabled = false
                )
            )

            val expected = listOf(
                charactersFactory.create(CharacterFactory.Hero.OnePunchMan)
            )

            assertEquals(
                PagingSource.LoadResult.Page(
                    data = expected,
                    prevKey = null,
                    nextKey = 20
                ),
                result
            )
        }

    @Test

    fun `should return a error Load result when load is called`() = runTest {
        //Arrange
        val exception = RuntimeException()

        whenever(remoteDataSource.fetchCharacters(any()))
            .thenThrow(exception)

        //Act
        val result = charactersPagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 2,
                placeholdersEnabled = false
            )
        )

        //Assert
        assertEquals(PagingSource.LoadResult.Error<Int, Character>(exception), result)
    }
}