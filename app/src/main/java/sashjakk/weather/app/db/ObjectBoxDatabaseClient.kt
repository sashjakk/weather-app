package sashjakk.weather.app.db

import io.objectbox.BoxStore
import io.objectbox.kotlin.boxFor
import io.objectbox.kotlin.query

private const val COORDINATES_PRECISION = 0.01

class ObjectBoxDatabaseClient(
    boxStore: BoxStore
) : DatabaseClient<WeatherEntity> {
    private val box = boxStore.boxFor<WeatherEntity>()

    override suspend fun save(item: WeatherEntity): WeatherEntity {
        val id = box.put(item)
        return item.copy(id = id)
    }

    override suspend fun getMatchingBy(item: WeatherEntity): WeatherEntity? {
        val query = box.query {
            between(
                WeatherEntity_.latitude,
                item.latitude - COORDINATES_PRECISION,
                item.latitude + COORDINATES_PRECISION
            )

            between(
                WeatherEntity_.longitude,
                item.longitude - COORDINATES_PRECISION,
                item.longitude + COORDINATES_PRECISION
            )
        }

        return query.findFirst()
    }

    override suspend fun getAll(): List<WeatherEntity> {
        return box.all.toList()
    }
}