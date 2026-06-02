package com.example.skyglass.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.skyglass.data.model.FavoriteCity
import com.example.skyglass.data.model.WeatherResponse
import com.example.skyglass.data.repository.WeatherRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WeatherViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = WeatherRepository()
    
    private val _weatherData = MutableStateFlow<WeatherResponse?>(null)
    val weatherData = _weatherData.asStateFlow()

    private val _favoriteCities = MutableStateFlow<List<FavoriteCity>>(emptyList())
    val favoriteCities = _favoriteCities.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    // Поток для внутреннего уведомления
    private val _inAppMessage = MutableStateFlow<String?>(null)
    val inAppMessage = _inAppMessage.asStateFlow()

    init {
        loadFavorites()
    }

    fun showInstantNotification(message: String) {
        viewModelScope.launch {
            _inAppMessage.value = message
            delay(3000) // Показывать 3 секунды
            _inAppMessage.value = null
        }
    }

    fun fetchWeather(city: String) {
        viewModelScope.launch {
            val result = repository.getWeather(city)
            result.onSuccess {
                _weatherData.value = it
                _error.value = null
                showInstantNotification("Погода в ${it.name} обновлена: ${it.main.temp}°C")
            }.onFailure {
                _error.value = it.message ?: "Ошибка сети"
            }
        }
    }

    fun loadFavorites() {
        viewModelScope.launch {
            try {
                _favoriteCities.value = repository.getFavoriteCities()
            } catch (e: Exception) {
                _error.value = "Ошибка загрузки избранного"
            }
        }
    }

    fun addToFavorites(cityName: String) {
        viewModelScope.launch {
            try {
                repository.addFavoriteCity(FavoriteCity(city_name = cityName))
                loadFavorites()
                showInstantNotification("$cityName добавлен в избранное!")
            } catch (e: Exception) {
                _error.value = "Ошибка добавления"
            }
        }
    }

    fun deleteFromFavorites(id: Int) {
        viewModelScope.launch {
            try {
                repository.deleteFavoriteCity(id)
                loadFavorites()
                showInstantNotification("Город удален")
            } catch (e: Exception) {
                _error.value = "Ошибка удаления"
            }
        }
    }

    fun updateFavorite(id: Int, newName: String) {
        viewModelScope.launch {
            try {
                repository.updateFavoriteCity(id, newName)
                loadFavorites()
                showInstantNotification("Обновлено: $newName")
            } catch (e: Exception) {
                _error.value = "Ошибка обновления"
            }
        }
    }
}
