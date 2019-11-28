package sashjakk.weather.app.api

import androidx.test.espresso.matcher.ViewMatchers.assertThat
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.features.json.GsonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.request.HttpRequestData
import io.ktor.client.request.HttpResponseData
import io.ktor.http.ContentType
import io.ktor.http.headersOf
import junit.framework.Assert.fail
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.*
import org.junit.Test
import sashjakk.weather.app.fixtures.coordinates
import sashjakk.weather.app.fixtures.notFound
import sashjakk.weather.app.fixtures.tawarano
import sashjakk.weather.app.tools.Failure
import sashjakk.weather.app.tools.Success

class KtorClientTest {

    @Test
    fun shouldFetchByCityName() = testWith { httpClient ->
        val instance = KtorClient(httpClient)
        val result = instance.getWeatherData("Tawarano")

        when (result) {
            is Failure -> fail("Wrong response type")
            is Success -> {
                val item = result.value

                assertThat(item.cityName, equalTo("Tawarano"))

                item.weatherData.forEach {
                    assertThat(it.icon, containsString("weather-app-icons"))
                    assertThat(it.icon, containsString(".svg"))
                }
            }
        }
    }

    @Test
    fun shouldFetchByCoordinates() = testWith { httpClient ->
        val instance = KtorClient(httpClient)
        val result = instance.getWeatherData(139.0, 35.0)

        when (result) {
            is Failure -> fail("Wrong response type")
            is Success -> {
                val item = result.value

                assertThat(item.cityName, equalTo("Shuzenji"))

                item.weatherData.forEach {
                    assertThat(it.icon, containsString("weather-app-icons"))
                    assertThat(it.icon, containsString(".svg"))
                }
            }
        }
    }

    @Test
    fun shouldFailWithInvalidCityName() = testWith { httpClient ->
        val instance = KtorClient(httpClient)
        val result = instance.getWeatherData("asdfg")

        when (result) {
            is Success -> fail("Wrong response type")
            is Failure -> {
                assertThat(result.error.message, notNullValue())
            }
        }
    }

    private fun testWith(test: suspend (HttpClient) -> Unit) {
        val mockResponseHandler: suspend (HttpRequestData) -> HttpResponseData = { request ->
            val params = request.url.parameters

            when {
                params["q"] == "Tawarano" -> respond(
                    content = tawarano,
                    headers = headersOf(
                        "Content-Type",
                        ContentType.Application.Json.toString()
                    )
                )

                params["lat"] == "139.0" && params["lon"] == "35.0" -> respond(
                    content = coordinates,
                    headers = headersOf(
                        "Content-Type",
                        ContentType.Application.Json.toString()
                    )
                )

                else -> respond(
                    content = notFound,
                    headers = headersOf(
                        "Content-Type",
                        ContentType.Application.Json.toString()
                    )
                )
            }
        }

        val httpClient = HttpClient(MockEngine) {
            install(JsonFeature) { serializer = GsonSerializer() }
            engine { addHandler(mockResponseHandler) }
        }

        runBlocking { test(httpClient) }
    }
}

