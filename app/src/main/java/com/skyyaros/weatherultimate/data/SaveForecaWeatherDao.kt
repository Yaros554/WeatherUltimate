package com.skyyaros.weatherultimate.data

import android.content.Context
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import com.skyyaros.weatherultimate.App
import com.skyyaros.weatherultimate.entity.FavouriteCity
import com.skyyaros.weatherultimate.entity.ForecastItem3HourRoom
import com.skyyaros.weatherultimate.entity.ForecastItemHourRoom
import com.skyyaros.weatherultimate.entity.ForecastItemTwoWeekRoom
import kotlinx.coroutines.flow.Flow

@Dao
interface SaveForecaWeatherDao {
    @Query("SELECT * FROM favouriteCities")
    fun getFavouriteCities(): Flow<List<FavouriteCity>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCity(favouriteCity: FavouriteCity)
    @Delete
    suspend fun deleteFavouriteCity(favouriteCity: FavouriteCity)

    @Insert
    suspend fun insertTwoWeek(forecastTwoWeekRoom: List<ForecastItemTwoWeekRoom>)
    @Query("DELETE FROM forecastTwoWeek WHERE cityId = :cityId")
    suspend fun deleteTwoWeekAll(cityId: Long)
    @Query("DELETE FROM forecastTwoWeek WHERE cityId = :cityId and time < :curTime")
    suspend fun deleteTwoWeekOld(cityId: Long, curTime: Long)
    @Query("SELECT * FROM forecastTwoWeek WHERE cityId = :cityId")
    suspend fun getTwoWeek(cityId: Long): List<ForecastItemTwoWeekRoom>

    @Insert
    suspend fun insertHour(forecastHourRoom: List<ForecastItemHourRoom>)
    @Query("DELETE FROM forecastHour WHERE cityId = :cityId")
    suspend fun deleteHourAll(cityId: Long)
    @Query("DELETE FROM forecastHour WHERE cityId = :cityId and time < :curTime")
    suspend fun deleteHourOld(cityId: Long, curTime: Long)
    @Query("SELECT * FROM forecastHour WHERE cityId = :cityId")
    suspend fun getHour(cityId: Long): List<ForecastItemHourRoom>

    @Insert
    suspend fun insert3Hour(forecast3HourRoom: List<ForecastItem3HourRoom>)
    @Query("DELETE FROM forecast3Hour WHERE cityId = :cityId")
    suspend fun delete3HourAll(cityId: Long)
    @Query("DELETE FROM forecast3Hour WHERE cityId = :cityId and time < :curTime")
    suspend fun delete3HourOld(cityId: Long, curTime: Long)
    @Query("SELECT * FROM forecast3Hour WHERE cityId = :cityId")
    suspend fun get3Hour(cityId: Long): List<ForecastItem3HourRoom>

    companion object RoomDbProvider {
        private const val DATABASE_NAME = "ForecaDb"

        fun provide(context: Context): SaveForecaWeatherDao {
            val db : AppDatabase = Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                DATABASE_NAME)
                .build()
            return db.saveForecaWeatherDao()
        }
    }
}