package com.skyyaros.weatherultimate.ui.detail

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.skyyaros.weatherultimate.databinding.DetailFragmentBinding
import com.skyyaros.weatherultimate.ui.AppBarCallbacks
import java.text.SimpleDateFormat

class DetailFragment: Fragment() {
    private val keyMode = "mode"
    private var mode = 1
    private var _bind: DetailFragmentBinding? = null
    private val bind get() = _bind!!
    private var appBarCallbacks: AppBarCallbacks? = null
    private val mAnimator = ViewPager2.PageTransformer { page, position ->
        val absPos = Math.abs(position)
        page.apply {
            rotation = position * 360
            translationY = absPos * 500f
            val scale = if (absPos > 1) 0F else 1 - absPos
            scaleX = scale
            scaleY = scale
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        appBarCallbacks = context as AppBarCallbacks
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _bind = DetailFragmentBinding.inflate(inflater, container, false)
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mode = savedInstanceState?.getInt(keyMode) ?: mode
        val args: DetailFragmentArgs by navArgs()
        val forecastTwoWeekList = args.forecastTwoWeekArray.toList()
        val forecastHourListList = if (args.forecastHourArray.isNotEmpty())
            args.forecastHourArray.toList().groupBy { it.time.substringBefore("T") }.values.toList()
        else
            emptyList()
        val forecast3HourListList = args.forecast3HourArray.toList().groupBy { it.time.substringBefore("T") }.values.toList()
        val date = args.date
        val tempItem = forecastTwoWeekList.find { it.date == date }
        val pos = forecastTwoWeekList.indexOf(tempItem)
        val adapter = ViewPagerAdapter(
            forecastTwoWeekList,
            forecastHourListList,
            forecast3HourListList,
            requireContext(),
            { mode },
            { selectMode -> mode = selectMode }
        )
        bind.viewPager.setPageTransformer(mAnimator)
        bind.viewPager.adapter = adapter
        bind.viewPager.setCurrentItem(pos, false)
        TabLayoutMediator(bind.tabs, bind.viewPager) { tab, position ->
            val dt = SimpleDateFormat("yyyy-MM-dd").parse(forecastTwoWeekList[position].date)
            val dayOfWeek = SimpleDateFormat("EE").format(dt)
            val dayAndMonth = SimpleDateFormat("dd.MM").format(dt)
            tab.text = "$dayOfWeek $dayAndMonth"
        }.attach()
    }

    override fun onResume() {
        super.onResume()
        val args: DetailFragmentArgs by navArgs()
        appBarCallbacks!!.setTitle(args.currentCity)
    }

    override fun onDestroyView() {
        _bind = null
        super.onDestroyView()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(keyMode, mode)
    }

    override fun onDetach() {
        appBarCallbacks = null
        super.onDetach()
    }
}