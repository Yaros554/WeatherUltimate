package com.skyyaros.weatherultimate.ui.main

import com.skyyaros.weatherultimate.entity.ForecastWithHelpData

sealed class StateForecast {
    data class Loading(val text: String): StateForecast()
    data class Success(val forecastWithHelpData: ForecastWithHelpData): StateForecast()
    data class Error(val message: String): StateForecast()
}