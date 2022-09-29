package com.example.marvelapp.presentation.characters.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.marvelapp.databinding.ItemCharacterBinding
import com.example.marvelapp.framework.imageloader.ImageLoader
import com.example.marvelapp.util.OnCharacterItemClick
import com.igaopk10.core.domain.model.Character

class CharacterViewHolder(
    itemCharacterBinding: ItemCharacterBinding,
    private val onItemClick: OnCharacterItemClick,
    private val imageLoader: ImageLoader
) : RecyclerView.ViewHolder(itemCharacterBinding.root) {

    private val textName = itemCharacterBinding.textName
    private val imageCharacter = itemCharacterBinding.imageView

    fun bind(character: Character) {
        textName.text = character.name
        imageCharacter.transitionName = character.name
        imageLoader.load(
            imageView = imageCharacter,
            imageURL = character.imageUrl
        )

        itemView.setOnClickListener {
            onItemClick(character, imageCharacter)
        }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            onItemClick: (character: Character, view: View) -> Unit,
            imageLoader: ImageLoader
        ): CharacterViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val itemBinding = ItemCharacterBinding.inflate(inflater, parent, false)
            return CharacterViewHolder(itemBinding, onItemClick, imageLoader)
        }
    }
}