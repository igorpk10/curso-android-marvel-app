package com.example.marvelapp.framework.imageloader

import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import javax.inject.Inject

class GlideImageLoader @Inject constructor() : ImageLoader {

    override fun load(imageView: ImageView, imageURL: String, @DrawableRes fallback: Int) {
        Glide.with(imageView.rootView)
            .load(imageURL)
            .fallback(fallback)
            .into(imageView)
    }
}