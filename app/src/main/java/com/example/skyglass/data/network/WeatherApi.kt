package com.example.skyglass.data.network

import com.example.skyglass.data.model.WeatherResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object WeatherApi {
    private const val API_KEY = "8495e9cad8297414d91a7684b189e50b"
    private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"

    val client = HttpClient(Android) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }

    suspend fun getCurrentWeather(city: String): WeatherResponse {
        return client.get("${BASE_URL}weather?q=$city&appid=$API_KEY&units=metric").body()
    }
}
