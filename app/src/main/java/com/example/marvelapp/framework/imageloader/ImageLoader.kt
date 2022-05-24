package com.example.marvelapp.framework.imageloader

import android.widget.ImageView
import androidx.annotation.DrawableRes

interface ImageLoader {

    fun load(imageView: ImageView, imageURL: String, @DrawableRes fallback: Int)
}