package sashjakk.weather.app.api

import com.google.gson.annotations.SerializedName

data class OpenWeatherResponse(
    @SerializedName("name") val cityName: String,
    @SerializedName("main") val mainData: MainData,
    @SerializedName("wind") val windData: WindData,
    @SerializedName("weather") val weatherData: List<WeatherData>,
    @SerializedName("dt") val date: Long
)

data class MainData(
    @SerializedName("temp") val degrees: Float,
    val humidity: Float
)

data class WindData(
    val speed: Float
)

data class WeatherData(
    val icon: String
)

interface OpenWeatherClient {
    suspend fun getWeatherData(latitude: Double, longitude: Double): OpenWeatherResponse

    fun getIconUrl(icon: String): String
}
