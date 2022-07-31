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
import kotlin.coroutines.CoroutineContext

class DetailFragmentFavoriteUIActionState(
    private val coroutineContext: CoroutineContext,
    private val addFavoriteUseCase: AddFavoriteUseCase
) {

    private val action = MutableLiveData<UIAction>()
    val state: LiveData<UIState> = action.switchMap {
        liveData(context = coroutineContext) {
            when (it) {
                UIAction.Default -> {
                    emit(UIState.Success(R.drawable.ic_not_favorite_unchecked))
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


    fun setDefault() {
        action.value = UIAction.Default
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
        object Default : UIAction()
        data class Update(val detailViewArgs: DetailViewArgs) : UIAction()
    }
}