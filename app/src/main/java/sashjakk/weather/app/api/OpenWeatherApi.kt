package sashjakk.weather.app.api

import com.google.gson.annotations.SerializedName
import sashjakk.weather.app.db.WeatherEntity
import sashjakk.weather.app.tools.Result

data class OpenWeatherResponse(
    @SerializedName("name") val cityName: String,
    @SerializedName("coord") val coordinates: Coordinates,
    @SerializedName("main") val mainData: MainData,
    @SerializedName("wind") val windData: WindData,
    @SerializedName("weather") val weatherData: List<WeatherData>,
    @SerializedName("dt") val date: Long
)

fun WeatherEntity.toOpenWeatherResponse() = OpenWeatherResponse(
    cityName = city,
    coordinates = Coordinates(latitude, longitude),
    mainData = MainData(degrees, humidity),
    windData = WindData(windSpeed),
    weatherData = listOf(WeatherData(iconUrl)),
    date = date
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

data class Coordinates(
    @SerializedName("lat") val latitude: Double,
    @SerializedName("lon") val longitude: Double
)

interface OpenWeatherClient {
    suspend fun getWeatherData(cityName: String): Result<OpenWeatherResponse>
    suspend fun getWeatherData(latitude: Double, longitude: Double): Result<OpenWeatherResponse>
}
