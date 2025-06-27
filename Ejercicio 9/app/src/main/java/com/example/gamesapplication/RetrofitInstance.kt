package com.example.gamesapplication

import com.example.gamesapplication.network.ApiService
import com.example.gamesapplication.network.AuthService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    const val API_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imx2bXliY3locmJpc2Zqb3VoYnJ4Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDg1Mjk2NzcsImV4cCI6MjA2NDEwNTY3N30.f2t60RjJh91cNlggE_2ViwPXZ1eXP7zD18rWplSI4jE"

    val authApi: AuthService by lazy {
        Retrofit.Builder()
            .baseUrl("https://lvmybcyhrbisfjouhbrx.supabase.co/auth/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthService::class.java)
    }
    val restApi: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://lvmybcyhrbisfjouhbrx.supabase.co/rest/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}