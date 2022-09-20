package com.example.marvelapp.presentation.details.actionstate

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import com.example.marvelapp.R
import com.example.marvelapp.presentation.details.DetailChildVE
import com.example.marvelapp.presentation.details.DetailParentVE
import com.example.marvelapp.presentation.extensions.watchStatus
import com.igaopk10.core.usecase.GetCharacterCategories
import kotlin.coroutines.CoroutineContext

class DetailFragmentUIActionState(
    private val coroutineContext: CoroutineContext,
    private val getCharactersCategoriesUseCase: GetCharacterCategories
) {

    private val action = MutableLiveData<UIAction>()
    val state: LiveData<UiState> = action.switchMap {
        liveData(context = coroutineContext) {
            when (it) {
                is UIAction.Load -> {
                    getCharactersCategoriesUseCase(
                        GetCharacterCategories.GetCharacterParams(it.characterId)
                    ).watchStatus(
                        loading = {
                            emit(UiState.Loading)
                        },
                        success = { data ->
                            val detailParentList: MutableList<DetailParentVE> = mutableListOf()
                            val comics = data.first
                            if (comics.isNotEmpty()) {
                                comics.map {
                                    DetailChildVE(it.id, it.imageURL)
                                }.also {
                                    detailParentList.add(
                                        DetailParentVE(
                                            R.string.details_comics_category,
                                            it
                                        )
                                    )
                                }
                            }

                            val events = data.second
                            if (events.isNotEmpty()) {
                                events.map {
                                    DetailChildVE(it.id, it.imageURL)
                                }.also {
                                    detailParentList.add(
                                        DetailParentVE(
                                            R.string.details_comics_events,
                                            it
                                        )
                                    )
                                }
                            }

                            if (detailParentList.isNotEmpty()) {
                                emit(UiState.Success(detailParentList))
                            } else emit(UiState.Empty)
                        },

                        error = {
                            emit(UiState.Error)
                        }

                    )
                }
            }
        }
    }

    fun load(characterId: Int) {
        action.value = UIAction.Load(characterId)
    }

    sealed class UiState {
        object Loading : UiState()
        data class Success(val detailParentList: List<DetailParentVE>) : UiState()
        object Error : UiState()
        object Empty : UiState()
    }

    sealed class UIAction {
        data class Load(val characterId: Int) : UIAction()
    }
}