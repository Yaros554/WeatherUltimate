package com.skyyaros.weatherultimate.ui.search

import com.skyyaros.weatherultimate.entity.FavouriteCity

sealed class StateSearchCity {
    object Loading: StateSearchCity()
    object Begin: StateSearchCity()
    data class Success(val listCities: List<FavouriteCity>): StateSearchCity()
    data class Error(val message: String): StateSearchCity()
}
