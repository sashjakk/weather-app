package sashjakk.weather.app

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.features.json.GsonSerializer
import io.ktor.client.features.json.JsonFeature
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import sashjakk.weather.app.api.DummyOpenWeatherClient
import sashjakk.weather.app.api.KtorOpenWeatherClient
import sashjakk.weather.app.api.OpenWeatherClient
import sashjakk.weather.app.api.PipeWeatherClient
import sashjakk.weather.app.tools.queryInjector
import sashjakk.weather.app.ui.MainViewModel

val uiModule = module {
    viewModel { MainViewModel(get()) }
}

val apiModule = module {
    single {
        KtorOpenWeatherClient(
            getProperty("OPENAPI_BASE_URL"),
            getProperty("OPENAPI_ICON_URL"),
            get()
        )
    }

    single { DummyOpenWeatherClient() }

    single<OpenWeatherClient> {
        PipeWeatherClient(
            listOf(get<DummyOpenWeatherClient>(), get<KtorOpenWeatherClient>())
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

