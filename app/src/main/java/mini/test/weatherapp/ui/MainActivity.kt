package mini.test.weatherapp.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint
import mini.test.weatherapp.ui.theme.WeatherAppTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherAppTheme {
                WeatherHome()
            }
        }
    }
}

@Composable
fun WeatherHome(
    modifier: Modifier = Modifier,
    weatherViewModel: WeatherViewModel = hiltViewModel()
) {
    val jakartaWeather: MutableState<String> = remember { mutableStateOf("Jakarta, ID") }

    DisposableEffect(jakartaWeather.value) {
        weatherViewModel.getWeatherList(jakartaWeather.value)
        onDispose { }
    }

    Surface(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(
                text = "Jakarta's Weather",
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(12.dp),
                fontSize = 28.sp,
                fontFamily = FontFamily.Monospace
            )

            if (weatherViewModel.list.value.isLoading) {
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }

            if (weatherViewModel.list.value.errorMessage.isNotBlank()) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = weatherViewModel.list.value.errorMessage,
                        modifier = Modifier.padding(12.dp),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Center
                    )
                }
            }

            if (weatherViewModel.list.value.data.isNotEmpty()) {
                val formattedForecast = weatherViewModel.list.value.weather

                LazyColumn {
                    items(listOf(formattedForecast)) { forecast ->
                        val modifiedForecast = forecast.replace("C", "\u2103")
                        Text(
                            text = modifiedForecast,
                            modifier = Modifier.padding(8.dp),
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WeatherAppTheme {
        WeatherHome()
    }
}