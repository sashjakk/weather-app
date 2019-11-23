package sashjakk.weather.app.api

import android.net.Uri
import io.ktor.client.HttpClient
import io.ktor.client.request.get

class KtorOpenWeatherClient(
    baseUrl: String,
    private val iconUrl: String,
    private val httpClient: HttpClient
) : OpenWeatherClient {

    private val urlBuilder = Uri.parse(baseUrl)

    override suspend fun getWeatherData(latitude: Double, longitude: Double): OpenWeatherResponse {
        val url = urlBuilder.buildUpon()
            .appendPath("weather")
            .appendQueryParameter("lat", latitude.toString())
            .appendQueryParameter("lon", longitude.toString())
            .toString()

        return httpClient.get(url)
    }

    override fun getIconUrl(icon: String): String {
        return Uri.parse(iconUrl)
            .buildUpon()
            .appendPath("img")
            .appendPath("wn")
            .appendPath("$icon@2x.png")
            .toString()
    }
}