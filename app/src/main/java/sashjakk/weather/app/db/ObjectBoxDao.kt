package sashjakk.weather.app.db

import io.objectbox.BoxStore
import io.objectbox.kotlin.boxFor
import io.objectbox.kotlin.query

private const val COORDINATES_PRECISION = 0.01

class ObjectBoxDao(
    boxStore: BoxStore
) : DatabaseDao<WeatherEntity> {
    private val box = boxStore.boxFor<WeatherEntity>()

    override suspend fun save(item: WeatherEntity): WeatherEntity {
        val target = getMatchingBy(item)
            ?.apply {
                date = item.date
                degrees = item.degrees
                humidity = item.humidity
                iconUrl = item.iconUrl
                windSpeed = item.windSpeed
            }
            ?: item

        val id = box.put(target)
        return target.copy(id = id)
    }

    override suspend fun getMatchingBy(item: WeatherEntity): WeatherEntity? {
        val query = box.query {
            if (item.city.isNotEmpty()) {
                equal(WeatherEntity_.city, item.city)
            }

            if (item.latitude != 0.0) {
                between(
                    WeatherEntity_.latitude,
                    item.latitude - COORDINATES_PRECISION,
                    item.latitude + COORDINATES_PRECISION
                )
            }

            if (item.longitude != 0.0) {
                between(
                    WeatherEntity_.longitude,
                    item.longitude - COORDINATES_PRECISION,
                    item.longitude + COORDINATES_PRECISION
                )
            }
        }

        return query.findFirst()
    }

    override suspend fun getAll(): List<WeatherEntity> {
        return box.all.toList()
    }
}