package sashjakk.weather.app.db

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

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

interface DatabaseDao<T> {
    suspend fun save(item: T): T
    suspend fun getMatchingBy(item: T): T?
    suspend fun getAll(): List<T>
}
