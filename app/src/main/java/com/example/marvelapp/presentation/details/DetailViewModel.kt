package com.example.marvelapp.presentation.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marvelapp.R
import com.igaopk10.core.domain.model.Comic
import com.igaopk10.core.domain.model.Event
import com.igaopk10.core.usecase.GetCharacterCategories
import com.igaopk10.core.usecase.base.ResultStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val useCase: GetCharacterCategories
) : ViewModel() {

    private val _uiState = MutableLiveData<UiState>()
    val uiState: LiveData<UiState> get() = _uiState

    fun getComics(characterId: Int) = viewModelScope.launch {
        useCase(GetCharacterCategories.GetCharacterParams(characterId)).watchStatus()
    }

    private fun Flow<ResultStatus<Pair<List<Comic>, List<Event>>>>.watchStatus() =
        viewModelScope.launch {
            collect { status ->
                _uiState.value = when (status) {
                    ResultStatus.Loading -> UiState.Loading
                    is ResultStatus.Success -> {
                        val detailParentList: MutableList<DetailParentVE> = mutableListOf()
                        val comics = status.data.first
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

                        val events = status.data.second
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

                        UiState.Success(detailParentList)
                    }
                    is ResultStatus.Error -> UiState.Error
                }
            }
        }

    sealed class UiState {
        object Loading : UiState()
        data class Success(val detailParentList: List<DetailParentVE>) : UiState()
        object Error : UiState()
    }

}