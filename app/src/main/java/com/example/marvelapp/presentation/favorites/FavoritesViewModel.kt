package com.example.marvelapp.presentation.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.liveData
import com.igaopk10.core.usecase.GetFavoritesUseCase
import com.igaopk10.core.usecase.base.CoroutinesDispatchers
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    useCase: GetFavoritesUseCase,
    coroutineDispatcher: CoroutinesDispatchers
) : ViewModel() {

    private val action = MutableLiveData<Action>()
    val state: LiveData<UIState> = action.switchMap {
        liveData(coroutineDispatcher.main()) {
            when (it) {
                is Action.GetAll -> {
                    useCase.invoke().catch {
                        emit(UIState.ShowEmpty)
                    }.collect {
                        val itens = it.map { character ->
                            FavoriteItem(character.id, character.name, character.imageUrl)
                        }

                        val uiState = if (itens.isEmpty())
                            UIState.ShowEmpty
                        else
                            UIState.ShowFavorites(itens)

                        emit(uiState)
                    }
                }
            }
        }
    }

    fun getAll() {
        action.value = Action.GetAll
    }

    sealed class UIState {
        data class ShowFavorites(val favorites: List<FavoriteItem>) : UIState()
        object ShowEmpty : UIState()
    }

    sealed class Action {
        object GetAll : Action()
    }
}
