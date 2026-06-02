package com.example.skyglass.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.skyglass.data.model.FavoriteCity
import com.example.skyglass.data.model.WeatherResponse
import com.example.skyglass.data.repository.WeatherRepository
import com.example.skyglass.utils.NotificationHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WeatherViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = WeatherRepository()
    private val notificationHelper = NotificationHelper(application)
    
    private val _weatherData = MutableStateFlow<WeatherResponse?>(null)
    val weatherData = _weatherData.asStateFlow()

    private val _favoriteCities = MutableStateFlow<List<FavoriteCity>>(emptyList())
    val favoriteCities = _favoriteCities.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    init {
        loadFavorites()
    }

    fun fetchWeather(city: String) {
        viewModelScope.launch {
            val result = repository.getWeather(city)
            result.onSuccess {
                _weatherData.value = it
                _error.value = null
                notificationHelper.showNotification("Погода обновлена", "В городе ${it.name} сейчас ${it.main.temp}°C")
            }.onFailure {
                _error.value = it.message ?: "Unknown error"
            }
        }
    }

    fun loadFavorites() {
        viewModelScope.launch {
            try {
                _favoriteCities.value = repository.getFavoriteCities()
            } catch (e: Exception) {
                _error.value = "Ошибка загрузки избранного: ${e.message}"
            }
        }
    }

    fun addToFavorites(cityName: String) {
        viewModelScope.launch {
            try {
                repository.addFavoriteCity(FavoriteCity(city_name = cityName))
                loadFavorites()
            } catch (e: Exception) {
                _error.value = "Ошибка добавления: ${e.message}"
            }
        }
    }

    fun deleteFromFavorites(id: Int) {
        viewModelScope.launch {
            try {
                repository.deleteFavoriteCity(id)
                loadFavorites()
            } catch (e: Exception) {
                _error.value = "Ошибка удаления: ${e.message}"
            }
        }
    }
}
