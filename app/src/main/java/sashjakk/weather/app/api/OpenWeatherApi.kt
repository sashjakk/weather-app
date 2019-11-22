package sashjakk.weather.app.api

import com.google.gson.annotations.SerializedName

data class Weather(
    val name: String,
    @SerializedName("main") val temperature: Temperature
)

data class Temperature(
    @SerializedName("temp") val value: Float
)

interface OpenWeatherClient {
    suspend fun getWeatherData(city: String): Weather
}
