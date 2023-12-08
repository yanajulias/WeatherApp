package mini.test.weatherapp.ui

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import mini.test.weatherapp.data.WeatherResponse
import mini.test.weatherapp.repository.WeatherRepository
import mini.test.weatherapp.util.Resource
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository
) : ViewModel() {
    val list: MutableState<WeatherState> = mutableStateOf(WeatherState())

    fun getWeatherList(query: String) {
        viewModelScope.launch {
            list.value = WeatherState(isLoading = true)
            try {
                when (val result = weatherRepository.getJakartaWeather(query)) {
                    is Resource.Success -> {
                        result.data?.list?.let { weatherList ->
                            Log.d("WeatherData", "Received Weather List: $weatherList")
                            val formattedForecast = formatWeatherForecast(weatherList)
                            list.value =
                                WeatherState(weather = formattedForecast, data = weatherList)
                        }
                    }

                    is Resource.Failure -> {
                        Log.e("WeatherData", "API Failure: ${result.message}")
                        list.value = WeatherState(errorMessage = "Data not found.")
                    }
                }
            } catch (e: Exception) {
                Log.e("WeatherData", "Exception: ${e.message}")
                list.value = WeatherState(errorMessage = "Something went wrong.")
            }
        }
    }

    private fun formatWeatherForecast(weatherData: List<WeatherResponse.WeatherData>): String {
        val dateFormatter = SimpleDateFormat("EEE, dd MMM yyyy", Locale.getDefault())
        val dailyTemperatures = weatherData.groupBy { it.dtTxt?.substring(0, 10) }
        val formattedForecast = StringBuilder("Weather Forecast:\n")

        dailyTemperatures.entries.sortedBy { it.key }.forEach { (date, temperatures) ->
            val averageTemperature = temperatures.map { it.main?.temp ?: 0.0 }.average() / 10
            val parsedDate = date?.let {
                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(it)
            }
            val formattedDate = parsedDate?.let { dateFormatter.format(it) }

            val formattedTemperature = String.format("%.2f", averageTemperature)
            formattedForecast.append("$formattedDate: $formattedTemperature C\n")
        }

        return formattedForecast.toString()
    }


}