package com.skyyaros.weatherultimate.ui.favourite

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.skyyaros.weatherultimate.databinding.ItemCityBinding
import com.skyyaros.weatherultimate.entity.FavouriteCity

class ItemCityViewHolder(val binding: ItemCityBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(item: FavouriteCity, isEdit: Boolean) {
        with (binding) {
            name.text = item.name
            geoData.text = "${item.country}, ${item.adminArea ?: ""}"
            if (isEdit)
                delete.visibility = View.VISIBLE
            else
                delete.visibility = View.GONE
        }
    }
}