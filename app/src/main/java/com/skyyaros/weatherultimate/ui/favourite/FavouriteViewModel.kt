package com.skyyaros.weatherultimate.ui.favourite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skyyaros.weatherultimate.data.ForecaRepository
import com.skyyaros.weatherultimate.entity.FavouriteCity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FavouriteViewModel(private val forecaRepository: ForecaRepository): ViewModel() {
    var isEdit = false
    val flowFavouriteCities = forecaRepository.getFavouriteCities().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = emptyList()
    )

    fun deleteFavouriteCity(favouriteCity: FavouriteCity) {
        viewModelScope.launch {
            forecaRepository.deleteFavouriteCity(favouriteCity)
            forecaRepository.deleteForecaForCity(favouriteCity.id)
        }
    }
}