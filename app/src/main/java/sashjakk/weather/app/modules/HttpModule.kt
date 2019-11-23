package sashjakk.weather.app.modules

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.features.json.GsonSerializer
import io.ktor.client.features.json.JsonFeature
import okhttp3.Interceptor
import org.koin.dsl.module
import sashjakk.weather.app.BuildConfig

var httpModule = module {
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

private fun queryInjector(param: Pair<String, String>) = Interceptor {
    val (key, value) = param

    val injector = it.request().url()
        .newBuilder()
        .addQueryParameter(key, value)
        .build()

    val request = it.request()
        .newBuilder()
        .url(injector)
        .build()

    it.proceed(request)
}