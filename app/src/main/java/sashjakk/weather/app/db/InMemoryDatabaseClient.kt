package sashjakk.weather.app.db

import sashjakk.weather.app.api.OpenWeatherResponse

class InMemoryDatabaseClient(
    private val cache: MutableList<OpenWeatherResponse> = mutableListOf()
) : DatabaseClient<OpenWeatherResponse> {

    override suspend fun save(item: OpenWeatherResponse): OpenWeatherResponse =
        getMatchingBy(item.cityName) ?: run {
            cache.add(item)
            item
        }

    override suspend fun getMatchingBy(query: String): OpenWeatherResponse? =
        cache.find { it.cityName == query }


    override suspend fun getAll(): List<OpenWeatherResponse> = cache.toList()
}