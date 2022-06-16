package com.example.marvelapp.presentation.details

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.marvelapp.R
import com.example.marvelapp.databinding.ItemChildDetailBinding
import com.example.marvelapp.framework.imageloader.ImageLoader

class DetailChildAdapter(
    val detailChilds: List<DetailChildVE>,
    val imageLoader: ImageLoader
) : RecyclerView.Adapter<DetailChildAdapter.DetailChildViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailChildViewHolder {
        return DetailChildViewHolder.create(parent, imageLoader)
    }

    override fun onBindViewHolder(holder: DetailChildViewHolder, position: Int) {
        holder.bind(detailChilds[position])
    }

    override fun getItemCount(): Int = detailChilds.size


    class DetailChildViewHolder(
        private val itemBinding: ItemChildDetailBinding,
        private val imageLoader: ImageLoader
    ) : RecyclerView.ViewHolder(itemBinding.root) {
        companion object {
            fun create(parent: ViewGroup, imageLoader: ImageLoader): DetailChildViewHolder {
                val itemBinding = ItemChildDetailBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )

                return DetailChildViewHolder(itemBinding, imageLoader)
            }
        }

        private val imageCategory: ImageView = itemBinding.imageItemCategory

        fun bind(detailChildVE: DetailChildVE) {
            imageLoader.load(
                imageView = imageCategory,
                imageURL = detailChildVE.imageURL
            )
        }
    }
}