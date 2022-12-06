package com.skyyaros.weatherultimate.entity

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@JsonClass(generateAdapter = true)
data class ForecastListTwoWeek(
    @Json(name = "forecast") val forecastList: List<ForecastItemTwoWeek>
)

@Parcelize
@JsonClass(generateAdapter = true)
data class ForecastItemTwoWeek(
    @Json(name = "date") val date: String,
    @Json(name = "symbol") val symbol: String,
    @Json (name = "symbolPhrase") val symbolPhrase: String,
    @Json(name = "maxTemp") val maxTemp: Int,
    @Json(name = "maxFeelsLikeTemp") val maxFeelsLikeTemp: Int,
    @Json(name = "minTemp") val minTemp: Int,
    @Json(name = "minFeelsLikeTemp") val minFeelsLikeTemp: Int,
    @Json(name = "precipAccum") val precipAccum: Double,
    @Json(name = "snowAccum") val snowAccum: Double,
    @Json(name = "precipProb") val precipProb: Int,
    @Json(name = "maxWindSpeed") val maxWindSpeed: Double,
    @Json(name = "maxWindGust") val maxWindGust: Double,
    @Json(name = "windDir") val windDir: Int,
    @Json(name = "sunrise") val sunrise: String?,
    @Json(name = "sunset") val sunset: String?
): Parcelable

@JsonClass(generateAdapter = true)
data class ForecastListHour(
    @Json(name = "forecast") val forecastList: List<ForecastItemHour>
)

@Parcelize
@JsonClass(generateAdapter = true)
data class ForecastItemHour(
    @Json(name = "time") val time: String,
    @Json(name = "symbol") val symbol: String,
    @Json(name = "symbolPhrase") val symbolPhrase: String,
    @Json(name = "temperature") val temperature: Int,
    @Json(name = "feelsLikeTemp") val feelsLikeTemp: Int,
    @Json(name = "windSpeed") val windSpeed: Double,
    @Json(name = "windGust") val windGust: Double,
    @Json(name = "windDirString") val windDirString: String,
    @Json(name = "precipProb") val precipProb: Int,
    @Json(name = "precipAccum") val precipAccum: Double,
    @Json(name = "snowAccum") val snowAccum: Double
): Parcelable

@JsonClass(generateAdapter = true)
data class ForecastNotListCurrent(
    @Json(name = "current") val forecastNotList: ForecastCurrent
)

@Parcelize
@JsonClass(generateAdapter = true)
data class ForecastCurrent(
    @Json(name = "time") val time: String,
    @Json(name = "symbol") val symbol: String,
    @Json(name = "symbolPhrase") val symbolPhrase: String,
    @Json(name = "temperature") val temperature: Int,
    @Json(name = "feelsLikeTemp") val feelsLikeTemp: Int,
    @Json(name = "windSpeed") val windSpeed: Double,
    @Json(name = "windGust") val windGust: Double,
    @Json(name = "windDirString") val windDirString: String,
    @Json(name = "precipProb") val precipProb: Int,
    @Json(name = "precipRate") val precipRate: Double,
    @Json(name = "pressure") val pressure: Double
): Parcelable

data class ForecastWithHelpData(
    val forecastCurrent: ForecastCurrent,
    val forecastListTwoWeek: List<ForecastItemTwoWeek>,
    val forecastListHour: List<ForecastItemHour>,
    val forecastList3Hour: List<ForecastItemHour>,
    val isCache: Boolean
)