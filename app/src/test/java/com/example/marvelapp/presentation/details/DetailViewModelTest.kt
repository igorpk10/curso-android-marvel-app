package com.example.marvelapp.presentation.details

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.marvelapp.R
import com.example.marvelapp.presentation.details.actionstate.DetailFragmentFavoriteUIActionState
import com.example.marvelapp.presentation.details.actionstate.DetailFragmentUIActionState
import com.example.testing.MainCoroutineRule
import com.example.testing.model.CharacterFactory
import com.example.testing.model.ComicFactory
import com.example.testing.model.EventFactory
import com.igaopk10.core.domain.model.Comic
import com.igaopk10.core.usecase.AddFavoriteUseCase
import com.igaopk10.core.usecase.CheckFavoriteUseCase
import com.igaopk10.core.usecase.GetCharacterCategories
import com.igaopk10.core.usecase.RemoveFavoriteUseCase
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
    private lateinit var getCheckFavoriteUseCase: CheckFavoriteUseCase

    @Mock
    private lateinit var getRemoveFavoriteUseCase: RemoveFavoriteUseCase

    @Mock
    private lateinit var uiStateObserver: Observer<DetailFragmentUIActionState.UiState>

    @Mock
    private lateinit var favoriteUIStateObserver: Observer<DetailFragmentFavoriteUIActionState.UIState>

    private lateinit var detailViewModel: DetailViewModel

    private val character = CharacterFactory().create(CharacterFactory.Hero.OnePunchMan)
    private val comics = listOf(ComicFactory().create(ComicFactory.FakeComic.FakeComic1))
    private val events = listOf(EventFactory().create(EventFactory.FakeEvent.FakeEvent1))

    @Before
    fun setup() {
        detailViewModel = DetailViewModel(
            useCase = getCharacterCategories,
            addFavoriteUseCase = getFavoriteUseCase,
            coroutineDispatcher = mainCoroutineRule.testDispatcherProvider,
            checkFavoriteUseCase = getCheckFavoriteUseCase,
            removeFavoriteUseCase = getRemoveFavoriteUseCase
        ).apply {
            categories.state.observeForever(uiStateObserver)
            favorite.state.observeForever(favoriteUIStateObserver)
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


    @Test
    fun `should notify favorite_uiState with filled favorite icon when check favorite returns true`() =
        runTest {
            // Arrange
            whenever(getCheckFavoriteUseCase.invoke(any()))
                .thenReturn(
                    flowOf(
                        ResultStatus.Success(true)
                    )
                )

            // Action

            detailViewModel.favorite.checkFavorite(character.id)

            // Assert
            verify(favoriteUIStateObserver).onChanged(
                isA<DetailFragmentFavoriteUIActionState.UIState.Success>()
            )

            val result =
                detailViewModel.favorite.state.value as DetailFragmentFavoriteUIActionState.UIState.Success
            assertEquals(result.icon, R.drawable.ic_favorite_checked)
        }

    @Test
    fun `should notify favorite_uiState with not filled favorite icon when check favorite returns false`() =
        runTest {
            // Arrange
            whenever(getCheckFavoriteUseCase.invoke(any()))
                .thenReturn(
                    flowOf(
                        ResultStatus.Success(false)
                    )
                )

            // Action

            detailViewModel.favorite.checkFavorite(character.id)

            // Assert
            verify(favoriteUIStateObserver).onChanged(
                isA<DetailFragmentFavoriteUIActionState.UIState.Success>()
            )

            val result =
                detailViewModel.favorite.state.value as DetailFragmentFavoriteUIActionState.UIState.Success
            assertEquals(result.icon, R.drawable.ic_not_favorite_unchecked)
        }


    @Test
    fun `should notify favorite_uiState with filled favorite icon when current icon is unchecked`() =
        runTest {
            // Arrange
            whenever(getFavoriteUseCase.invoke(any()))
                .thenReturn(
                    flowOf(
                        ResultStatus.Success(Unit)
                    )
                )

            // Act
            detailViewModel.run {
                favorite.currentFavoriteIcon = R.drawable.ic_not_favorite_unchecked
                favorite.update(
                    DetailViewArgs(character.id, character.name, character.imageUrl)
                )
            }

            // Assert
            verify(favoriteUIStateObserver).onChanged(isA<DetailFragmentFavoriteUIActionState.UIState.Success>())
            val uiState =
                detailViewModel.favorite.state.value as DetailFragmentFavoriteUIActionState.UIState.Success
            assertEquals(R.drawable.ic_favorite_checked, uiState.icon)
        }

    @Test
    fun `should call remove and notify favorite_uiState with filled favorite icon when current icon is checked`() =
        runTest {
            // Arrange
            whenever(getRemoveFavoriteUseCase.invoke(any()))
                .thenReturn(
                    flowOf(
                        ResultStatus.Success(Unit)
                    )
                )

            // Act
            detailViewModel.run {
                favorite.currentFavoriteIcon = R.drawable.ic_favorite_checked
                favorite.update(
                    DetailViewArgs(character.id, character.name, character.imageUrl)
                )
            }

            // Assert
            verify(favoriteUIStateObserver).onChanged(isA<DetailFragmentFavoriteUIActionState.UIState.Success>())
            val uiState =
                detailViewModel.favorite.state.value as DetailFragmentFavoriteUIActionState.UIState.Success
            assertEquals(R.drawable.ic_not_favorite_unchecked, uiState.icon)
        }

}