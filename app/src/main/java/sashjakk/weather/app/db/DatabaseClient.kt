package sashjakk.weather.app.db

interface DatabaseClient<T> {
    suspend fun save(item: T)
    suspend fun getMatchingBy(query: String): T?
    suspend fun getAll(): List<T>
}
