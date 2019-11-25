package sashjakk.weather.app.api

import sashjakk.weather.app.db.DatabaseClient

class CachedWeatherClient(
    private val DatabaseClient: DatabaseClient<OpenWeatherResponse>,
    private val delegate: OpenWeatherClient
) : OpenWeatherClient {

    override suspend fun getWeatherData(
        latitude: Double,
        longitude: Double
    ): Result<OpenWeatherResponse> {
        val result = delegate.getWeatherData(latitude, longitude)

        if (result.isSuccess) {
            DatabaseClient.save(result.result)
        }

        return result
    }

    override fun getIconUrl(icon: String): String {
        return delegate.getIconUrl(icon)
    }
}