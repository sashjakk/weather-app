package sashjakk.weather.app.db

import sashjakk.weather.app.api.OpenWeatherResponse

class InMemoryDatabaseClient(
    private val cache: MutableList<OpenWeatherResponse> = mutableListOf()
) : DatabaseClient<OpenWeatherResponse> {
    override suspend fun save(item: OpenWeatherResponse) {
        getMatchingBy(item.cityName) ?: cache.add(item)
    }

    override suspend fun getMatchingBy(query: String): OpenWeatherResponse? {
        return cache.find { it.cityName == query }
    }

    override suspend fun getAll(): List<OpenWeatherResponse> {
        return cache
    }
}