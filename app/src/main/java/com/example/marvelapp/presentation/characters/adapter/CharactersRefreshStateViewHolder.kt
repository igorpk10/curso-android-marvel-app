package com.example.marvelapp.presentation.characters.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.example.marvelapp.databinding.ItemCharacterLoadingStateBinding
import com.example.marvelapp.databinding.ItemCharacterRefreshStateBinding

class CharactersRefreshStateViewHolder(
    view: ItemCharacterRefreshStateBinding,
    val retry: () -> Unit
) : RecyclerView.ViewHolder(view.root) {

    companion object {

        fun create(parent: ViewGroup, retry: () -> Unit): CharactersRefreshStateViewHolder {
            val itemBinding = ItemCharacterRefreshStateBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

            return CharactersRefreshStateViewHolder(itemBinding, retry)
        }

    }

    val binding = ItemCharacterRefreshStateBinding.bind(itemView)
    private val progressBarRefresh = binding.progressLoadingRefresh
    private val textTryAgainMessage = binding.textTryAgain.also {
        it.setOnClickListener {
            retry()
        }
    }

    fun bind(loadState: LoadState) {
        progressBarRefresh.isVisible = loadState is LoadState.Loading
        textTryAgainMessage.isVisible = loadState is LoadState.Error
    }
}