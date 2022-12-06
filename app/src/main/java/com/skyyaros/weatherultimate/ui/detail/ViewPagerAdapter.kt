package com.skyyaros.weatherultimate.ui.detail

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.skyyaros.weatherultimate.databinding.ItemForViewPagerBinding
import com.skyyaros.weatherultimate.entity.ForecastItemHour
import com.skyyaros.weatherultimate.entity.ForecastItemTwoWeek

class ViewPagerAdapter(
    private val itemsTwoWeek: List<ForecastItemTwoWeek>,
    private val itemsHour: List<List<ForecastItemHour>>,
    private val items3Hour: List<List<ForecastItemHour>>,
    private val context: Context,
    private val getUiMode: ()->Int,
    private val setUiMode: (Int)->Unit
    ): Adapter<ViewPagerHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerHolder {
        return ViewPagerHolder(ItemForViewPagerBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewPagerHolder, position: Int) {
        val itemTwoWeek = itemsTwoWeek[position]
        val listHour = if (itemsHour.size - 1 >= position) itemsHour[position] else emptyList()
        val list3Hour = if (items3Hour.size - 1 >= position) items3Hour[position] else emptyList()
        holder.bind(itemTwoWeek, listHour, list3Hour, context, position, getUiMode, setUiMode) { pos -> updateElems(pos) }
    }

    override fun getItemCount(): Int {
        return itemsTwoWeek.size
    }

    private fun updateElems(pos: Int) {
        if (pos + 3 <= itemsHour.size - 1)
            notifyItemRangeChanged(pos + 1, 3)
        else if (pos + 2 <= itemsHour.size - 1)
            notifyItemRangeChanged(pos + 1, 2)
        else if (pos + 1 <= itemsHour.size - 1)
            notifyItemChanged(pos + 1)

        if (pos - 3 >= 0)
            notifyItemRangeChanged(pos - 3, 3)
        else if (pos - 2 >= 0)
            notifyItemRangeChanged(0, 2)
        else if (pos - 1 >= 0)
            notifyItemChanged(0)
    }
}