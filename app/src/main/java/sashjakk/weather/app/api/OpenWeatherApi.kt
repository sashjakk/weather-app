package sashjakk.weather.app.api

import com.google.gson.annotations.SerializedName

data class OpenWeatherResponse(
    @SerializedName("name") val cityName: String,
    @SerializedName("coord") val coordinates: Coordinates,
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

data class Coordinates(
    @SerializedName("lat") val latitude: Double,
    @SerializedName("lon") val longitude: Double
)

class Result<T> private constructor(
    private val _result: T? = null,
    private val _error: Throwable? = null
) {
    val isSuccess: Boolean get() = _result != null

    val isFailure: Boolean get() = _result == null

    val result: T get() = if (isSuccess) _result!! else throw IllegalStateException()

    val error: Throwable get() = if (isFailure) _error!! else throw IllegalStateException()

    companion object {
        fun <T> success(result: T) = Result(result, null)

        fun <T> failure(error: Throwable) = Result<T>(null, error)
        fun <T> failure(message: String) = Result<T>(null, Error(message))
    }
}

interface OpenWeatherClient {
    suspend fun getWeatherData(latitude: Double, longitude: Double): Result<OpenWeatherResponse>

    fun getIconUrl(icon: String): String
}
