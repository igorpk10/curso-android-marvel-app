package com.example.marvelapp.presentation.characters

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.asLiveData
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.igaopk10.core.domain.model.Character
import com.igaopk10.core.usecase.GetCharactersUseCase
import com.igaopk10.core.usecase.base.CoroutinesDispatchers
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class CharactersViewModel @Inject constructor(
    private val getCharactersUseCase: GetCharactersUseCase,
    private val coroutineDispatcher: CoroutinesDispatchers
) : ViewModel() {

    var currentSearchQuery = ""

    private val action = MutableLiveData<Action>()
    val state: LiveData<UIState> = action.switchMap {
        when (it) {
            is Action.Search, Action.Sort -> {
                getCharactersUseCase(
                    GetCharactersUseCase.GetCharactersParams(currentSearchQuery, getPageConfig())
                ).cachedIn(viewModelScope).map { data ->
                    UIState.SearchResult(data)
                }.asLiveData(coroutineDispatcher.main())
            }
        }
    }

    fun searchCharacters() {
        action.value = Action.Search
    }

    fun charactersPagingData(query: String): Flow<PagingData<Character>> {
        return getCharactersUseCase(
            GetCharactersUseCase.GetCharactersParams(query, getPageConfig())
        ).cachedIn(viewModelScope)
    }

    fun applySort(){
        action.value = Action.Sort
    }

    fun closeSearch() {
        if (currentSearchQuery.isNotEmpty()) {
            currentSearchQuery = ""
        }
    }

    private fun getPageConfig() = PagingConfig(
        pageSize = 20
    )

    sealed class UIState {
        data class SearchResult(val data: PagingData<Character>) : UIState()
    }

    sealed class Action {
        object Search : Action()
        object Sort: Action()
    }
}