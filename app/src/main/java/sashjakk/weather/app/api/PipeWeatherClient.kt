package sashjakk.weather.app.api

class PipeWeatherClient(
    private val clients: List<OpenWeatherClient>
) : OpenWeatherClient {

    override suspend fun getWeatherData(
        latitude: Double,
        longitude: Double
    ): Result<OpenWeatherResponse> {
        var result = Result
            .failure<OpenWeatherResponse>("Unable to obtain weather data")

        for (client in clients) {
            result = client.getWeatherData(latitude, longitude)
            if (result.isSuccess) {
                return result
            }
        }

        return result
    }

    override fun getIconUrl(icon: String): String {
        return ""
    }
}