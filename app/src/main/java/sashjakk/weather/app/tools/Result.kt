package sashjakk.weather.app.tools

sealed class Result<T>

class Success<T>(val value: T) : Result<T>()

class Failure<T>(val error: Throwable) : Result<T>() {
    constructor(message: String) : this(Error(message))
}