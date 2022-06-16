package com.example.marvelapp.presentation.details

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.marvelapp.databinding.ItemParentDetailBinding
import com.example.marvelapp.framework.imageloader.ImageLoader

class DetailParentAdapter(
    val detailParents: List<DetailParentVE>,
    val imageLoader: ImageLoader
) : RecyclerView.Adapter<DetailParentAdapter.DetailParentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailParentViewHolder {
        return DetailParentViewHolder.create(parent, imageLoader)
    }

    override fun onBindViewHolder(holder: DetailParentViewHolder, position: Int) {
        holder.bind(detailParents[position])
    }

    override fun getItemCount(): Int = detailParents.size


    class DetailParentViewHolder(
        private val itemBinding: ItemParentDetailBinding,
        private val imageLoader: ImageLoader
    ) : RecyclerView.ViewHolder(itemBinding.root) {
        companion object {
            fun create(parent: ViewGroup, imageLoader: ImageLoader): DetailParentViewHolder {
                val itemBinding = ItemParentDetailBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )

                return DetailParentViewHolder(itemBinding, imageLoader)
            }
        }

        val textItemCategory: TextView = itemBinding.textItemCategory
        val recyclerChildDetail: RecyclerView = itemBinding.recyclerChildDetail


        fun bind(detailChildVE: DetailParentVE) {
            textItemCategory.text = itemView.context.getString(detailChildVE.categoryStringResId)
            recyclerChildDetail.run {
                setHasFixedSize(true)
                adapter = DetailChildAdapter(detailChildVE.detailChildList, imageLoader)
            }
        }
    }
}