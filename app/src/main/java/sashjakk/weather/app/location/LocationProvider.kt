package sashjakk.weather.app.location

import android.location.Location
import kotlinx.coroutines.flow.Flow

interface LocationProvider {
    fun getLocation(provider: String, minInterval: Long, minDistance: Float): Flow<Location>
}