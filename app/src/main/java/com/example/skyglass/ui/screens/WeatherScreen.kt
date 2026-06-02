package com.example.skyglass.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
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
    val inAppMessage by viewModel.inAppMessage.collectAsState()
    
    var cityInput by remember { mutableStateOf("") }
    var editingCityId by remember { mutableStateOf<Int?>(null) }
    var editingCityName by remember { mutableStateOf("") }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color(0xFF0B1326))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "SkyGlass",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                
                Row {
                    IconButton(onClick = { 
                        viewModel.showInstantNotification("🔔 Notification system active!") 
                    }) {
                        Icon(Icons.Default.Notifications, contentDescription = null, tint = Color(0xFF60A5FA))
                    }
                    TextButton(onClick = { navController.navigate("login") }) {
                        Text("Logout", color = Color.White.copy(alpha = 0.6f))
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Search Bar
            OutlinedTextField(
                value = cityInput,
                onValueChange = { cityInput = it },
                label = { Text("Search City", color = Color.White.copy(alpha = 0.6f)) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color(0xFF60A5FA)) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF60A5FA),
                    unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                singleLine = true
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Button(
                onClick = { viewModel.fetchWeather(cityInput) },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF60A5FA))
            ) {
                Text("Get Weather", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Weather Display
            weatherData?.let { weather ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.05f))
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = weather.name, fontSize = 28.sp, color = Color.White, fontWeight = FontWeight.Bold)
                        Text(text = "${weather.main.temp.toInt()}°", fontSize = 72.sp, color = Color.White, fontWeight = FontWeight.Thin)
                        Text(text = weather.weather.firstOrNull()?.description?.uppercase() ?: "", fontSize = 14.sp, color = Color(0xFF60A5FA), letterSpacing = 2.sp)
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Button(
                            onClick = { viewModel.addToFavorites(weather.name) },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.1f))
                        ) {
                            Text("⭐ Add to Favorites", color = Color.White)
                        }
                    }
                }
            }

            if (error != null) {
                Text(text = error!!, color = Color.Red.copy(alpha = 0.7f), modifier = Modifier.padding(8.dp))
            }

            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                "Favorites",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White,
                modifier = Modifier.align(Alignment.Start)
            )
            
            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(favoriteCities) { city ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.03f))
                    ) {
                        ListItem(
                            colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                            headlineContent = {
                                if (editingCityId == city.id) {
                                    TextField(
                                        value = editingCityName,
                                        onValueChange = { editingCityName = it },
                                        colors = TextFieldDefaults.colors(focusedTextColor = Color.White)
                                    )
                                } else {
                                    Text(city.city_name, color = Color.White)
                                }
                            },
                            trailingContent = {
                                Row {
                                    if (editingCityId == city.id) {
                                        IconButton(onClick = {
                                            city.id?.let { viewModel.updateFavorite(it, editingCityName) }
                                            editingCityId = null
                                        }) {
                                            Text("OK", color = Color(0xFF60A5FA))
                                        }
                                    } else {
                                        IconButton(onClick = {
                                            editingCityId = city.id
                                            editingCityName = city.city_name
                                        }) {
                                            Icon(Icons.Default.Edit, contentDescription = null, tint = Color.White.copy(alpha = 0.4f))
                                        }
                                    }
                                    IconButton(onClick = { city.id?.let { viewModel.deleteFromFavorites(it) } }) {
                                        Icon(Icons.Default.Delete, contentDescription = null, tint = Color.Red.copy(alpha = 0.4f))
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }

        // Animated In-App Notification
        AnimatedVisibility(
            visible = inAppMessage != null,
            enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
            modifier = Modifier.align(Alignment.TopCenter).padding(top = 40.dp)
        ) {
            inAppMessage?.let { message ->
                Card(
                    modifier = Modifier.fillMaxWidth(0.9f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF60A5FA)),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    Text(
                        text = message,
                        modifier = Modifier.padding(16.dp),
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
