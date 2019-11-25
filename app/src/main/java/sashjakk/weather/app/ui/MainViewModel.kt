package sashjakk.weather.app.ui

import androidx.lifecycle.ViewModel
import sashjakk.weather.app.api.OpenWeatherClient
import sashjakk.weather.app.api.OpenWeatherResponse
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

class MainViewModel(
    private val apiClient: OpenWeatherClient
) : ViewModel() {

    suspend fun getWeatherData(
        latitude: Double,
        longitude: Double
    ): Result<WeatherViewData> {
        val result = apiClient.getWeatherData(latitude, longitude)

        return when (result) {
            is Success -> Success(mapToWeatherViewData(result.value))
            is Failure -> Failure(result.error)
        }
    }
}

fun mapToWeatherViewData(response: OpenWeatherResponse): WeatherViewData {
    val dateFormatter = SimpleDateFormat("EEEE, dd MMM yyyy", Locale.getDefault())

    return WeatherViewData(
        city = response.cityName,
        date = dateFormatter.format(response.date * 1000L),
        degrees = response.mainData.degrees,
        windSpeed = response.windData.speed,
        humidity = response.mainData.humidity,
        iconUrl = ""
    )
}
