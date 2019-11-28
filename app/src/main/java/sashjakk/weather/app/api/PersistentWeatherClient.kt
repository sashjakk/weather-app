package sashjakk.weather.app.api

import sashjakk.weather.app.connectivity.ConnectivityProvider
import sashjakk.weather.app.db.DatabaseClient
import sashjakk.weather.app.db.WeatherEntity
import sashjakk.weather.app.db.toWeatherEntity
import sashjakk.weather.app.tools.Failure
import sashjakk.weather.app.tools.Result
import sashjakk.weather.app.tools.Success

class PersistentWeatherClient(
    private val connectivity: ConnectivityProvider,
    private val database: DatabaseClient<WeatherEntity>,
    private val client: OpenWeatherClient
) : OpenWeatherClient {

    override suspend fun getWeatherData(
        latitude: Double,
        longitude: Double
    ): Result<OpenWeatherResponse> {
        if (connectivity.isConnected) {
            val result = client.getWeatherData(latitude, longitude)

            if (result is Success) {
                database.save(result.value.toWeatherEntity())
                return result
            }
        }

        val criteria = WeatherEntity(
            latitude = latitude,
            longitude = longitude
        )

        val result = database.getMatchingBy(criteria)
            ?: return Failure("Not found in database")

        return Success(result.toOpenWeatherResponse())
    }
}