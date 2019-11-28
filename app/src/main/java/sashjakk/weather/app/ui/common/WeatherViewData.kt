package sashjakk.weather.app.ui.common

data class WeatherViewData(
    val city: String,
    val date: String,
    val degrees: Float,
    val windSpeed: Float,
    val humidity: Float,
    val iconUrl: String
)

val emptyViewData = WeatherViewData(
    city = "X",
    date = "X",
    degrees = 0.0f,
    windSpeed = 0.0f,
    humidity = 0.0f,
    iconUrl = "https://raw.githubusercontent.com/sashjakk/weather-app-icons/master/icons/50n.svg"
)