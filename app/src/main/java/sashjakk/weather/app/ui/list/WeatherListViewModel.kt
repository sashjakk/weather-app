package sashjakk.weather.app.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import sashjakk.weather.app.db.DatabaseDao
import sashjakk.weather.app.db.WeatherEntity

class WeatherListViewModel(
    private val database: DatabaseDao<WeatherEntity>
) : ViewModel() {

    val locations = liveData {
        val items = database.getAll()
        emit(items)
    }
}