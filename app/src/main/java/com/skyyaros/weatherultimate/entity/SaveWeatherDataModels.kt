package com.skyyaros.weatherultimate.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize
import java.text.SimpleDateFormat
import java.util.Date

@JsonClass(generateAdapter = true)
data class CitiesList(
    @Json(name = "locations") val locations: List<FavouriteCity>
)

@Parcelize
@JsonClass(generateAdapter = true)
@Entity(tableName = "favouriteCities")
data class FavouriteCity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    @Json(name = "id")
    val id: Long,
    @ColumnInfo(name = "name")
    @Json(name = "name")
    var name: String,
    @ColumnInfo(name = "country")
    @Json(name = "country")
    val country: String,
    @ColumnInfo(name = "adminArea")
    @Json(name = "adminArea")
    val adminArea: String?,
    @ColumnInfo(name = "lon")
    @Json(name = "lon")
    val lon: Double,
    @ColumnInfo(name = "lat")
    @Json(name = "lat")
    val lat: Double
): Parcelable {
    override fun toString(): String {
        return "${id}_${name}_${country}_${adminArea}_${lon}_${lat}"
    }

    companion object {
        fun fromString(str: String): FavouriteCity {
            val listStr = str.split("_")
            return FavouriteCity(
                listStr[0].toLong(),
                listStr[1],
                listStr[2],
                listStr[3],
                listStr[4].toDouble(),
                listStr[5].toDouble()
            )
        }
    }
}

@Entity(tableName = "forecastTwoWeek", primaryKeys = ["cityId", "time"])
data class ForecastItemTwoWeekRoom(
    @ColumnInfo(name = "cityId") val cityId: Long,
    @ColumnInfo(name = "time") val time: Long,
    @ColumnInfo(name = "symbol") val symbol: String,
    @ColumnInfo(name = "symbolPhrase") val symbolPhrase: String,
    @ColumnInfo(name = "maxTemp") val maxTemp: Int,
    @ColumnInfo(name = "maxFeelsLikeTemp") val maxFeelsLikeTemp: Int,
    @ColumnInfo(name = "minTemp") val minTemp: Int,
    @ColumnInfo(name = " minFeelsLikeTemp") val minFeelsLikeTemp: Int,
    @ColumnInfo(name = "precipAccum") val precipAccum: Double,
    @ColumnInfo(name = "snowAccum") val snowAccum: Double,
    @ColumnInfo(name = "precipProb") val precipProb: Int,
    @ColumnInfo(name = "maxWindSpeed") val maxWindSpeed: Double,
    @ColumnInfo(name = "maxWindGust") val maxWindGust: Double,
    @ColumnInfo(name = "windDir") val windDir: Int,
    @ColumnInfo(name = "sunrise") val sunrise: String?,
    @ColumnInfo(name = "sunset") val sunset: String?
) {
    fun toApiModel(): ForecastItemTwoWeek {
        val date = Date(time)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val finalDate = dateFormat.format(date)
        return ForecastItemTwoWeek(
            finalDate, symbol, symbolPhrase,
            maxTemp, maxFeelsLikeTemp, minTemp, minFeelsLikeTemp,
            precipAccum, snowAccum, precipProb,
            maxWindSpeed, maxWindGust, windDir,
            sunrise, sunset
        )
    }

    companion object {
        fun getItem(fTwoWeek: ForecastItemTwoWeek, cityId: Long): ForecastItemTwoWeekRoom {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd")
            val parsedDate = dateFormat.parse(fTwoWeek.date)
            return ForecastItemTwoWeekRoom(
                cityId, parsedDate.time, fTwoWeek.symbol, fTwoWeek.symbolPhrase,
                fTwoWeek.maxTemp, fTwoWeek.maxFeelsLikeTemp, fTwoWeek.minTemp, fTwoWeek.minFeelsLikeTemp,
                fTwoWeek.precipAccum, fTwoWeek.snowAccum, fTwoWeek.precipProb, fTwoWeek.maxWindSpeed,
                fTwoWeek.maxWindGust, fTwoWeek.windDir, fTwoWeek.sunrise, fTwoWeek.sunset
            )
        }
    }
}

@Entity(tableName = "forecastHour", primaryKeys = ["cityId", "time"])
data class ForecastItemHourRoom(
    @ColumnInfo(name = "cityId") val cityId: Long,
    @ColumnInfo(name = "time") val time: Long,
    @ColumnInfo(name = "symbol") val symbol: String,
    @ColumnInfo(name = "symbolPhrase") val symbolPhrase: String,
    @ColumnInfo(name = "temperature") val temperature: Int,
    @ColumnInfo(name = "feelsLikeTemp") val feelsLikeTemp: Int,
    @ColumnInfo(name = "windSpeed") val windSpeed: Double,
    @ColumnInfo(name = "windGust") val windGust: Double,
    @ColumnInfo(name = "windDirString") val windDirString: String,
    @ColumnInfo(name = "precipProb") val precipProb: Int,
    @ColumnInfo(name = "precipAccum") val precipAccum: Double,
    @ColumnInfo(name = "snowAccum") val snowAccum: Double
) {
    fun toApiModel(): ForecastItemHour {
        val date = Date(time)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm")
        val finalDate = dateFormat.format(date).replace(' ', 'T') + "+03:00" //этот хвост никому не нужен, добавляется ради сохранения единого формата
        return ForecastItemHour(
            finalDate, symbol, symbolPhrase,
            temperature, feelsLikeTemp,
            windSpeed, windGust, windDirString,
            precipProb, precipAccum, snowAccum
        )
    }
    companion object {
        fun getItem(fHour: ForecastItemHour, cityId: Long): ForecastItemHourRoom {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm")
            val parsedDate = dateFormat.parse(fHour.time.substringBefore("+").replace('T', ' ', true))
            return ForecastItemHourRoom(
                cityId, parsedDate.time, fHour.symbol, fHour.symbolPhrase, fHour.temperature, fHour.feelsLikeTemp,
                fHour.windSpeed, fHour.windGust, fHour.windDirString, fHour.precipProb, fHour.precipAccum, fHour.snowAccum
            )
        }
    }
}

@Entity(tableName = "forecast3Hour", primaryKeys = ["cityId", "time"])
data class ForecastItem3HourRoom(
    @ColumnInfo(name = "cityId") val cityId: Long,
    @ColumnInfo(name = "time") val time: Long,
    @ColumnInfo(name = "symbol") val symbol: String,
    @ColumnInfo(name = "symbolPhrase") val symbolPhrase: String,
    @ColumnInfo(name = "temperature") val temperature: Int,
    @ColumnInfo(name = "feelsLikeTemp") val feelsLikeTemp: Int,
    @ColumnInfo(name = "windSpeed") val windSpeed: Double,
    @ColumnInfo(name = "windGust") val windGust: Double,
    @ColumnInfo(name = "windDirString") val windDirString: String,
    @ColumnInfo(name = "precipProb") val precipProb: Int,
    @ColumnInfo(name = "precipAccum") val precipAccum: Double,
    @ColumnInfo(name = "snowAccum") val snowAccum: Double
) {
    fun toApiModel(): ForecastItemHour {
        val date = Date(time)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm")
        val finalDate = dateFormat.format(date).replace(' ', 'T') + "+03:00" //этот хвост никому не нужен, добавляется ради сохранения единого формата
        return ForecastItemHour(
            finalDate, symbol, symbolPhrase,
            temperature, feelsLikeTemp,
            windSpeed, windGust, windDirString,
            precipProb, precipAccum, snowAccum
        )
    }

    companion object {
        fun getItem(fHour: ForecastItemHour, cityId: Long): ForecastItem3HourRoom {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm")
            val parsedDate = dateFormat.parse(fHour.time.substringBefore("+").replace('T', ' ', true))
            return ForecastItem3HourRoom(
                cityId, parsedDate.time, fHour.symbol, fHour.symbolPhrase, fHour.temperature, fHour.feelsLikeTemp,
                fHour.windSpeed, fHour.windGust, fHour.windDirString, fHour.precipProb, fHour.precipAccum, fHour.snowAccum
            )
        }
    }
}