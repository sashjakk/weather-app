package sashjakk.weather.app

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.features.json.GsonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.request.get
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity() {

    private val scope: CoroutineScope = object : CoroutineScope {
        override val coroutineContext: CoroutineContext = Job() + Dispatchers.Main
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val client = HttpClient(OkHttp) {
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

        scope.launch {
            val data =
                client.get<WeatherResponse>("https://api.openweathermap.org/data/2.5/weather?q=Ventspils")
            Log.d("HEYO", data.toString())
        }

    }
}

