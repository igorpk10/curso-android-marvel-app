package com.example.marvelapp.presentation.details.actionstate

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.liveData
import androidx.lifecycle.LiveDataScope
import com.example.marvelapp.R
import com.example.marvelapp.presentation.details.DetailViewArgs
import com.example.marvelapp.presentation.extensions.watchStatus
import com.igaopk10.core.usecase.AddFavoriteUseCase
import com.igaopk10.core.usecase.CheckFavoriteUseCase
import com.igaopk10.core.usecase.RemoveFavoriteUseCase
import kotlin.coroutines.CoroutineContext

class DetailFragmentFavoriteUIActionState(
    private val coroutineContext: CoroutineContext,
    private val checkFavoriteUseCase: CheckFavoriteUseCase,
    private val addFavoriteUseCase: AddFavoriteUseCase,
    private val removeFavoriteUseCase: RemoveFavoriteUseCase
) {

    @set:VisibleForTesting
    var currentFavoriteIcon = R.drawable.ic_not_favorite_unchecked

    private val action = MutableLiveData<UIAction>()
    val state: LiveData<UIState> = action.switchMap {
        liveData(context = coroutineContext) {
            when (it) {
                is UIAction.CheckFavorite -> {
                    checkFavoriteUseCase.invoke(
                        CheckFavoriteUseCase.Params(it.characterId)
                    ).watchStatus(
                        success = { isFavorite ->
                            currentFavoriteIcon = if (isFavorite) {
                                R.drawable.ic_favorite_checked
                            } else R.drawable.ic_not_favorite_unchecked

                            emitFavoriteIcon()
                        },
                        error = {}
                    )
                }
                is UIAction.AddFavorite -> {
                    it.detailViewArgs.run {
                        addFavoriteUseCase.invoke(
                            AddFavoriteUseCase.Params(characterId, name, imageURL)
                        )
                    }.watchStatus(
                        loading = {
                            emit(UIState.Loading)
                        },
                        success = {
                            currentFavoriteIcon = R.drawable.ic_favorite_checked
                            emitFavoriteIcon()
                        },
                        error = {
                            emit(UIState.Error(R.string.error_add_favorite))
                        }
                    )
                }
                is UIAction.RemoveFavorite -> {
                    it.detailViewArgs.run {
                        removeFavoriteUseCase.invoke(
                            RemoveFavoriteUseCase.Params(characterId, name, imageURL)
                        ).watchStatus(
                            loading = {
                                emit(UIState.Loading)
                            },
                            success = {
                                currentFavoriteIcon = R.drawable.ic_not_favorite_unchecked
                                emitFavoriteIcon()
                            },
                            error = {
                                emit(UIState.Error(R.string.error_remove_favorite))
                            }
                        )
                    }
                }
            }
        }
    }

    private suspend fun LiveDataScope<UIState>.emitFavoriteIcon() {
        emit(UIState.Success(currentFavoriteIcon))
    }

    fun checkFavorite(characterId: Int) {
        action.value = UIAction.CheckFavorite(characterId)
    }

    fun update(detailViewArgs: DetailViewArgs) {
        action.value = if (currentFavoriteIcon == R.drawable.ic_not_favorite_unchecked) {
            UIAction.AddFavorite(detailViewArgs)
        } else UIAction.RemoveFavorite(detailViewArgs)
    }

    sealed class UIState {
        object Loading : UIState()
        class Success(@DrawableRes val icon: Int) : UIState()
        data class Error(@StringRes val messageResId: Int) : UIState()
    }

    sealed class UIAction {
        data class CheckFavorite(val characterId: Int) : UIAction()
        data class AddFavorite(val detailViewArgs: DetailViewArgs) : UIAction()
        data class RemoveFavorite(val detailViewArgs: DetailViewArgs) : UIAction()
    }
}