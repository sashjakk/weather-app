package sashjakk.weather.app.adapters

import sashjakk.weather.app.api.*
import sashjakk.weather.app.db.WeatherEntity
import sashjakk.weather.app.ui.common.WeatherViewData
import java.text.SimpleDateFormat
import java.util.*

fun WeatherEntity.toOpenWeatherResponse() =
    OWResponse(
        cityName = city,
        coordinates = Coordinates(latitude!!, longitude!!),
        main = Main(degrees, humidity),
        wind = Wind(windSpeed),
        weatherData = listOf(Weather(iconUrl)),
        date = date
    )

fun OWResponse.toWeatherEntity() =
    WeatherEntity(
        city = cityName,
        date = date,
        degrees = main.degrees,
        windSpeed = wind.speed,
        humidity = main.humidity,
        iconUrl = weatherData.firstOrNull()?.icon ?: "",
        latitude = coordinates.latitude,
        longitude = coordinates.longitude
    )

fun OWResponse.toWeatherViewData(): WeatherViewData {
    val dateFormatter = SimpleDateFormat(
        "EEEE, dd MMM yyyy",
        Locale.getDefault()
    )

    return WeatherViewData(
        city = cityName,
        date = dateFormatter.format(date * 1000L),
        degrees = main.degrees,
        windSpeed = wind.speed,
        humidity = main.humidity,
        iconUrl = weatherData.firstOrNull()?.icon ?: ""
    )
}