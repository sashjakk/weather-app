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

            item.latitude?.let {
                between(
                    WeatherEntity_.latitude,
                    it - COORDINATES_PRECISION,
                    it + COORDINATES_PRECISION
                )
            }

            item.longitude?.let {
                between(
                    WeatherEntity_.longitude,
                    it - COORDINATES_PRECISION,
                    it + COORDINATES_PRECISION
                )
            }
        }

        return query.findFirst()
    }

    override suspend fun getAll(): List<WeatherEntity> {
        return box.all.toList()
    }
}