package sashjakk.weather.app.db

import androidx.test.espresso.matcher.ViewMatchers.assertThat
import io.objectbox.BoxStore
import io.objectbox.kotlin.boxFor
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.*
import org.junit.Test
import java.io.File

private val testDir = File("/data/data/sashjakk.weather.app/object-box/test-db")

class ObjectBoxDaoTest {

    @Test
    fun shouldSave() = testWith { boxStore ->
        val instance = ObjectBoxDao(boxStore)
        val item = WeatherEntity(
            city = "test city",
            date = System.currentTimeMillis()
        )
        val result = instance.save(item)

        assertThat(result.id, not(equalTo(0L)))
        assertThat(instance.getAll().size, equalTo(11))
    }

    @Test
    fun shouldUpdate() = testWith { boxStore ->
        val instance = ObjectBoxDao(boxStore)
        val item = WeatherEntity(
            city = "test city",
            degrees = 0f
        )

        instance.save(item)
        assertThat(instance.getAll().size, equalTo(11))

        val item2 = item.copy(degrees = 15f)
        val result = instance.save(item2)

        assertThat(instance.getAll().size, equalTo(11))
        assertThat(result.degrees, equalTo(item2.degrees))
    }

    @Test
    fun shouldMatchByName() = testWith { boxStore ->
        val instance = ObjectBoxDao(boxStore)
        val item = WeatherEntity(city = "city1")

        val result = instance.getMatchingBy(item)

        assertThat(result, notNullValue())
    }

    @Test
    fun shouldFailToMatchByName() = testWith { boxStore ->
        val instance = ObjectBoxDao(boxStore)
        val item = WeatherEntity(city = "random")

        val result = instance.getMatchingBy(item)

        assertThat(result, nullValue())
    }

    @Test
    fun shouldReturnFirstItemIfEmptyName() = testWith { boxStore ->
        val instance = ObjectBoxDao(boxStore)
        val item = WeatherEntity(city = "")

        val result = instance.getMatchingBy(item)

        assertThat(result, notNullValue())
        assertThat(result?.city, equalTo("city0"))
    }

    @Test
    fun shouldMatchByLatitude() = testWith { boxStore ->
        val instance = ObjectBoxDao(boxStore)
        val item = WeatherEntity(latitude = 1.0)
        val result = instance.getMatchingBy(item)

        assertThat(result, notNullValue())
        assertThat(result?.latitude, equalTo(1.0))
        assertThat(result?.longitude, equalTo(1.0))
        assertThat(result?.city, equalTo("city1"))
    }

    @Test
    fun shouldMatchByLongitude() = testWith { boxStore ->
        val instance = ObjectBoxDao(boxStore)
        val item = WeatherEntity(longitude = 1.0)
        val result = instance.getMatchingBy(item)

        assertThat(result, notNullValue())
        assertThat(result?.latitude, equalTo(1.0))
        assertThat(result?.longitude, equalTo(1.0))
        assertThat(result?.city, equalTo("city1"))
    }

    @Test
    fun shouldFailToMatchByLongitude() = testWith { boxStore ->
        val instance = ObjectBoxDao(boxStore)
        val item = WeatherEntity(longitude = 15.0)
        val result = instance.getMatchingBy(item)

        assertThat(result, nullValue())
    }

    @Test
    fun shouldReturnAll() = testWith { boxStore ->
        val instance = ObjectBoxDao(boxStore)
        val result = instance.getAll()

        assertThat(result.size, equalTo(10))
    }

    private fun testWith(test: suspend (BoxStore) -> Unit) {
        BoxStore.deleteAllFiles(testDir)

        val store = MyObjectBox
            .builder()
            .directory(testDir)
            .build()

        val item = WeatherEntity(
            city = "city",
            date = System.currentTimeMillis(),
            windSpeed = 0f,
            degrees = 0f,
            humidity = 0f,
            iconUrl = "url",
            latitude = 0.0,
            longitude = 0.0
        )

        val items = Array(10) {
            item.copy(
                city = item.city + it,
                longitude = it.toDouble(),
                latitude = it.toDouble()
            )
        }

        store.boxFor<WeatherEntity>().put(*items)

        runBlocking { test(store) }

        store.close()
    }
}