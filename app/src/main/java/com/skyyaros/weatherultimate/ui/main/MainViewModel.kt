package com.skyyaros.weatherultimate.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skyyaros.weatherultimate.R
import com.skyyaros.weatherultimate.data.ForecaRepository
import com.skyyaros.weatherultimate.entity.FavouriteCity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(app: Application, private val forecaRepository: ForecaRepository): AndroidViewModel(app) {
    private val _stateForecastFlow = MutableStateFlow<StateForecast>(StateForecast.Loading(""))
    val stateForecastFlow = _stateForecastFlow.asStateFlow()
    var isFirst = true
    var currentCity: FavouriteCity? = null
    private val locationString: String? get() {
        return if (currentCity != null)
            "${currentCity!!.lon},${currentCity!!.lat}"
        else
            null
    }

    fun getForecast(isUserLocation: Boolean, needCache: Boolean = false) {
        viewModelScope.launch {
            if (locationString != null) {
                _stateForecastFlow.value = StateForecast.Loading("")
                val forecastWithHelpData = forecaRepository.getForecast(
                    locationString!!,
                    if (isUserLocation) -1L else currentCity!!.id,
                    needCache
                )
                if (forecastWithHelpData != null) {
                    _stateForecastFlow.value = StateForecast.Success(forecastWithHelpData)
                } else {
                    _stateForecastFlow.value = StateForecast.Error(getApplication<Application>().resources.getString(R.string.no_foreca))
                }
            } else {
                _stateForecastFlow.value = StateForecast.Error(getApplication<Application>().resources.getString(R.string.no_location))
            }
        }
    }

    fun getForecastWithLocation() {
        viewModelScope.launch {
            var city = ""
            val tempLocationString = forecaRepository.getUserLocation()
            if (tempLocationString != null) {
                currentCity = forecaRepository.getNameCity(tempLocationString)
                if (currentCity != null) {
                    currentCity!!.name += "⌖"
                    forecaRepository.saveLocation(currentCity!!)
                    city = currentCity!!.name
                }
            } else {
                currentCity = null
            }
            if (currentCity == null) {
                currentCity = forecaRepository.getLocation()
                city = currentCity?.name ?: ""
                _stateForecastFlow.value = StateForecast.Loading(city)
                getForecast(true, true)
            } else {
                _stateForecastFlow.value = StateForecast.Loading(city)
                getForecast(true)
            }
        }
    }

    fun getCurCity(): FavouriteCity? {
        return forecaRepository.getCurrentCity()
    }

    fun saveCurCity(favouriteCity: FavouriteCity) {
        forecaRepository.saveCurrentCity(favouriteCity)
    }

    fun deleteCity() {
        forecaRepository.deleteCity()
    }
}