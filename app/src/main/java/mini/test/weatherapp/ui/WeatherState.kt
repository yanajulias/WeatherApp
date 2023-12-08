package mini.test.weatherapp.ui

import mini.test.weatherapp.data.WeatherResponse

data class WeatherState(
    val weather: String ="",
    val data: List<WeatherResponse.WeatherData> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String = ""
)