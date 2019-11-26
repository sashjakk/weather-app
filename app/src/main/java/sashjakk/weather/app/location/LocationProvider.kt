package sashjakk.weather.app.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.core.content.getSystemService
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

interface LocationProvider {
    fun observeLocation(
        provider: String,
        minInterval: Long,
        minDistance: Float
    ): Flow<Location>
}

class DefaultLocationProvider(context: Context) : LocationProvider {

    private val manager by lazy {
        checkNotNull(context.getSystemService<LocationManager>())
    }

    @SuppressLint("MissingPermission")
    override fun observeLocation(
        provider: String,
        minInterval: Long,
        minDistance: Float
    ): Flow<Location> = callbackFlow {
        val listener = object : LocationListener {
            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
            override fun onProviderEnabled(provider: String?) {}
            override fun onProviderDisabled(provider: String?) {}

            override fun onLocationChanged(location: Location?) {
                location?.let { offer(it) }
            }
        }

        manager.requestLocationUpdates(
            provider,
            minInterval,
            minDistance,
            listener
        )

        offer(manager.getLastKnownLocation(provider))

        awaitClose { manager.removeUpdates(listener) }
    }
}