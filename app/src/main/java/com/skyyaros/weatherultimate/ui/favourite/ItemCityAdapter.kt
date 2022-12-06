package com.skyyaros.weatherultimate.ui.favourite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.skyyaros.weatherultimate.databinding.ItemCityBinding
import com.skyyaros.weatherultimate.entity.FavouriteCity

class ItemCityAdapter(
    private val onClick: (FavouriteCity) -> Unit,
    private val onDeleteClick: (FavouriteCity) -> Unit,
    private val getMode: () -> Boolean
): ListAdapter<FavouriteCity, ItemCityViewHolder>(DiffUtilCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemCityViewHolder {
        return ItemCityViewHolder(ItemCityBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ItemCityViewHolder, position: Int) {
        val item = getItem(position)
        val mode = getMode()
        holder.bind(item, mode)
        holder.binding.root.setOnClickListener {
            onClick(item)
        }
        holder.binding.delete.setOnClickListener {
            onDeleteClick(item)
        }
    }
}

class DiffUtilCallback : DiffUtil.ItemCallback<FavouriteCity>() {
    override fun areItemsTheSame(oldItem: FavouriteCity, newItem: FavouriteCity): Boolean = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: FavouriteCity, newItem: FavouriteCity): Boolean = oldItem == newItem
}