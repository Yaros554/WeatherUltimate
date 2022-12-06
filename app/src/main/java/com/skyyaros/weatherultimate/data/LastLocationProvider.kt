package com.skyyaros.weatherultimate.data

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.Task
import com.skyyaros.weatherultimate.App
import kotlinx.coroutines.delay
import java.util.NoSuchElementException

class LastLocationProvider (val context: Context) {
    suspend fun getLocation(): Result<Location> {
        val lastLocationTask = getLocationTask()
        while (!lastLocationTask.isComplete) {
            delay(100)
        }
        val location = lastLocationTask.getLocation()
        return if (location != null) {
            Result.success(location)
        } else {
            Result.failure(NoSuchElementException("No location found"))
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLocationTask(): Task<Location> {
        return LocationServices
            .getFusedLocationProviderClient(context)
            .getCurrentLocation(
                CurrentLocationRequest.Builder()
                    .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                    .setMaxUpdateAgeMillis(1000*60*2)
                    .build(),
                null
            )
    }

    private fun Task<Location>.getLocation(): Location? {
        return if (isSuccessful && result != null) {
            result!!
        } else {
            null
        }
    }
}