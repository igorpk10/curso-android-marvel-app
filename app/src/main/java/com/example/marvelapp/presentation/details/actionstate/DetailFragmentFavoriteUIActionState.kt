package com.example.marvelapp.presentation.details.actionstate

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import com.example.marvelapp.R
import com.example.marvelapp.presentation.details.DetailViewArgs
import com.example.marvelapp.presentation.extensions.watchStatus
import com.igaopk10.core.usecase.AddFavoriteUseCase
import com.igaopk10.core.usecase.CheckFavoriteUseCase
import kotlin.coroutines.CoroutineContext

class DetailFragmentFavoriteUIActionState(
    private val coroutineContext: CoroutineContext,
    private val checkFavoriteUseCase: CheckFavoriteUseCase,
    private val addFavoriteUseCase: AddFavoriteUseCase
) {

    private val action = MutableLiveData<UIAction>()
    val state: LiveData<UIState> = action.switchMap {
        liveData(context = coroutineContext) {
            when (it) {
                is UIAction.CheckFavorite -> {
                    checkFavoriteUseCase.invoke(
                        CheckFavoriteUseCase.Params(it.characterId)
                    ).watchStatus(
                        success = { isFavorite ->
                            val icon = if(isFavorite) {
                                R.drawable.ic_favorite_checked
                            }else  R.drawable.ic_not_favorite_unchecked

                            emit(UIState.Success(icon))
                        },
                        error = {}
                    )
                }
                is UIAction.Update -> {
                    it.detailViewArgs.run {
                        addFavoriteUseCase.invoke(
                            AddFavoriteUseCase.Params(characterId, name, imageURL)
                        )
                    }.watchStatus(
                        loading = {
                            emit(UIState.Loading)
                        },
                        success = {
                            emit(UIState.Success(R.drawable.ic_favorite_checked))
                        },
                        error = {
                            emit(UIState.Error(R.string.error_add_favorite))
                        }
                    )
                }
            }
        }
    }


    fun checkFavorite(characterId: Int) {
        action.value = UIAction.CheckFavorite(characterId)
    }

    fun update(detailViewArgs: DetailViewArgs) {
        action.value = UIAction.Update(detailViewArgs)
    }

    sealed class UIState {
        object Loading : UIState()
        class Success(@DrawableRes val icon: Int) : UIState()
        data class Error(@StringRes val messageResId: Int) : UIState()
    }

    sealed class UIAction {
        data class CheckFavorite(val characterId: Int) : UIAction()
        data class Update(val detailViewArgs: DetailViewArgs) : UIAction()
    }
}