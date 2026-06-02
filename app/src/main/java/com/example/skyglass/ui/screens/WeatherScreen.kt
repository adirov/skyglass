package com.example.skyglass.ui.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.skyglass.viewmodel.WeatherViewModel

@Composable
fun WeatherScreen(navController: NavController, viewModel: WeatherViewModel = viewModel()) {
    val weatherData by viewModel.weatherData.collectAsState()
    val favoriteCities by viewModel.favoriteCities.collectAsState()
    val error by viewModel.error.collectAsState()
    var cityInput by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Skyglass", style = MaterialTheme.typography.headlineSmall)
            TextButton(onClick = { navController.navigate("login") }) {
                Text("Выйти")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

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

        Spacer(modifier = Modifier.height(16.dp))

        weatherData?.let { weather ->
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = weather.name, fontSize = 24.sp)
                    Text(text = "${weather.main.temp.toInt()}°C", fontSize = 48.sp)
                    Text(text = weather.weather.firstOrNull()?.description ?: "", fontSize = 16.sp)
                    
                    Button(onClick = { viewModel.addToFavorites(weather.name) }) {
                        Text("⭐ В избранное")
                    }
                }
            }
        }

        error?.let {
            Text(text = it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(8.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("Избранные города:", style = MaterialTheme.typography.titleMedium)
        
        LazyColumn {
            items(favoriteCities) { city ->
                ListItem(
                    headlineContent = { Text(city.city_name) },
                    trailingContent = {
                        IconButton(onClick = { city.id?.let { viewModel.deleteFromFavorites(it) } }) {
                            Icon(Icons.Default.Delete, contentDescription = "Удалить")
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
