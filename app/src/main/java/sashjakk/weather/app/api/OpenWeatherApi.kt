package sashjakk.weather.app.api

import com.google.gson.annotations.SerializedName

data class WeatherData(
    @SerializedName("name") val cityName: String,
    @SerializedName("main") val mainData: MainData,
    @SerializedName("wind") val windData: WindData,
    @SerializedName("dt") val date: Long
)

data class MainData(
    @SerializedName("temp") val degrees: Float,
    val humidity: Float
)

data class WindData(
    val speed: Float
)

interface OpenWeatherClient {
    suspend fun getWeatherData(city: String): WeatherData
}
