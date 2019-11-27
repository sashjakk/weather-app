package sashjakk.weather.app.db

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import sashjakk.weather.app.api.OpenWeatherResponse

@Entity
data class WeatherEntity(
    var city: String = "",
    var date: Long = 0,
    var degrees: Float = 0f,
    var windSpeed: Float = 0f,
    var humidity: Float = 0f,
    var iconUrl: String = "",
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
    @Id var id: Long = 0
)

fun OpenWeatherResponse.toWeatherEntity() = WeatherEntity(
    city = cityName,
    date = date,
    degrees = mainData.degrees,
    windSpeed = windData.speed,
    humidity = mainData.humidity,
    iconUrl = weatherData.firstOrNull()?.icon ?: "",
    latitude = coordinates.latitude,
    longitude = coordinates.longitude
)

interface DatabaseClient<T> {
    suspend fun save(item: T): T
    suspend fun getMatchingBy(item: T): T?
    suspend fun getAll(): List<T>
}
