package com.skyyaros.weatherultimate.ui.search

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skyyaros.weatherultimate.R
import com.skyyaros.weatherultimate.data.ForecaRepository
import com.skyyaros.weatherultimate.entity.FavouriteCity
import com.skyyaros.weatherultimate.ui.main.StateForecast
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SearchViewModel(app: Application, private val forecaRepository: ForecaRepository): AndroidViewModel(app) {
    private val _stateCitiesFlow = MutableStateFlow<StateSearchCity>(StateSearchCity.Begin)
    val stateCitiesFlow = _stateCitiesFlow.asStateFlow()
    val helpFlow = MutableStateFlow("")
    private var isNotLoading = true
    private var listHelp: List<FavouriteCity> = emptyList()
    init {
        viewModelScope.launch {
            helpFlow.collect { query->
                if (query.length < 3) {
                    listHelp = emptyList()
                    if (isNotLoading)
                        _stateCitiesFlow.value = StateSearchCity.Begin
                } else if (query.length == 3) {
                    val templistHelp = forecaRepository.getCities(query)
                    if (templistHelp != null) {
                        listHelp = templistHelp
                        if (isNotLoading)
                            _stateCitiesFlow.value = StateSearchCity.Success(listHelp)
                    }
                } else {
                    if (isNotLoading)
                        _stateCitiesFlow.value = StateSearchCity.Success(listHelp.filter { it.name.contains(query, true) })
                }
            }
        }
    }

    fun getCities(citySearch: String) {
        viewModelScope.launch {
            isNotLoading = false
            _stateCitiesFlow.value = StateSearchCity.Loading
            val cities = forecaRepository.getCities(citySearch)
            if (cities != null) {
                _stateCitiesFlow.value = StateSearchCity.Success(cities)
            } else {
                _stateCitiesFlow.value = StateSearchCity.Error(getApplication<Application>().resources.getString(R.string.error_search))
            }
            isNotLoading = true
        }
    }

    fun addCity(favouriteCity: FavouriteCity) {
        GlobalScope.launch {
            forecaRepository.insertCity(favouriteCity)
        }
    }

    fun saveSearch(nameCity: String) {
        forecaRepository.saveSearch(nameCity)
    }

    fun getSearch(): String {
        return forecaRepository.getSearch()
    }
}