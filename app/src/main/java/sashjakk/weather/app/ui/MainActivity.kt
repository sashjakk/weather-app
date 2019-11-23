package sashjakk.weather.app.ui

import android.Manifest
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel
import sashjakk.weather.app.R

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.weatherData
            .observe(this, ::setupViewData)

        lifecycleScope.launch {
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

