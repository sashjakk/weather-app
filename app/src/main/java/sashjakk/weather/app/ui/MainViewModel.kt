package sashjakk.weather.app.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import sashjakk.weather.app.api.OpenWeatherClient

data class WeatherViewData(
    val city: String,
    val date: String,
    val degrees: Float,
    val windSpeed: Float,
    val humidity: Float
)

class MainViewModel(
    private val apiClient: OpenWeatherClient
) : ViewModel() {

    val weatherData: Flow<WeatherViewData> = flow {
        val response = apiClient.getWeatherData("Riga")

        val instance = WeatherViewData(
            city = response.cityName,
            date = response.date.toString(),
            degrees = response.mainData.degrees,
            windSpeed = response.windData.speed,
            humidity = response.mainData.humidity
        )

        emit(instance)
    }
}