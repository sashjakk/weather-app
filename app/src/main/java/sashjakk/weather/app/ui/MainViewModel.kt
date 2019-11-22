package sashjakk.weather.app.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

data class WeatherViewData(
    val city: String,
    val date: String,
    val degrees: Float,
    val windSpeed: Float,
    val humidity: Float
)

class MainViewModel : ViewModel() {

    val weatherItem: Flow<WeatherViewData> =
        flow {
            val item = WeatherViewData(
                "New York",
                "Monday 1 Jan 2019",
                20f,
                1.5f,
                15f
            )

            emit(item)
        }
}