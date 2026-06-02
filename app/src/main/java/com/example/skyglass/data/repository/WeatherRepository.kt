package com.example.skyglass.data.repository

import com.example.skyglass.data.model.WeatherResponse
import com.example.skyglass.data.network.WeatherApi

class WeatherRepository {
    suspend fun getWeather(city: String): Result<WeatherResponse> {
        return try {
            val response = WeatherApi.getCurrentWeather(city)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
