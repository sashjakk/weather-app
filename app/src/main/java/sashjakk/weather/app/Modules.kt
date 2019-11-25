package sashjakk.weather.app

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.features.json.GsonSerializer
import io.ktor.client.features.json.JsonFeature
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import sashjakk.weather.app.api.*
import sashjakk.weather.app.db.DatabaseClient
import sashjakk.weather.app.db.InMemoryDatabaseClient
import sashjakk.weather.app.tools.queryInjector
import sashjakk.weather.app.ui.MainViewModel

val uiModule = module {
    viewModel { MainViewModel(get()) }
}

val dbModule = module {
    single<DatabaseClient<OpenWeatherResponse>> { InMemoryDatabaseClient() }
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

