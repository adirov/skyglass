package com.example.skyglass.data.repository

import com.example.skyglass.data.model.FavoriteCity
import com.example.skyglass.data.model.WeatherResponse
import com.example.skyglass.data.network.WeatherApi
import com.example.skyglass.data.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest

class WeatherRepository {
    suspend fun getWeather(city: String): Result<WeatherResponse> {
        return try {
            val response = WeatherApi.getCurrentWeather(city)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addFavoriteCity(city: FavoriteCity) {
        SupabaseClient.client.postgrest["favorite_cities"].insert(city)
    }

    suspend fun getFavoriteCities(): List<FavoriteCity> {
        return SupabaseClient.client.postgrest["favorite_cities"]
            .select().decodeList<FavoriteCity>()
    }

    suspend fun deleteFavoriteCity(id: Int) {
        SupabaseClient.client.postgrest["favorite_cities"].delete {
            filter {
                eq("id", id)
            }
        }
    }

    suspend fun updateFavoriteCity(id: Int, newName: String) {
        SupabaseClient.client.postgrest["favorite_cities"].update(
            mapOf("city_name" to newName)
        ) {
            filter {
                eq("id", id)
            }
        }
    }
}
