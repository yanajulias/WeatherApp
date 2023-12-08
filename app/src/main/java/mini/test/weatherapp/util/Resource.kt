package mini.test.weatherapp.util

sealed class Resource<out T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T) : Resource<T>(data)
    class Failure<T>(data: T? = null, message: String) : Resource<T>(data, message)
}