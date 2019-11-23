package sashjakk.weather.app.ui

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.location.LocationManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel
import sashjakk.weather.app.R
import sashjakk.weather.app.tools.location
import sashjakk.weather.app.tools.toast

@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity() {

    private val viewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lifecycleScope.launch {
            val granted = requestPermissionAsync(ACCESS_FINE_LOCATION).await()

            if (!granted) {
                toast("No permission to access location")
                return@launch
            }

            handleLocationUpdates()
        }
    }

    private suspend fun handleLocationUpdates() {
        val locationManager = getSystemService<LocationManager>()
            ?: return

        locationManager.location()
            .map { viewModel.getWeatherData(it.latitude, it.longitude) }
            .catch { toast(it.message ?: "Location is not available") }
            .collect { bindWeatherData(it) }
    }

    private fun bindWeatherData(data: WeatherViewData) {
        cityName.text = data.city
        date.text = data.date
        windSpeed.text = "${data.windSpeed} m/s"
        humidity.text = "${data.humidity} %"
        degrees.text = "${data.degrees} C"

        Glide.with(this)
            .load(data.iconUrl)
            .into(weatherIcon)
    }
}