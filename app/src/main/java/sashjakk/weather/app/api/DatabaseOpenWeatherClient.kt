package sashjakk.weather.app.api

import sashjakk.weather.app.db.DatabaseClient
import sashjakk.weather.app.db.WeatherEntity
import sashjakk.weather.app.tools.Failure
import sashjakk.weather.app.tools.Result
import sashjakk.weather.app.tools.Success

class DatabaseOpenWeatherClient(
    private val databaseClient: DatabaseClient<WeatherEntity>
) : OpenWeatherClient {

    override suspend fun getWeatherData(
        latitude: Double,
        longitude: Double
    ): Result<OpenWeatherResponse> {
        val criteria = WeatherEntity(
            latitude = latitude,
            longitude = longitude
        )

        val entity = databaseClient.getMatchingBy(criteria)
            ?: return Failure("Not found in database")

        return Success(entity.toOpenWeatherResponse())
    }
}