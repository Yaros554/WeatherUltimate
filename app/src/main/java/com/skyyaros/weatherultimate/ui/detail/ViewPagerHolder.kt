package com.skyyaros.weatherultimate.ui.detail

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.skyyaros.weatherultimate.R
import com.skyyaros.weatherultimate.databinding.ItemForViewPagerBinding
import com.skyyaros.weatherultimate.entity.ForecastItemHour
import com.skyyaros.weatherultimate.entity.ForecastItemTwoWeek

class ViewPagerHolder(private val binding: ItemForViewPagerBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(
        itemTwoWeek: ForecastItemTwoWeek,
        listHour: List<ForecastItemHour>,
        list3Hour: List<ForecastItemHour>,
        context: Context,
        position: Int,
        getUiMode: ()->Int,
        setUiMode: (Int)->Unit,
        updateElems: (Int)->Unit
    ) {
        val mode = getUiMode()
        drawUi(itemTwoWeek, listHour, list3Hour, context)
        setUi(listHour.isNotEmpty(), list3Hour.isNotEmpty(), mode)
        binding.buttonReview.setOnClickListener {
            setUiMode(1)
            setUi(listHour.isNotEmpty(), list3Hour.isNotEmpty(), 1)
            updateElems(position)
        }
        binding.buttonHour.setOnClickListener {
            setUiMode(2)
            setUi(listHour.isNotEmpty(), list3Hour.isNotEmpty(), 2)
            updateElems(position)
        }
        binding.button3hour.setOnClickListener {
            setUiMode(3)
            setUi(listHour.isNotEmpty(), list3Hour.isNotEmpty(), 3)
            updateElems(position)
        }
    }

    private fun drawUi(
        itemTwoWeek: ForecastItemTwoWeek,
        listHour: List<ForecastItemHour>,
        list3Hour: List<ForecastItemHour>,
        context: Context
    ) {
        with (binding) {
            val resId = context.resources.getIdentifier(itemTwoWeek.symbol, "drawable", context.packageName)
            imageForeca.setImageResource(resId)
            textForeca.text = itemTwoWeek.symbolPhrase
            maxTemp.text = context.resources.getString(R.string.max_temp_viewpager, itemTwoWeek.maxTemp, itemTwoWeek.maxFeelsLikeTemp)
            minTemp.text = context.resources.getString(R.string.min_temp_viewpager, itemTwoWeek.minTemp, itemTwoWeek.minFeelsLikeTemp)
            precipAccum.text = context.resources.getString(R.string.precip_accum_viewpager, itemTwoWeek.precipAccum, itemTwoWeek.snowAccum)
            precipProb.text = context.resources.getString(R.string.precip_prob_viewpager, itemTwoWeek.precipProb)
            windSpeed.text = context.resources.getString(R.string.wind_viewpager, itemTwoWeek.maxWindSpeed, itemTwoWeek.maxWindGust)
            windDir.text = context.resources.getString(R.string.wind_dir_viewpager, windDirToString(itemTwoWeek.windDir))
            sunrise.text = context.resources.getString(R.string.sunrise_viewpager, itemTwoWeek.sunrise?.replace(':', '.') ?: context.resources.getString(R.string.no_sun_viewpager))
            sunset.text = context.resources.getString(R.string.sunset_viewpager, itemTwoWeek.sunset?.replace(':', '.') ?: context.resources.getString(R.string.no_sun_viewpager))

            if (listHour.isNotEmpty()) {
                val forecaHourAdapter = ForecaHourAdapter(listHour, context)
                recyclerHour.adapter = forecaHourAdapter
            }

            if (list3Hour.isNotEmpty()) {
                val foreca3HourAdapter = ForecaHourAdapter(list3Hour, context)
                recycler3Hour.adapter = foreca3HourAdapter
            }
        }
    }

    private fun windDirToString(windDir: Int): String {
        return when (windDir) {
            in 0..22 -> "С"
            in 337..360 -> "С"
            in 23..67 -> "СВ"
            in 68..112 -> "В"
            in 113..157 -> "ЮВ"
            in 158..202 -> "Ю"
            in 203..247 -> "ЮЗ"
            in 248..292 -> "З"
            in 293..336 -> "СЗ"
            else -> "NA"
        }
    }

    private fun setUi(
        listHourNotEmpty: Boolean,
        list3HourNotEmpty: Boolean,
        mode: Int
    ) {
        with (binding) {
            if (mode == 1) {
                recyclerHour.visibility = View.GONE
                recycler3Hour.visibility = View.GONE
                notForecaHour.visibility = View.GONE
                scrollView.visibility = View.VISIBLE
                buttonReview.isEnabled = false
                buttonHour.isEnabled = true
                button3hour.isEnabled = true
            } else if (mode == 2) {
                scrollView.visibility = View.GONE
                recycler3Hour.visibility = View.GONE
                if (listHourNotEmpty) {
                    notForecaHour.visibility = View.GONE
                    recyclerHour.visibility = View.VISIBLE
                } else {
                    recyclerHour.visibility = View.GONE
                    notForecaHour.visibility = View.VISIBLE
                }
                buttonReview.isEnabled = true
                buttonHour.isEnabled = false
                button3hour.isEnabled = true
            } else {
                scrollView.visibility = View.GONE
                recyclerHour.visibility = View.GONE
                if (list3HourNotEmpty) {
                    notForecaHour.visibility = View.GONE
                    recycler3Hour.visibility = View.VISIBLE
                } else {
                    recycler3Hour.visibility = View.GONE
                    notForecaHour.visibility = View.VISIBLE
                }
                buttonReview.isEnabled = true
                buttonHour.isEnabled = true
                button3hour.isEnabled = false
            }
        }
    }
}