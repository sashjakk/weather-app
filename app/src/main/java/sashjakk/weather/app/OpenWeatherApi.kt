package sashjakk.weather.app

import android.util.Log
import com.google.gson.annotations.SerializedName
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.features.json.GsonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.request.get
import okhttp3.Interceptor

data class WeatherResponse(
    val name: String,
    @SerializedName("main") val temperatureInfo: TemperatureResponse
)

data class TemperatureResponse(
    @SerializedName("temp") val temperature: Int
)

interface OpenWeatherClient {
    suspend fun getWeatherData(city: String): WeatherResponse
}

private val client = HttpClient(OkHttp) {
    install(JsonFeature) {
        serializer = GsonSerializer()
    }

    engine {
        addInterceptor(Interceptor {
            val apiKeyInjector = it
                .request()
                .url()
                .newBuilder()
                .addQueryParameter("APPID", BuildConfig.OPENWEATHER_API_KEY)
                .addQueryParameter("units", "metric")
                .build()

            val request = it
                .request()
                .newBuilder()
                .url(apiKeyInjector)
                .build()

            Log.d("HEYO", apiKeyInjector.toString())

            return@Interceptor it.proceed(request)
        })
    }
}

class KtorOpenWeatherClient(
    private val baseUrl: String,
    private val httpClient: HttpClient
) : OpenWeatherClient {

    override suspend fun getWeatherData(city: String): WeatherResponse {
        return client
            .get("https://api.openweathermap.org/data/2.5/weather?q=$city")
    }
}