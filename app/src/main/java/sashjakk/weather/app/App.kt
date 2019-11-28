package sashjakk.weather.app

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)

            modules(
                listOf(httpModule, dbModule, apiModule, locationModule, uiModule)
            )

            properties(
                mapOf(
                    "OPENAPI_BASE_URL" to "https://api.openweathermap.org/data/2.5"
                )
            )
        }
    }
}