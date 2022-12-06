package com.skyyaros.weatherultimate.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.skyyaros.weatherultimate.databinding.ItemSearchCityBinding
import com.skyyaros.weatherultimate.entity.FavouriteCity

class ItemSearchCityAdapter(val onClick: (FavouriteCity)->Unit): ListAdapter<FavouriteCity, ItemSearchCityViewHolder>(DiffUtilCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemSearchCityViewHolder {
        return ItemSearchCityViewHolder(ItemSearchCityBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ItemSearchCityViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.binding.root.setOnClickListener {
            onClick(item)
        }
    }
}

class DiffUtilCallback : DiffUtil.ItemCallback<FavouriteCity>() {
    override fun areItemsTheSame(oldItem: FavouriteCity, newItem: FavouriteCity): Boolean = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: FavouriteCity, newItem: FavouriteCity): Boolean = oldItem == newItem
}