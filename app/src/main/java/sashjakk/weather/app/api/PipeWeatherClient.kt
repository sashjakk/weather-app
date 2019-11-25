package sashjakk.weather.app.api

import sashjakk.weather.app.tools.Failure
import sashjakk.weather.app.tools.Result
import sashjakk.weather.app.tools.Success

class PipeWeatherClient(
    private val clients: List<OpenWeatherClient>
) : OpenWeatherClient {

    override suspend fun getWeatherData(
        latitude: Double,
        longitude: Double
    ): Result<OpenWeatherResponse> {
        var result: Result<OpenWeatherResponse> = Failure("Unable to obtain weather data")

        for (client in clients) {
            result = client.getWeatherData(latitude, longitude)
            if (result is Success) {
                return result
            }
        }

        return result
    }

    override fun getIconUrl(icon: String): String {
        return ""
    }
}