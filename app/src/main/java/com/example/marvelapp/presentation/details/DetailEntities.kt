package com.example.marvelapp.presentation.details

import androidx.annotation.StringRes

data class DetailChildVE(
    val id: Int,
    val imageURL: String
)

data class DetailParentVE(
    @StringRes val categoryStringResId: Int,
    val detailChildList: List<DetailChildVE> = listOf()
)