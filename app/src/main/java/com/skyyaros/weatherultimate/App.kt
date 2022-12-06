package com.skyyaros.weatherultimate

import android.app.Application
import android.content.Context

class App: Application() {
    companion object {
        lateinit var component: DaggerComponent
        val deleteCities = mutableListOf<Long>()
    }

    override fun onCreate() {
        super.onCreate()
        component = DaggerDaggerComponent.builder()
            .daggerModule(DaggerModule(this))
            .build()
    }
}