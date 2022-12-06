package com.skyyaros.weatherultimate.ui.main

import android.Manifest
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.PermissionChecker
import androidx.core.content.PermissionChecker.checkSelfPermission
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ConcatAdapter
import com.skyyaros.weatherultimate.App
import com.skyyaros.weatherultimate.R
import com.skyyaros.weatherultimate.databinding.MainFragmentBinding
import com.skyyaros.weatherultimate.entity.ForecastCurrent
import com.skyyaros.weatherultimate.entity.ForecastItemHour
import com.skyyaros.weatherultimate.entity.ForecastItemTwoWeek
import com.skyyaros.weatherultimate.ui.AppBarCallbacks

class MainFragment: Fragment() {
    private lateinit var adapterTwoWeek: ForecaTwoWeekAdapter
    private lateinit var adapterCurrent: ForecaCurrentAdapter
    private var forecastTwoWeekArray: Array<ForecastItemTwoWeek> = emptyArray()
    private var forecastHourArray: Array<ForecastItemHour> = emptyArray()
    private var forecast3HourArray: Array<ForecastItemHour> = emptyArray()
    private var appBarCallbacks: AppBarCallbacks? = null
    private var _bind: MainFragmentBinding? = null
    private val bind get() = _bind!!
    private val viewModel: MainViewModel by viewModels {
        object: ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return MainViewModel(requireActivity().application, App.component.forecaRepo()) as T
            }
        }
    }
    private val launcherLocation = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { map ->
        if (map.values.isNotEmpty() && (map.values.toList()[0] || map.values.toList()[1])) {
            viewModel.getForecastWithLocation()
        } else {
            Toast.makeText(requireContext(), getString(R.string.need_permission), Toast.LENGTH_LONG).show()
            viewModel.currentCity = null
            bind.swipe.isRefreshing = false
            viewModel.getForecast(true) //тут не важно, что передавать
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        appBarCallbacks = context as AppBarCallbacks
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _bind = MainFragmentBinding.inflate(inflater, container, false)
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind.swipe.setOnRefreshListener {
            if (viewModel.currentCity != null) {
                if (viewModel.currentCity!!.name.contains("⌖"))
                    viewModel.getForecast(true)
                else
                    viewModel.getForecast(false)
            } else {
                checkPermissions()
            }
        }
        adapterTwoWeek = ForecaTwoWeekAdapter(requireContext()) { date ->
            val action = MainFragmentDirections.actionMainFragmentToDetailFragment(
                forecastTwoWeekArray,
                forecastHourArray,
                forecast3HourArray,
                date,
                viewModel.currentCity?.name ?: "???"
            )
            findNavController().navigate(action)
        }
        adapterCurrent = ForecaCurrentAdapter(emptyList(), requireContext())
        val orientation = requireActivity().resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            bind.recycler.adapter = ConcatAdapter(adapterCurrent, adapterTwoWeek)
        } else {
            bind.recyclerCurrent!!.adapter = adapterCurrent
            bind.recycler.adapter = adapterTwoWeek
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.stateForecastFlow.collect { stateForecast ->
                when (stateForecast) {
                    is StateForecast.Success -> {
                        bind.swipe.isRefreshing = false
                        if (viewModel.currentCity != null) {
                            appBarCallbacks!!.setTitle(viewModel.currentCity!!.name)
                        }
                        bind.textView.visibility = View.GONE
                        bind.progressBar.visibility = View.GONE
                        bind.recycler.visibility = View.VISIBLE
                        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                            bind.recyclerCurrent!!.visibility = View.VISIBLE
                        }
                        forecastTwoWeekArray = stateForecast.forecastWithHelpData.forecastListTwoWeek.toTypedArray()
                        forecastHourArray = stateForecast.forecastWithHelpData.forecastListHour.toTypedArray()
                        forecast3HourArray = stateForecast.forecastWithHelpData.forecastList3Hour.toTypedArray()
                        adapterCurrent.item = listOf(stateForecast.forecastWithHelpData.forecastCurrent)
                        adapterCurrent.notifyDataSetChanged()
                        adapterTwoWeek.submitList(stateForecast.forecastWithHelpData.forecastListTwoWeek)
                        val isCache = stateForecast.forecastWithHelpData.isCache
                        if (isCache) {
                            Toast.makeText(requireContext(), getString(R.string.save_data), Toast.LENGTH_LONG).show()
                        }
                    }
                    is StateForecast.Error -> {
                        bind.swipe.isRefreshing = false
                        if (viewModel.currentCity != null) {
                            appBarCallbacks!!.setTitle(viewModel.currentCity!!.name)
                        }
                        bind.progressBar.visibility = View.GONE
                        bind.recycler.visibility = View.GONE
                        if (orientation == Configuration.ORIENTATION_LANDSCAPE)
                            bind.recyclerCurrent!!.visibility = View.GONE
                        bind.textView.visibility = View.VISIBLE
                        bind.textView.text = stateForecast.message
                    }
                    is StateForecast.Loading -> {
                        bind.swipe.isRefreshing = false
                        if (stateForecast.text.isNotBlank()) {
                            appBarCallbacks!!.setTitle(stateForecast.text)
                        }
                        bind.textView.visibility = View.GONE
                        bind.recycler.visibility = View.GONE
                        if (orientation == Configuration.ORIENTATION_LANDSCAPE)
                            bind.recyclerCurrent!!.visibility = View.GONE
                        bind.progressBar.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        appBarCallbacks!!.showAppbar()
    }

    override fun onResume() {
        super.onResume()
        if (viewModel.currentCity != null)
            appBarCallbacks!!.setTitle(viewModel.currentCity!!.name)
        if (viewModel.isFirst) {
            viewModel.isFirst = false
            val args: MainFragmentArgs by navArgs()
            var city = args.city
            if (city == null) {
                city = viewModel.getCurCity()
                if (city != null) {
                    viewModel.currentCity = city
                    appBarCallbacks!!.setTitle(viewModel.currentCity!!.name)
                    bind.swipe.isRefreshing = false
                    viewModel.getForecast(false)
                } else {
                   checkPermissions()
                }
            } else {
                if (city.id != -1L) {
                    viewModel.currentCity = city
                    appBarCallbacks!!.setTitle(viewModel.currentCity!!.name)
                    bind.swipe.isRefreshing = false
                    viewModel.getForecast(false)
                } else {
                    checkPermissions()
                }
            }
        } else {
            if (App.deleteCities.isNotEmpty() &&
                viewModel.currentCity != null &&
                !viewModel.currentCity!!.name.contains("⌖") &&
                App.deleteCities.contains(viewModel.currentCity!!.id)) {
                    checkPermissions()
            }
        }
        App.deleteCities.clear()
    }

    override fun onStop() {
        super.onStop()
        if (viewModel.currentCity != null && !viewModel.currentCity!!.name.contains("⌖")) {
            viewModel.saveCurCity(viewModel.currentCity!!)
        } else {
            viewModel.deleteCity()
        }
    }

    override fun onDestroyView() {
        _bind = null
        super.onDestroyView()
    }

    override fun onDetach() {
        appBarCallbacks = null
        super.onDetach()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.go_favourite -> {
                val action = MainFragmentDirections.actionMainFragmentToFavouriteFragment()
                findNavController().navigate(action)
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun checkPermissions() {
        val allowedPermissions = REQUIRED_PERMISSION.map { permission ->
            checkSelfPermission(requireContext(), permission) == PermissionChecker.PERMISSION_GRANTED
        }
        if (allowedPermissions[0] || allowedPermissions[1]) {
            viewModel.getForecastWithLocation()
        } else {
            launcherLocation.launch(REQUIRED_PERMISSION)
        }
    }

    private val REQUIRED_PERMISSION: Array<String> = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )
}