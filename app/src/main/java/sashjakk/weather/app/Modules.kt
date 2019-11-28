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
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import sashjakk.weather.app.api.KtorClient
import sashjakk.weather.app.api.OWClient
import sashjakk.weather.app.api.PersistentClient
import sashjakk.weather.app.connectivity.ConnectivityProvider
import sashjakk.weather.app.connectivity.DefaultConnectivityProvider
import sashjakk.weather.app.db.DatabaseDao
import sashjakk.weather.app.db.MyObjectBox
import sashjakk.weather.app.db.ObjectBoxDao
import sashjakk.weather.app.db.WeatherEntity
import sashjakk.weather.app.location.DefaultLocationProvider
import sashjakk.weather.app.location.LocationProvider
import sashjakk.weather.app.tools.queryInjector
import sashjakk.weather.app.ui.details.WeatherDetailsViewModel
import sashjakk.weather.app.ui.list.WeatherListViewModel

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
        MyObjectBox.builder()
            .androidContext(get() as Context)
            .build()
    }

    single<DatabaseDao<WeatherEntity>> { ObjectBoxDao(get()) }
}

val apiModule = module {
    single<OWClient> {
        PersistentClient(
            get(),
            get(),
            KtorClient(get())
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
