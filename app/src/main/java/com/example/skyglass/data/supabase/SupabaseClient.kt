package com.example.skyglass.data.supabase

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime

object SupabaseClient {
    private const val SUPABASE_URL = "https://dnmdhyjjdvqafcuoodoz.supabase.co"
    private const val SUPABASE_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImRubWRoeWpqZHZxYWZjdW9vZG96Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3ODAzOTYyMzUsImV4cCI6MjA5NTk3MjIzNX0.cWpQZzLZ5josrNHK1SSfGo87ZZ2XfOVJeXBQAmQ6ztA"

    val client = createSupabaseClient(
        supabaseUrl = SUPABASE_URL,
        supabaseKey = SUPABASE_KEY
    ) {
        install(Auth)
        install(Postgrest)
        install(Realtime)
    }
}
