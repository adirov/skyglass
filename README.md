# Skyglass 🌤️

Skyglass is a modern Android weather application built with Kotlin, Jetpack Compose, and Ktor.

## Features
- **Real-time Weather:** Get current weather data using OpenWeatherMap API.
- **Authentication:** Secure login and registration powered by Supabase.
- **Modern UI:** Built entirely with Jetpack Compose following Material 3 guidelines.
- **Clean Architecture:** Uses Repository pattern and ViewModel for better code maintenance.

## Tech Stack
- **Language:** [Kotlin](https://kotlinlang.org/)
- **UI Framework:** [Jetpack Compose](https://developer.android.com/jetpack/compose)
- **Networking:** [Ktor](https://ktor.io/)
- **Backend/Auth:** [Supabase](https://supabase.com/)
- **Serialization:** [Kotlinx Serialization](https://github.com/Kotlin/kotlinx.serialization)

## Setup
1. Clone the repository.
2. Add your API keys:
   - OpenWeatherMap API Key in `WeatherApi.kt`.
   - Supabase URL and Anon Key in `SupabaseClient.kt`.
3. Build and run the app.

## License
MIT License
