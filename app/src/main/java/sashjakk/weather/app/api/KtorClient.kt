package sashjakk.weather.app.api

import android.net.Uri
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import sashjakk.weather.app.tools.Failure
import sashjakk.weather.app.tools.Result
import sashjakk.weather.app.tools.Success

private const val openApiBaseUrl =
    "https://api.openweathermap.org/data/2.5/weather"

private const val iconUrlBase =
    "https://raw.githubusercontent.com/sashjakk/weather-app-icons/master/icons"

class KtorClient(
    private val httpClient: HttpClient
) : OWClient {

    private val urlBuilder = Uri.parse(openApiBaseUrl)

    override suspend fun getWeatherData(
        cityName: String
    ): Result<OWResponse> {
        val url = urlBuilder.buildUpon()
            .appendQueryParameter("q", cityName)
            .toString()

        return requestData(url)
    }

    override suspend fun getWeatherData(
        latitude: Double,
        longitude: Double
    ): Result<OWResponse> {
        val url = urlBuilder.buildUpon()
            .appendQueryParameter("lat", latitude.toString())
            .appendQueryParameter("lon", longitude.toString())
            .toString()

        return requestData(url)
    }

    private suspend fun requestData(url: String): Result<OWResponse> {
        return try {
            val response = httpClient.get<OWResponse>(url)
            val withIconUrls = response.weatherData
                .map(::injectIconUrl)

            Success(response.copy(weatherData = withIconUrls))
        } catch (e: Throwable) {
            Failure(e)
        }
    }

    private fun injectIconUrl(weather: Weather): Weather {
        return weather.copy(icon = "$iconUrlBase/${weather.icon}.svg")
    }
}