package mini.test.weatherapp.repository

import mini.test.weatherapp.data.WeatherApi
import mini.test.weatherapp.data.WeatherResponse
import mini.test.weatherapp.util.Resource
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val weatherApi: WeatherApi
) {
    suspend fun getJakartaWeather(query: String): Resource<WeatherResponse> {
        return try {
            val result = weatherApi.getWeatherList(query = query)
            Resource.Success(data = result)
        } catch (e: Exception) {
            Resource.Failure(message = e.message.toString())
        }
    }
}