package com.example.marvelapp.presentation.favorites

import com.example.marvelapp.presentation.common.ListItem

data class FavoriteItem(
    val id: Int,
    val name: String,
    val imageURL: String,
    override val key: Long = id.toLong()
) : ListItem