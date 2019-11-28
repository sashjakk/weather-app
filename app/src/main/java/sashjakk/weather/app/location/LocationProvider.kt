package sashjakk.weather.app.location

import android.location.Location
import kotlinx.coroutines.flow.Flow

interface LocationProvider {
    fun observeLocation(
        provider: String,
        minInterval: Long,
        minDistance: Float
    ): Flow<Location>

    fun getLastKnownLocation(provider: String): Location?
}