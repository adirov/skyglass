package com.example.skyglass.data.model

import kotlinx.serialization.Serializable

@Serializable
data class WeatherResponse(
    val main: Main,
    val weather: List<Weather>,
    val name: String
)

@Serializable
data class Main(
    val temp: Double,
    val pressure: Int,
    val humidity: Int
)

@Serializable
data class Weather(
    val main: String,
    val description: String,
    val icon: String
)

@Serializable
data class FavoriteCity(
    val id: Int? = null,
    val user_id: String? = null,
    val city_name: String
)
