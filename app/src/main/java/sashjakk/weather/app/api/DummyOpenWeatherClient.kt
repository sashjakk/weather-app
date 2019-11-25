package sashjakk.weather.app.api

class DummyOpenWeatherClient : OpenWeatherClient {
    override suspend fun getWeatherData(
        latitude: Double,
        longitude: Double
    ): Result<OpenWeatherResponse> {
        return Result.failure("failed")
    }

    override fun getIconUrl(icon: String): String {
        return ""
    }
}