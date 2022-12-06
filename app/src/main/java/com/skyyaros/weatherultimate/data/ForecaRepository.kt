package com.skyyaros.weatherultimate.data

import com.skyyaros.weatherultimate.entity.*
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.*

class ForecaRepository (
    private val forecaWeatherApi: ForecaWeatherApi,
    private val saveForecaWeatherDao: SaveForecaWeatherDao,
    private val sharedPref: SharedPref,
    private val lastLocationProvider: LastLocationProvider
    ) {
    suspend fun getForecast(location: String, cityId: Long, needCache: Boolean): ForecastWithHelpData? {
        return try {
            if (needCache)
                throw Throwable("Необходим прогноз из бд для раннего местополож.")
            val responseCurrent = forecaWeatherApi.getForecastCurrent(location)
            val responseTwoWeek = forecaWeatherApi.getForecastTwoWeek(location)
            val responseHour = forecaWeatherApi.getForecastHour(location)
            val response3Hour = forecaWeatherApi.getForecast3Hour(location)
            if (responseCurrent.isSuccessful && responseTwoWeek.isSuccessful && responseHour.isSuccessful && response3Hour.isSuccessful) {
                val forecastCurrent = responseCurrent.body()!!.forecastNotList
                val listForecastTwoWeek = responseTwoWeek.body()!!.forecastList
                val listForecastHour = responseHour.body()!!.forecastList
                val listForecast3Hour = response3Hour.body()!!.forecastList

                saveForecaWeatherDao.deleteTwoWeekAll(cityId)
                saveForecaWeatherDao.deleteHourAll(cityId)
                saveForecaWeatherDao.delete3HourAll(cityId)

                val listForecastTwoWeekRoom = listForecastTwoWeek.map { ForecastItemTwoWeekRoom.getItem(it, cityId) }
                saveForecaWeatherDao.insertTwoWeek(listForecastTwoWeekRoom)
                val listForecastHourRoom = listForecastHour.map { ForecastItemHourRoom.getItem(it, cityId) }
                saveForecaWeatherDao.insertHour(listForecastHourRoom)
                val listForecast3HourRoom = listForecast3Hour.map { ForecastItem3HourRoom.getItem(it, cityId) }
                saveForecaWeatherDao.insert3Hour(listForecast3HourRoom)

                ForecastWithHelpData(forecastCurrent, listForecastTwoWeek, listForecastHour, listForecast3Hour, false)
            } else {
                throw Throwable("Ошибка получения прогноза!")
            }
        } catch (t: Throwable) {
            val curDate = Date()
            var sdf = SimpleDateFormat("yyyy-MM-dd")
            var trimDate = sdf.format(curDate)
            var trimTime = sdf.parse(trimDate).time
            saveForecaWeatherDao.deleteTwoWeekOld(cityId, trimTime)
            sdf = SimpleDateFormat("yyyy-MM-dd HH:mm")
            trimDate = sdf.format(curDate).substringBefore(":") + ":00" //срезали ненужные минуты
            trimTime = sdf.parse(trimDate).time
            saveForecaWeatherDao.deleteHourOld(cityId, trimTime)
            var divHour = trimDate.substringAfter(" ").substringBefore(":").toInt()
            divHour -= divHour % 3
            val normTime = if (divHour >= 10) "$divHour" else "0$divHour"
            trimDate = trimDate.substringBefore(" ") + " $normTime:00"
            trimTime = sdf.parse(trimDate).time
            saveForecaWeatherDao.delete3HourOld(cityId, trimTime)

            val listTwoWeekRoom = saveForecaWeatherDao.getTwoWeek(cityId).sortedBy { it.time }
            val listHourRoom = saveForecaWeatherDao.getHour(cityId).sortedBy { it.time }
            val list3HourRoom = saveForecaWeatherDao.get3Hour(cityId).sortedBy { it.time }
            if (listTwoWeekRoom.isNotEmpty()) {
                val listTwoWeek = listTwoWeekRoom.map { it.toApiModel() }
                val listHour = listHourRoom.map { it.toApiModel() }
                val list3Hour = list3HourRoom.map { it.toApiModel() }
                val current = if (listHour.isNotEmpty()) {
                    val hour = listHour[0]
                    ForecastCurrent(
                        hour.time, hour.symbol, hour.symbolPhrase,
                        hour.temperature, hour.feelsLikeTemp,
                        hour.windSpeed, hour.windGust, hour.windDirString,
                        hour.precipProb, hour.precipAccum, -1.0
                    )
                } else {
                    val hour = list3Hour[0]
                    ForecastCurrent(
                        hour.time, hour.symbol, hour.symbolPhrase,
                        hour.temperature, hour.feelsLikeTemp,
                        hour.windSpeed, hour.windGust, hour.windDirString,
                        hour.precipProb, hour.precipAccum, -1.0
                    )
                }
                ForecastWithHelpData(current, listTwoWeek, listHour, list3Hour, true)
            } else {
                null
            }
        }
    }

    fun getFavouriteCities(): Flow<List<FavouriteCity>> {
        return saveForecaWeatherDao.getFavouriteCities()
    }

    suspend fun getCities(citySearch: String): List<FavouriteCity>? {
        return try {
            val responce = forecaWeatherApi.getCities(citySearch)
            if (responce.isSuccessful) {
                responce.body()!!.locations
            } else {
                throw Throwable("Ошибка получения списка городов!")
            }
        } catch (t: Throwable) {
            null
        }
    }

    suspend fun insertCity(favouriteCity: FavouriteCity) {
        saveForecaWeatherDao.insertCity(favouriteCity)
    }

    suspend fun deleteFavouriteCity(favouriteCity: FavouriteCity) {
        saveForecaWeatherDao.deleteFavouriteCity(favouriteCity)
    }

    fun saveSearch(nameCity: String) {
        sharedPref.saveSearch(nameCity)
    }

    fun getSearch(): String {
        return sharedPref.getSearch()
    }

    fun getCurrentCity(): FavouriteCity? {
        return sharedPref.getCurrentCity()
    }

    fun saveCurrentCity(favouriteCity: FavouriteCity) {
        sharedPref.saveCurrentCity(favouriteCity)
    }

    fun deleteCity() {
        sharedPref.deleteCity()
    }

    fun getLocation(): FavouriteCity? {
        return sharedPref.getLocation()
    }

    fun saveLocation(favouriteCity: FavouriteCity) {
        sharedPref.saveLocation(favouriteCity)
    }

    suspend fun getNameCity(location: String): FavouriteCity? {
        return try {
            val response = forecaWeatherApi.getCityName(location)
            if (response.isSuccessful)
                response.body()!!
            else
                null
        } catch (t: Throwable) {
            null
        }
    }

    suspend fun getUserLocation(): String? {
        val resultLocation = lastLocationProvider.getLocation()
        return if (resultLocation.isSuccess) {
            val location = resultLocation.getOrNull()!!
            "${location.longitude},${location.latitude}"
        } else {
            null
        }
    }

    suspend fun deleteForecaForCity(cityId: Long) {
        saveForecaWeatherDao.deleteTwoWeekAll(cityId)
        saveForecaWeatherDao.deleteHourAll(cityId)
        saveForecaWeatherDao.delete3HourAll(cityId)
    }
}