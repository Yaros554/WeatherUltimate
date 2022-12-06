package com.skyyaros.weatherultimate.data

import android.content.Context
import com.skyyaros.weatherultimate.App
import com.skyyaros.weatherultimate.entity.FavouriteCity

class SharedPref(context: Context) {
    private val keySearch = "key_search"
    private val keyCity = "key_city"
    private val keyLocation = "key_location"
    private val sharedPreferences = context.getSharedPreferences("my_settings", Context.MODE_PRIVATE)

    fun saveSearch(nameCity: String) {
        sharedPreferences.edit().putString(keySearch, nameCity).apply()
    }

    fun getSearch(): String {
        return  sharedPreferences.getString(keySearch, "") ?: ""
    }

    fun saveCurrentCity(city: FavouriteCity) {
        sharedPreferences.edit().putString(keyCity, city.toString()).apply()
    }

    fun getCurrentCity(): FavouriteCity? {
        val cityStr = sharedPreferences.getString(keyCity, null)
        return if (cityStr != null) {
            FavouriteCity.fromString(cityStr)
        } else {
            null
        }
    }

    fun deleteCity() {
        sharedPreferences.edit().remove(keyCity).apply()
    }

    fun saveLocation(city: FavouriteCity) {
        sharedPreferences.edit().putString(keyLocation, city.toString()).apply()
    }

    fun getLocation(): FavouriteCity? {
        val cityStr = sharedPreferences.getString(keyLocation, null)
        return if (cityStr != null) {
            FavouriteCity.fromString(cityStr)
        } else {
            null
        }
    }
}