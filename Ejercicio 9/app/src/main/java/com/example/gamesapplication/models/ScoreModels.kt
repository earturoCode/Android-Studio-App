package com.example.gamesapplication.models

import com.google.gson.annotations.SerializedName

// Modelo para Game de la API
data class Game(
    val id: Int,
    val name: String,
    val description: String? = null,
    @SerializedName("created_at") val createdAt: String? = null
)

// Modelo para Score de la API
//data class Score(
// val id: Int? = null,
// @SerializedName("user_id") val userId: String,
//  @SerializedName("game_id") val gameId: Int,
//  val score: Int,
// val date: String,
//  @SerializedName("created_at") val createdAt: String? = null
//)

// Modelo para crear un score
data class CreateScoreRequest(
    @SerializedName("user_id") val userId: String,
    @SerializedName("game_id") val gameId: Int,
    val score: Int,
    val date: String
)

// Respuesta del servidor al crear score
data class CreateScoreResponse(
    val id: Int,
    @SerializedName("user_id") val userId: String,
    @SerializedName("game_id") val gameId: Int,
    val score: Int,
    val date: String,
    @SerializedName("created_at") val createdAt: String
)

// Estados de sincronización
enum class SyncStatus {
    PENDING,
    SYNCED,
    FAILED
}

// Score local con estado de sincronización
data class LocalScore(
    val id: Long = 0,
    val userId: String,
    val gameId: Int,
    val score: Int,
    val date: String,
    val syncStatus: SyncStatus = SyncStatus.PENDING,
    val localTimestamp: Long = System.currentTimeMillis()
)

data class Score(
    val playerName: String,
    val points: Int,
    val date: String,
    val gameType: String // "Poker" o "Tocame"
)