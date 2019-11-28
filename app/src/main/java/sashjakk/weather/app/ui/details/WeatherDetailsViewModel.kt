package sashjakk.weather.app.ui.details

import android.location.LocationManager.GPS_PROVIDER
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import sashjakk.weather.app.adapters.toWeatherViewData
import sashjakk.weather.app.api.OWClient
import sashjakk.weather.app.api.OWResponse
import sashjakk.weather.app.location.LocationProvider
import sashjakk.weather.app.tools.Failure
import sashjakk.weather.app.tools.Result
import sashjakk.weather.app.tools.Success
import sashjakk.weather.app.ui.common.WeatherViewData
import sashjakk.weather.app.ui.common.emptyViewData

@ExperimentalCoroutinesApi
class WeatherDetailsViewModel(
    private val apiClient: OWClient,
    private val locationProvider: LocationProvider
) : ViewModel() {

    private val location = ConflatedBroadcastChannel<Result<OWResponse>>()

    val weatherData: LiveData<Result<WeatherViewData>> = liveData {
        location.consumeEach {
            when (it) {
                is Success -> {
                    val success: Result<WeatherViewData> = Success(it.value.toWeatherViewData())
                    emit(success)
                }

                is Failure -> {
                    val fail = Failure<WeatherViewData>(it.error)

                    emit(fail)
                    emit(Success(emptyViewData))
                }
            }
        }
    }

    fun fetchWeatherData(city: String? = null) {
        viewModelScope.launch {
            if (!city.isNullOrBlank()) {
                location.send(apiClient.getWeatherData(city))
                return@launch
            }

            val (latitude, longitude) = locationProvider.getLastKnownLocation(GPS_PROVIDER)
                ?.let { it.latitude to it.longitude }
                ?: run {
                    location.send(Failure("No location data available"))
                    return@launch
                }

            location.send(apiClient.getWeatherData(latitude, longitude))
        }
    }

}
