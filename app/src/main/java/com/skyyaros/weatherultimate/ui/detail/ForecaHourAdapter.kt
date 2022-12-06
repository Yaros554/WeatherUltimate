package com.skyyaros.weatherultimate.ui.detail

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.skyyaros.weatherultimate.databinding.ItemHourBinding
import com.skyyaros.weatherultimate.entity.ForecastItemHour

class ForecaHourAdapter(private val items: List<ForecastItemHour>, private val context: Context): Adapter<ForecaHourViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecaHourViewHolder {
        return ForecaHourViewHolder(ItemHourBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ForecaHourViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item, context)
    }

    override fun getItemCount(): Int {
        return items.size
    }
}