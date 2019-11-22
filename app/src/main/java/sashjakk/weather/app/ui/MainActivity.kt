package sashjakk.weather.app.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import sashjakk.weather.app.api.KtorOpenWeatherClient
import sashjakk.weather.app.api.OpenWeatherClient
import sashjakk.weather.app.R
import sashjakk.weather.app.modules.baseUrl
import sashjakk.weather.app.modules.httpClient
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity() {

    private val scope: CoroutineScope = object : CoroutineScope {
        override val coroutineContext: CoroutineContext = Job() + Dispatchers.Main
    }

    private val apiClient: OpenWeatherClient by lazy {
        KtorOpenWeatherClient(
            baseUrl,
            httpClient
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        scope.launch {
            val data = apiClient.getWeatherData("New York")
            Log.d("HEYO", data.toString())
        }
    }
}

