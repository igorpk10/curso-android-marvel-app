package com.example.marvelapp.presentation.characters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.example.marvelapp.databinding.ItemCharacterLoadingStateBinding

class CharactersLoadMoreStateViewHolder(
    view: ItemCharacterLoadingStateBinding,
    val retry: () -> Unit
) : RecyclerView.ViewHolder(view.root) {

    companion object {

        fun create(parent: ViewGroup, retry: () -> Unit): CharactersLoadMoreStateViewHolder {
            val itemBinding = ItemCharacterLoadingStateBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

            return CharactersLoadMoreStateViewHolder(itemBinding, retry)
        }

    }

    val binding = ItemCharacterLoadingStateBinding.bind(itemView)
    private val progressBarLoadingMore = binding.progressLoadingMore
    private val textTryAgainMessage = binding.textTryAgain.also {
        it.setOnClickListener {
            retry()
        }
    }

    fun bind(loadState: LoadState) {
        progressBarLoadingMore.isVisible = loadState is LoadState.Loading
        textTryAgainMessage.isVisible = loadState is LoadState.Error
    }
}