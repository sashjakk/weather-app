package sashjakk.weather.app.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import sashjakk.weather.app.R
import sashjakk.weather.app.api.KtorOpenWeatherClient
import sashjakk.weather.app.api.OpenWeatherClient
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

    private val viewModel = MainViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        scope.launch {
            val data = apiClient.getWeatherData("New York")
            Log.d("HEYO", data.toString())
        }

        viewModel.weatherItem
            .onEach { setupViewData(it) }
            .launchIn(scope)
    }

    private fun setupViewData(item: WeatherViewData) {
        cityName.text = item.city
        date.text = item.date
        windSpeed.text = "${item.windSpeed} m/s"
        humidity.text = "${item.humidity} %"
        degrees.text = "${item.degrees} C"
    }
}
