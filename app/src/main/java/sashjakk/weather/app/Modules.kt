package sashjakk.weather.app

import android.content.Context
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.features.json.GsonSerializer
import io.ktor.client.features.json.JsonFeature
import io.objectbox.android.AndroidObjectBrowser
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import sashjakk.weather.app.api.*
import sashjakk.weather.app.db.DatabaseClient
import sashjakk.weather.app.db.MyObjectBox
import sashjakk.weather.app.db.ObjectBoxDatabaseClient
import sashjakk.weather.app.db.WeatherEntity
import sashjakk.weather.app.tools.queryInjector
import sashjakk.weather.app.ui.MainViewModel

val uiModule = module {
    viewModel { MainViewModel(get()) }
}

val dbModule = module {
    single {
        val store = MyObjectBox.builder()
            .androidContext(get() as Context)
            .build()

        if (BuildConfig.DEBUG) {
            AndroidObjectBrowser(store).start(get() as Context)
        }

        store
    }

    single<DatabaseClient<WeatherEntity>> { ObjectBoxDatabaseClient(get()) }
}

val apiModule = module {
    single {
        CachedWeatherClient(
            get(),
            KtorOpenWeatherClient(
                getProperty("OPENAPI_BASE_URL"),
                getProperty("OPENAPI_ICON_URL"),
                get()
            )
        )
    }

    single { DatabaseOpenWeatherClient(get()) }

    single<OpenWeatherClient> {
        PipeWeatherClient(
            listOf(
                get<DatabaseOpenWeatherClient>(),
                get<CachedWeatherClient>()
            )
        )
    }
}

val httpModule = module {
    single {
        HttpClient(OkHttp) {
            install(JsonFeature) {
                serializer = GsonSerializer()
            }

            engine {
                addInterceptor(queryInjector("APPID" to BuildConfig.OPENWEATHER_API_KEY))
                addInterceptor(queryInjector("units" to "metric"))
            }
        }
    }
}

