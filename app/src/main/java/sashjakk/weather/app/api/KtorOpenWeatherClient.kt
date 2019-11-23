package sashjakk.weather.app.api

import android.net.Uri
import io.ktor.client.HttpClient
import io.ktor.client.request.get

class KtorOpenWeatherClient(
    baseUrl: String,
    private val httpClient: HttpClient
) : OpenWeatherClient {

    private val urlBuilder = Uri.parse(baseUrl)

    override suspend fun getWeatherData(latitude: Double, longitude: Double): WeatherData {
        val url = urlBuilder.buildUpon()
            .appendPath("weather")
            .appendQueryParameter("lat", latitude.toString())
            .appendQueryParameter("lon", longitude.toString())
            .toString()

        return httpClient.get(url)
    }
}