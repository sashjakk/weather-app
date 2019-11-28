package sashjakk.weather.app.api

import android.net.Uri
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import sashjakk.weather.app.tools.Failure
import sashjakk.weather.app.tools.Result
import sashjakk.weather.app.tools.Success

class KtorOpenWeatherClient(
    baseUrl: String,
    private val httpClient: HttpClient
) : OpenWeatherClient {

    private val urlBuilder = Uri.parse(baseUrl)

    override suspend fun getWeatherData(cityName: String): Result<OpenWeatherResponse> {
        val url = urlBuilder.buildUpon()
            .appendPath("weather")
            .appendQueryParameter("q", cityName)
            .toString()

        return requestData(url)
    }

    override suspend fun getWeatherData(
        latitude: Double,
        longitude: Double
    ): Result<OpenWeatherResponse> {
        val url = urlBuilder.buildUpon()
            .appendPath("weather")
            .appendQueryParameter("lat", latitude.toString())
            .appendQueryParameter("lon", longitude.toString())
            .toString()

        return requestData(url)
    }

    private suspend fun requestData(url: String): Result<OpenWeatherResponse> {
        return try {
            val response = httpClient.get<OpenWeatherResponse>(url)
            val withIconUrls = response.weatherData
                .map(::injectIconUrl)

            Success(response.copy(weatherData = withIconUrls))
        } catch (e: Throwable) {
            Failure(e)
        }
    }

    private fun injectIconUrl(weatherData: WeatherData): WeatherData {
        val url = "https://raw.githubusercontent.com/sashjakk/weather-app-icons/master/icons/${weatherData.icon}.svg"
        return weatherData.copy(icon = url)
    }
}