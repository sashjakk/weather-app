package sashjakk.weather.app.modules

import org.koin.dsl.module
import sashjakk.weather.app.api.KtorOpenWeatherClient
import sashjakk.weather.app.api.OpenWeatherClient

val apiModule = module {
    single<OpenWeatherClient> {
        KtorOpenWeatherClient(getProperty("OPENAPI_BASE_URL"), get())
    }
}