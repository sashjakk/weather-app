package sashjakk.weather.app.api

import sashjakk.weather.app.db.DatabaseClient
import sashjakk.weather.app.tools.Failure
import sashjakk.weather.app.tools.Result
import sashjakk.weather.app.tools.Success
import kotlin.math.abs

class DatabaseOpenWeatherClient(
    private val databaseClient: DatabaseClient<OpenWeatherResponse>
) : OpenWeatherClient {

    override suspend fun getWeatherData(
        latitude: Double,
        longitude: Double
    ): Result<OpenWeatherResponse> {
        val result = databaseClient.getAll()
            .find {
                val (lat, lon) = it.coordinates
                abs(lat - latitude) > 0.001 && abs(lon - longitude) > 0.001
            }

        return result?.let { Success(it) } ?: Failure("Not found in database")
    }

    override fun getIconUrl(icon: String): String {
        return ""
    }
}