package sashjakk.weather.app.api

import com.google.gson.annotations.SerializedName
import sashjakk.weather.app.tools.Result

data class OWResponse(
    @SerializedName("name") val cityName: String,
    @SerializedName("coord") val coordinates: Coordinates,
    @SerializedName("main") val main: Main,
    @SerializedName("wind") val wind: Wind,
    @SerializedName("weather") val weatherData: List<Weather>,
    @SerializedName("dt") val date: Long
)

data class Main(
    @SerializedName("temp") val degrees: Float,
    val humidity: Float
)

data class Wind(
    val speed: Float
)

data class Weather(
    val icon: String
)

data class Coordinates(
    @SerializedName("lat") val latitude: Double,
    @SerializedName("lon") val longitude: Double
)

interface OWClient {
    suspend fun getWeatherData(cityName: String): Result<OWResponse>
    suspend fun getWeatherData(latitude: Double, longitude: Double): Result<OWResponse>
}
