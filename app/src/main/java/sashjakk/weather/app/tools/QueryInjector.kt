package sashjakk.weather.app.tools

import okhttp3.Interceptor

fun queryInjector(param: Pair<String, String>) = Interceptor {
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