package com.skyyaros.weatherultimate.data

import com.skyyaros.weatherultimate.entity.*
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.Locale


interface ForecaWeatherApi {
    @GET("api/v1/current/{location}")
    suspend fun getForecastCurrent(
        @Path("location") location: String,
        @Query("lang") lang: String = if (Locale.getDefault().language == "ru") "ru" else "en"
    ): Response<ForecastNotListCurrent>

    @GET("api/v1/forecast/daily/{location}")
    suspend fun getForecastTwoWeek(
        @Path("location") location: String,
        @Query("periods") periods: Int = 14,
        @Query("dataset") dataset: String = "full",
        @Query("lang") lang: String = if (Locale.getDefault().language == "ru") "ru" else "en"
    ): Response<ForecastListTwoWeek>

    @GET("api/v1/forecast/hourly/{location}")
    suspend fun getForecastHour(
        @Path("location") location: String,
        @Query("periods") periods: Int = 168,
        @Query("dataset") dataset: String = "full",
        @Query("lang") lang: String = if (Locale.getDefault().language == "ru") "ru" else "en"
    ): Response<ForecastListHour>

    @GET("api/v1/forecast/3hourly/{location}")
    suspend fun getForecast3Hour(
        @Path("location") location: String,
        @Query("periods") periods: Int = 112,
        @Query("dataset") dataset: String = "full",
        @Query("lang") lang: String = if (Locale.getDefault().language == "ru") "ru" else "en"
    ): Response<ForecastListHour>

    @GET("api/v1/location/search/{query}")
    suspend fun getCities(
        @Path("query") query: String,
        @Query("lang") lang: String = if (Locale.getDefault().language == "ru") "ru" else "en"
    ): Response<CitiesList>

    @GET("api/v1/location/{query}")
    suspend fun getCityName(
        @Path("query") query: String,
        @Query("lang") lang: String = if (Locale.getDefault().language == "ru") "ru" else "en"
    ): Response<FavouriteCity>

    companion object RetrofitProvider {
        private const val BASE_URL = "https://pfa.foreca.com/"
        private const val TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOlwvXC9wZmEuZm9yZWNhLmNvbVwvYXV0aG9yaXplXC90b2tlbiIsImlhdCI6MTY2ODU1MzkzMiwiZXhwIjo5OTk5OTk5OTk5LCJuYmYiOjE2Njg1NTM5MzIsImp0aSI6IjVlNWNhZjQwMzAwMGNkNGYiLCJzdWIiOiJ5YXJvc2xhdm9wYXJrbyIsImZtdCI6IlhEY09oakM0MCtBTGpsWVR0amJPaUE9PSJ9.ENuCE_xVDg_vV6lfWz4xhdrqliVCfVKxq0d6DF9APfg"

        fun provide(): ForecaWeatherApi {
            val client = OkHttpClient.Builder().addInterceptor { chain ->
                val newRequest: Request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $TOKEN")
                    .build()
                chain.proceed(newRequest)
            }.build()
            return Retrofit.Builder()
                .client(client)
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
                .create(ForecaWeatherApi::class.java)
        }
    }
}