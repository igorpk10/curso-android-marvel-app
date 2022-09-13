package com.example.marvelapp.presentation.characters

import androidx.paging.PagingData
import com.example.testing.MainCoroutineRule
import com.example.testing.model.CharacterFactory
import com.igaopk10.core.usecase.GetCharactersUseCase
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import junit.framework.Assert.assertNotNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class CharactersViewModelTest {

    @get:Rule()
    var mainCoroutineRule = MainCoroutineRule()

    @Mock
    lateinit var getCharactersUseCase: GetCharactersUseCase

    private lateinit var viewModel: CharactersViewModel

    private val pagingDataCharacters = PagingData.from(
        listOf(
            CharacterFactory().create(CharacterFactory.Hero.OnePunchMan)
        )
    )

    @Before
    fun setup() {
        viewModel = CharactersViewModel(getCharactersUseCase, mainCoroutineRule.testDispatcherProvider)
    }

    @Test
    fun `should validate the paging data object values when calling charactersPagingData`() =
        runTest {
            whenever(
                getCharactersUseCase.invoke(any())
            ).thenReturn(
                flowOf(
                    pagingDataCharacters
                )
            )

            val result = viewModel.charactersPagingData("")

            assertNotNull(result.first())
        }


    @Test(expected = RuntimeException::class)
    fun `should throw an exception when the calling to the use case returns an exception`() =
        runTest {
            whenever(getCharactersUseCase.invoke(any()))
                .thenThrow(RuntimeException())

            viewModel.charactersPagingData("")
        }
}