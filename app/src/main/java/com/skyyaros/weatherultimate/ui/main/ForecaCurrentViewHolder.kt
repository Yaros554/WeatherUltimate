package com.skyyaros.weatherultimate.ui.main

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.skyyaros.weatherultimate.R
import com.skyyaros.weatherultimate.databinding.ItemCurrentBinding
import com.skyyaros.weatherultimate.entity.ForecastCurrent

class ForecaCurrentViewHolder(private val binding: ItemCurrentBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(item: ForecastCurrent, context: Context) {
        with (binding) {
            val anim = when (item.symbol) {
                "d000", "d100" -> "sun"
                "n000", "n100" -> "moon"
                "d200", "d300", "d210", "d310", "d211", "d311", "d212", "d312" -> "sun_with_cloud"
                "n200", "n300", "n210", "n310", "n211", "n311", "n212", "n312" -> "moon_with_cloud"
                "d400", "d410", "n400", "n410", "d411", "n411", "d412", "n412" -> "cloud"
                "d500", "d600", "n500", "n600" -> "fog"
                "d220", "d320", "d420", "d430", "n220", "n320", "n420", "n430" -> "rain"
                "d240", "d340", "d440", "n240", "n340", "n440" -> "thunder"
                else -> if (item.symbol[3] == '1') "snow_rain" else "snow"
            }
            val resId = context.resources.getIdentifier(anim, "raw", context.packageName)
            forecaAnim.setAnimation(resId)
            forecaText.text = item.symbolPhrase
            textTemp.text = context.resources.getString(R.string.temp_current, item.temperature, item.feelsLikeTemp)
            textWind.text = context.resources.getString(R.string.wind_current, item.windSpeed, item.windGust)
            textWindDir.text = context.resources.getString(R.string.wind_dir_current, item.windDirString)
            textRainInten.text = context.resources.getString(R.string.water_current, item.precipRate, item.precipProb)
            textPressure.text = if (item.pressure != -1.0) "${item.pressure} hPa" else "N/A"
        }
    }
}