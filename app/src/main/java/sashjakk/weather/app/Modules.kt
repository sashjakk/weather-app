package sashjakk.weather.app

import android.content.Context
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.features.json.GsonSerializer
import io.ktor.client.features.json.JsonFeature
import io.objectbox.android.AndroidObjectBrowser
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import sashjakk.weather.app.api.*
import sashjakk.weather.app.connectivity.ConnectivityProvider
import sashjakk.weather.app.connectivity.DefaultConnectivityProvider
import sashjakk.weather.app.db.DatabaseClient
import sashjakk.weather.app.db.MyObjectBox
import sashjakk.weather.app.db.ObjectBoxDatabaseClient
import sashjakk.weather.app.db.WeatherEntity
import sashjakk.weather.app.location.DefaultLocationProvider
import sashjakk.weather.app.location.LocationProvider
import sashjakk.weather.app.tools.queryInjector
import sashjakk.weather.app.ui.WeatherDetailsViewModel
import sashjakk.weather.app.ui.WeatherListViewModel

val uiModule = module {
    viewModel { WeatherDetailsViewModel(get(), get()) }
    viewModel { WeatherListViewModel(get()) }
}

val connectivityModule = module {
    single<LocationProvider> { DefaultLocationProvider(get()) }
    single<ConnectivityProvider> { DefaultConnectivityProvider(get()) }
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
    single<OpenWeatherClient> {
        PersistentWeatherClient(
            get(),
            get(),
            KtorOpenWeatherClient(
                getProperty("OPENAPI_BASE_URL"),
                get()
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

@GlideModule
class GlideModule : AppGlideModule() {
    override fun applyOptions(context: Context, builder: GlideBuilder) {
        super.applyOptions(context, builder)

        val options = RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.DATA)

        builder.setDefaultRequestOptions(options)
    }
}
