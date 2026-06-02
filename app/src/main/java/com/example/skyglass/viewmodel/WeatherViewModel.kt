package com.example.skyglass.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skyglass.data.model.WeatherResponse
import com.example.skyglass.data.network.WeatherApi
import io.ktor.client.call.body
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {
    private val _weatherData = MutableStateFlow<WeatherResponse?>(null)
    val weatherData = _weatherData.asStateFlow()

    fun fetchWeather(city: String) {
        viewModelScope.launch {
            try {
                val response = WeatherApi.getCurrentWeather(city)
                _weatherData.value = response.body<WeatherResponse>()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
