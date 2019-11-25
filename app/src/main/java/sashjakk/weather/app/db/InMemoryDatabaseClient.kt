package sashjakk.weather.app.db

class InMemoryDatabaseClient(
    private val cache: MutableList<WeatherEntity> = mutableListOf()
) : DatabaseClient<WeatherEntity> {

    override suspend fun save(item: WeatherEntity): WeatherEntity =
        getMatchingBy(item) ?: run {
            cache.add(item)
            item
        }

    override suspend fun getMatchingBy(item: WeatherEntity): WeatherEntity? =
        cache.find { it.city == item.city }


    override suspend fun getAll(): List<WeatherEntity> = cache.toList()
}