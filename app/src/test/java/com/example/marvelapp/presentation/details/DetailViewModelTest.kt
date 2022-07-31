package com.example.marvelapp.presentation.details

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.marvelapp.R
import com.example.marvelapp.presentation.details.actionstate.DetailFragmentUIActionState
import com.example.testing.MainCoroutineRule
import com.example.testing.model.CharacterFactory
import com.example.testing.model.ComicFactory
import com.example.testing.model.EventFactory
import com.igaopk10.core.domain.model.Comic
import com.igaopk10.core.usecase.AddFavoriteUseCase
import com.igaopk10.core.usecase.GetCharacterCategories
import com.igaopk10.core.usecase.base.ResultStatus
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.isA
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class DetailViewModelTest() {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule()
    var mainCoroutineRule = MainCoroutineRule()


    @Mock
    private lateinit var getCharacterCategories: GetCharacterCategories

    @Mock
    private lateinit var getFavoriteUseCase: AddFavoriteUseCase

    @Mock
    private lateinit var uiStateObserver: Observer<DetailFragmentUIActionState.UiState>

    private lateinit var detailViewModel: DetailViewModel

    private val character = CharacterFactory().create(CharacterFactory.Hero.OnePunchMan)
    private val comics = listOf(ComicFactory().create(ComicFactory.FakeComic.FakeComic1))
    private val events = listOf(EventFactory().create(EventFactory.FakeEvent.FakeEvent1))

    @Before
    fun setup() {
        detailViewModel = DetailViewModel(
            useCase = getCharacterCategories,
            addFavoriteUseCase = getFavoriteUseCase,
            coroutineDispatcher = mainCoroutineRule.testDispatcherProvider
        ).apply {
            categories.state.observeForever(uiStateObserver)
        }
    }

    @Test
    fun `should notify uiState with Success from UiState when get character categories returns success`() =
        runTest {
            // Arrange
            whenever(getCharacterCategories.invoke(any()))
                .thenReturn(
                    flowOf(
                        ResultStatus.Success(
                            comics to events
                        )
                    )
                )

            //Act
            detailViewModel.categories.load(character.id)

            //Assert
            verify(uiStateObserver).onChanged(isA<DetailFragmentUIActionState.UiState.Success>())

            val uiStateSuccess =
                detailViewModel.categories.state.value as DetailFragmentUIActionState.UiState.Success
            assertEquals(2, uiStateSuccess.detailParentList.size)
            assertEquals(
                R.string.details_comics_category,
                uiStateSuccess.detailParentList[0].categoryStringResId
            )
        }

    @Test
    fun `should notify uiState with Success from UiState when get character categories returns only comics`() =
        runTest {
            // Arrange
            whenever(getCharacterCategories.invoke(any()))
                .thenReturn(
                    flowOf(
                        ResultStatus.Success(
                            comics to emptyList()
                        )
                    )
                )

            //Act
            detailViewModel.categories.load(character.id)

            //Assert
            verify(uiStateObserver).onChanged(isA<DetailFragmentUIActionState.UiState.Success>())

            val uiStateSuccess =
                detailViewModel.categories.state.value as DetailFragmentUIActionState.UiState.Success
            assertEquals(1, uiStateSuccess.detailParentList.size)
            assertEquals(
                R.string.details_comics_category,
                uiStateSuccess.detailParentList[0].categoryStringResId
            )
        }

    @Test
    fun `should notify uiState with Success from UiState when get character categories returns only events`() =
        runTest {
            // Arrange
            whenever(getCharacterCategories.invoke(any()))
                .thenReturn(
                    flowOf(
                        ResultStatus.Success(
                            emptyList<Comic>() to events
                        )
                    )
                )

            //Act
            detailViewModel.categories.load(character.id)

            //Assert
            verify(uiStateObserver).onChanged(isA<DetailFragmentUIActionState.UiState.Success>())

            val uiStateSuccess =
                detailViewModel.categories.state.value as DetailFragmentUIActionState.UiState.Success
            assertEquals(1, uiStateSuccess.detailParentList.size)
            assertEquals(
                R.string.details_comics_events,
                uiStateSuccess.detailParentList[0].categoryStringResId
            )
        }

    @Test
    fun `should notify uiState with Empty from UiState when get character categories returns an empty result list`() =
        runTest {
            // Arrange
            whenever(getCharacterCategories.invoke(any()))
                .thenReturn(
                    flowOf(
                        ResultStatus.Success(
                            emptyList<Comic>() to emptyList()
                        )
                    )
                )

            //Act
            detailViewModel.categories.load(character.id)

            //Assert
            verify(uiStateObserver).onChanged(isA<DetailFragmentUIActionState.UiState.Empty>())
        }

    @Test
    fun `should notify uiState with Error from UiState when get character categories returns an exception`() =
        runTest {
            // Arrange
            whenever(getCharacterCategories.invoke(any()))
                .thenReturn(
                    flowOf(
                        ResultStatus.Error(Throwable())
                    )
                )

            //Act
            detailViewModel.categories.load(character.id)

            //Assert
            verify(uiStateObserver).onChanged(isA<DetailFragmentUIActionState.UiState.Error>())
        }

}