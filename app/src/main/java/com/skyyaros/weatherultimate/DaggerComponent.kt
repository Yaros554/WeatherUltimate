package com.skyyaros.weatherultimate

import android.app.Application
import com.skyyaros.weatherultimate.data.*
import dagger.Component
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Named
import javax.inject.Singleton

@Singleton
@Component(
    modules = [DaggerModule::class],
)
interface DaggerComponent {
    fun forecaRepo(): ForecaRepository
}

@Module
class DaggerModule(private val application: Application) {
    @Provides
    @Singleton
    fun lastLocationProvider(): LastLocationProvider = LastLocationProvider(application.applicationContext)

    @Provides
    @Singleton
    fun sharedPref(): SharedPref = SharedPref(application.applicationContext)

    @Provides
    @Singleton
    fun forecaWeatherApi(): ForecaWeatherApi = ForecaWeatherApi.provide()

    @Provides
    @Singleton
    fun saveForecaWeatherDao(): SaveForecaWeatherDao = SaveForecaWeatherDao.provide(application.applicationContext)

    @Provides
    @Singleton
    fun forecaRepository(
        forecaWeatherApi: ForecaWeatherApi,
        saveForecaWeatherDao: SaveForecaWeatherDao,
        sharedPref: SharedPref,
        lastLocationProvider: LastLocationProvider
    ): ForecaRepository = ForecaRepository(forecaWeatherApi, saveForecaWeatherDao, sharedPref, lastLocationProvider)
}