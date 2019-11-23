package sashjakk.weather.app.ui

import androidx.lifecycle.ViewModel
import sashjakk.weather.app.api.OpenWeatherClient
import java.text.SimpleDateFormat

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

    suspend fun getWeatherData(latitude: Double, longitude: Double): WeatherViewData {
        val response = apiClient.getWeatherData(latitude, longitude)

        val icon = response.weatherData
            .first()
            .icon

        val iconUrl = apiClient.getIconUrl(icon)

        val dateFormatter = SimpleDateFormat("EEEE, dd MMM yyyy")

        return WeatherViewData(
            city = response.cityName,
            date = dateFormatter.format(response.date * 1000L),
            degrees = response.mainData.degrees,
            windSpeed = response.windData.speed,
            humidity = response.mainData.humidity,
            iconUrl = iconUrl
        )
    }
}