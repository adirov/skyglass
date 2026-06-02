package com.example.skyglass

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.skyglass.ui.theme.SkyglassTheme
import com.example.skyglass.viewmodel.WeatherViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SkyglassTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WeatherScreen()
                }
            }
        }
    }
}

@Composable
fun WeatherScreen(viewModel: WeatherViewModel = viewModel()) {
    val weatherData by viewModel.weatherData.collectAsState()
    val error by viewModel.error.collectAsState()
    var cityInput by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = cityInput,
            onValueChange = { cityInput = it },
            label = { Text("Введите город") },
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Button(
            onClick = { viewModel.fetchWeather(cityInput) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Узнать погоду")
        }

        Spacer(modifier = Modifier.height(32.dp))

        weatherData?.let { weather ->
            Text(text = weather.name, fontSize = 32.sp)
            Text(text = "${weather.main.temp.toInt()}°C", fontSize = 64.sp)
            Text(text = weather.weather.firstOrNull()?.description ?: "", fontSize = 20.sp)
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Влажность")
                    Text("${weather.main.humidity}%")
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Давление")
                    Text("${weather.main.pressure} hPa")
                }
            }
        }

        error?.let {
            Text(text = "Ошибка: $it", color = MaterialTheme.colorScheme.error)
        }
    }
}
