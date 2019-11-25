package sashjakk.weather.app.tools

import android.annotation.SuppressLint
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

@ExperimentalCoroutinesApi
@SuppressLint("MissingPermission")
fun LocationManager.location(
    provider: String = LocationManager.GPS_PROVIDER,
    minInterval: Long = 1 * 10000,
    minDistance: Float = 10f
): Flow<Location> = callbackFlow {
    val listener = object : LocationListener {
        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
        override fun onProviderEnabled(provider: String?) {}
        override fun onProviderDisabled(provider: String?) {}

        override fun onLocationChanged(location: Location?) {
            location?.let { offer(it) }
        }
    }

    requestLocationUpdates(
        provider,
        minInterval,
        minDistance,
        listener
    )

    offer(getLastKnownLocation(provider))

    awaitClose { removeUpdates(listener) }
}