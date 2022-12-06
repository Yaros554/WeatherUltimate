package com.skyyaros.weatherultimate.ui.search

import androidx.recyclerview.widget.RecyclerView
import com.skyyaros.weatherultimate.databinding.ItemSearchCityBinding
import com.skyyaros.weatherultimate.entity.FavouriteCity

class ItemSearchCityViewHolder(val binding: ItemSearchCityBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(item: FavouriteCity) {
        with (binding) {
            name.text = item.name
            geoData.text = "${item.country}, ${item.adminArea ?: ""}"
        }
    }
}