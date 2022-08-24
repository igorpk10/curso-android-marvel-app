package com.example.marvelapp.presentation.details

import androidx.lifecycle.ViewModel
import com.example.marvelapp.presentation.details.actionstate.DetailFragmentUIActionState
import com.example.marvelapp.presentation.details.actionstate.DetailFragmentFavoriteUIActionState
import com.igaopk10.core.usecase.AddFavoriteUseCase
import com.igaopk10.core.usecase.CheckFavoriteUseCase
import com.igaopk10.core.usecase.GetCharacterCategories
import com.igaopk10.core.usecase.base.CoroutinesDispatchers
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    useCase: GetCharacterCategories,
    checkFavoriteUseCase: CheckFavoriteUseCase,
    addFavoriteUseCase: AddFavoriteUseCase,
    coroutineDispatcher: CoroutinesDispatchers
) : ViewModel() {

    val categories = DetailFragmentUIActionState(
        coroutineContext = coroutineDispatcher.main(),
        getCharactersCategoriesUseCase = useCase
    )

    val favorite = DetailFragmentFavoriteUIActionState(
        coroutineContext = coroutineDispatcher.main(),
        addFavoriteUseCase = addFavoriteUseCase,
        checkFavoriteUseCase = checkFavoriteUseCase
    )

}
