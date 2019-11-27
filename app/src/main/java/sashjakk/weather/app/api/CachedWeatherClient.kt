package sashjakk.weather.app.api

import sashjakk.weather.app.db.DatabaseClient
import sashjakk.weather.app.db.WeatherEntity
import sashjakk.weather.app.db.toWeatherEntity
import sashjakk.weather.app.tools.Result
import sashjakk.weather.app.tools.Success

class CachedWeatherClient(
    private val database: DatabaseClient<WeatherEntity>,
    private val delegate: OpenWeatherClient
) : OpenWeatherClient {

    override suspend fun getWeatherData(
        latitude: Double,
        longitude: Double
    ): Result<OpenWeatherResponse> {
        val result = delegate.getWeatherData(latitude, longitude)

        if (result is Success) {
            database.save(result.value.toWeatherEntity())
        }

        return result
    }
}