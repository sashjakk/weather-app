package sashjakk.weather.app.ui

import android.Manifest
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.viewmodel.ext.android.viewModel
import sashjakk.weather.app.R
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity() {

    private val scope: CoroutineScope = object : CoroutineScope {
        override val coroutineContext: CoroutineContext = Job() + Dispatchers.Main
    }

    private val viewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.weatherData
            .onEach { setupViewData(it) }
            .launchIn(scope)

        scope.launch {
            delay(5000)

            val granted = requestPermissionAsync(Manifest.permission.ACCESS_FINE_LOCATION)
                .await()

            Log.d("HEYO", "yay $granted")
        }
    }

    private fun setupViewData(item: WeatherViewData) {
        cityName.text = item.city
        date.text = item.date
        windSpeed.text = "${item.windSpeed} m/s"
        humidity.text = "${item.humidity} %"
        degrees.text = "${item.degrees} C"

        Glide.with(this)
            .load("https://openweathermap.org/img/wn/10d@2x.png")
            .into(weatherIcon)
    }
}

