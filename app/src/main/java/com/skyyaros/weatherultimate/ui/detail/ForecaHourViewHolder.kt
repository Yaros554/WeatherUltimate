package com.skyyaros.weatherultimate.ui.detail

import android.content.Context
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.skyyaros.weatherultimate.R
import com.skyyaros.weatherultimate.databinding.ItemHourBinding
import com.skyyaros.weatherultimate.entity.ForecastItemHour

class ForecaHourViewHolder(private val binding: ItemHourBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(item: ForecastItemHour, context: Context) {
        with (binding) {
            hour.text = item.time.substringAfter("T").substringBefore("+")
            val resId = context.resources.getIdentifier(item.symbol, "drawable", context.packageName)
            imageView.setImageResource(resId)
            textTemp.text = context.resources.getString(R.string.temp_hour, item.temperature)
            textTempFeel.text = context.resources.getString(R.string.temp_feel_hour, item.feelsLikeTemp)
            windSpeed.text = context.resources.getString(R.string.wind_speed_hour, item.windSpeed)
            windGust.text = context.resources.getString(R.string.wind_gust_hour, item.windGust)
            windDir.text = context.resources.getString(R.string.wind_dir_hour, item.windDirString)
            accumWater.text = context.resources.getString(R.string.accum_water_hour, item.precipAccum)
            accumSnow.text = context.resources.getString(R.string.accum_snow_hour, item.snowAccum)
            probWater.text = context.resources.getString(R.string.precip_prob_hour, item.precipProb)

            imageView.setOnClickListener {
                Toast.makeText(context, item.symbolPhrase, Toast.LENGTH_LONG).show()
            }
        }
    }
}