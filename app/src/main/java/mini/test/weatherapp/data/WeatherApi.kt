package mini.test.weatherapp.data

import mini.test.weatherapp.util.Constants
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("forecast")
    suspend fun getWeatherList(
        @Query("q") query: String,
        @Query("appid") appid: String = Constants.API_KEY
    ): WeatherResponse
}