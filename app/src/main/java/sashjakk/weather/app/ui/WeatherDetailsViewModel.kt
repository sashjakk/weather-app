package sashjakk.weather.app.ui

import android.location.LocationManager.GPS_PROVIDER
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import sashjakk.weather.app.api.OpenWeatherClient
import sashjakk.weather.app.api.OpenWeatherResponse
import sashjakk.weather.app.location.LocationProvider
import sashjakk.weather.app.tools.Failure
import sashjakk.weather.app.tools.Result
import sashjakk.weather.app.tools.Success
import java.text.SimpleDateFormat
import java.util.*

data class WeatherViewData(
    val city: String,
    val date: String,
    val degrees: Float,
    val windSpeed: Float,
    val humidity: Float,
    val iconUrl: String
)

fun OpenWeatherResponse.toWeatherViewData(): WeatherViewData {
    val dateFormatter = SimpleDateFormat("EEEE, dd MMM yyyy", Locale.getDefault())

    return WeatherViewData(
        city = cityName,
        date = dateFormatter.format(date * 1000L),
        degrees = mainData.degrees,
        windSpeed = windData.speed,
        humidity = mainData.humidity,
        iconUrl = ""
    )
}

class WeatherDetailsViewModel(
    private val apiClient: OpenWeatherClient,
    private val locationProvider: LocationProvider
) : ViewModel() {

    val weatherData: LiveData<Result<WeatherViewData>> = liveData {
        locationProvider
            .observeLocation(GPS_PROVIDER, 1 * 10000, 1 * 10f)
            .map { apiClient.getWeatherData(it.latitude, it.longitude) }
            .map {
                when (it) {
                    is Success -> Success(it.value.toWeatherViewData())
                    is Failure -> Failure<WeatherViewData>(it.error)
                }
            }
            .collect(::emit)
    }
}
