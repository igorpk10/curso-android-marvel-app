package com.example.marvelapp.presentation.details

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class DetailViewArgs(
    val characterId: Int,
    val name: String,
    val imageURL: String
) : Parcelable
