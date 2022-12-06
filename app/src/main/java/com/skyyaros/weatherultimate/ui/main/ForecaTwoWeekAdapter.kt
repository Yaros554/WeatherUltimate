package com.skyyaros.weatherultimate.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.skyyaros.weatherultimate.databinding.ItemTwoWeekBinding
import com.skyyaros.weatherultimate.entity.ForecastItemTwoWeek

class ForecaTwoWeekAdapter(private val context: Context, private val onClick: (String) -> Unit):
    ListAdapter<ForecastItemTwoWeek, ForecaTwoWeekViewHolder>(DiffUtilCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecaTwoWeekViewHolder {
        return ForecaTwoWeekViewHolder(ItemTwoWeekBinding.inflate(LayoutInflater.from(parent.context), parent,false))
    }

    override fun onBindViewHolder(holder: ForecaTwoWeekViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, context)
        holder.binding.root.setOnClickListener {
            onClick(item.date)
        }
    }
}

class DiffUtilCallback : DiffUtil.ItemCallback<ForecastItemTwoWeek>() {
    override fun areItemsTheSame(oldItem: ForecastItemTwoWeek, newItem: ForecastItemTwoWeek): Boolean = oldItem.date == newItem.date
    override fun areContentsTheSame(oldItem: ForecastItemTwoWeek, newItem: ForecastItemTwoWeek): Boolean = oldItem == newItem
}