package sashjakk.weather.app.api

import sashjakk.weather.app.adapters.toOpenWeatherResponse
import sashjakk.weather.app.connectivity.ConnectivityProvider
import sashjakk.weather.app.db.DatabaseDao
import sashjakk.weather.app.db.WeatherEntity
import sashjakk.weather.app.adapters.toWeatherEntity
import sashjakk.weather.app.tools.Failure
import sashjakk.weather.app.tools.Result
import sashjakk.weather.app.tools.Success

class PersistentClient(
    private val connectivity: ConnectivityProvider,
    private val database: DatabaseDao<WeatherEntity>,
    private val client: OWClient
) : OWClient {

    override suspend fun getWeatherData(cityName: String): Result<OWResponse> {
        if (connectivity.isConnected) {
            val result = client.getWeatherData(cityName)

            if (result is Success) {
                database.save(result.value.toWeatherEntity())
                return result
            }
        }

        val criteria = WeatherEntity(
            city = cityName
        )

        val result = database.getMatchingBy(criteria)
            ?: return Failure("Not found in database")

        return Success(result.toOpenWeatherResponse())
    }

    override suspend fun getWeatherData(
        latitude: Double,
        longitude: Double
    ): Result<OWResponse> {
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