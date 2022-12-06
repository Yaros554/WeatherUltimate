package com.skyyaros.weatherultimate.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.skyyaros.weatherultimate.databinding.ItemCurrentBinding
import com.skyyaros.weatherultimate.entity.ForecastCurrent

class ForecaCurrentAdapter(var item: List<ForecastCurrent>, private val context: Context): RecyclerView.Adapter<ForecaCurrentViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecaCurrentViewHolder {
        return ForecaCurrentViewHolder(ItemCurrentBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ForecaCurrentViewHolder, position: Int) {
        holder.bind(item[position], context)
    }

    override fun getItemCount(): Int {
        return item.size
    }
}