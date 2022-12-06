package com.skyyaros.weatherultimate.ui.main

import android.content.Context
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.skyyaros.weatherultimate.R
import com.skyyaros.weatherultimate.databinding.ItemTwoWeekBinding
import com.skyyaros.weatherultimate.entity.ForecastItemTwoWeek
import java.text.SimpleDateFormat

class ForecaTwoWeekViewHolder(val binding: ItemTwoWeekBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(item: ForecastItemTwoWeek, context: Context) {
        with (binding) {
            val dt = SimpleDateFormat("yyyy-MM-dd").parse(item.date)
            dayWeek.text = SimpleDateFormat("EE").format(dt)
            date.text = item.date.substringAfter('-')
            val resId = context.resources.getIdentifier(item.symbol, "drawable", context.packageName)
            imageView.setImageResource(resId)
            maxTemp.text = "${item.maxTemp}℃"
            minTemp.text = "${item.minTemp}℃"
            water.text = context.resources.getString(R.string.water_two_week, item.precipAccum)
            wind.text = context.resources.getString(R.string.wind_two_week, item.maxWindSpeed)

            imageView.setOnClickListener {
                Toast.makeText(context, item.symbolPhrase, Toast.LENGTH_LONG).show()
            }
        }
    }
}