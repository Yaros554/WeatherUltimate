package com.skyyaros.weatherultimate.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.skyyaros.weatherultimate.entity.FavouriteCity
import com.skyyaros.weatherultimate.entity.ForecastItem3HourRoom
import com.skyyaros.weatherultimate.entity.ForecastItemHourRoom
import com.skyyaros.weatherultimate.entity.ForecastItemTwoWeekRoom

@Database(entities = [FavouriteCity::class, ForecastItemTwoWeekRoom::class, ForecastItemHourRoom::class, ForecastItem3HourRoom::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun saveForecaWeatherDao(): SaveForecaWeatherDao
}