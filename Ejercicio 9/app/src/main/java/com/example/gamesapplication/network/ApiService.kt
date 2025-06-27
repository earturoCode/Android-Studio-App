package com.example.gamesapplication.network

import com.example.gamesapplication.models.*
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @POST("scores")
    suspend fun createScore(
        @Header("apikey") apiKey: String,
        @Header("Authorization") token: String,
        @Body request: CreateScoreRequest
    ): Response<ResponseBody>

    @GET("scores")
    suspend fun getAllScores(
        @Header("apikey") apiKey: String,
        @Header("Authorization") token: String
    ): Response<List<ApiScore>>
}
