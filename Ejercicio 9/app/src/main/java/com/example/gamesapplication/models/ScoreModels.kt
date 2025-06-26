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
data class ApiScore(
    val id: Int? = null,
    @SerializedName("user_id") val userId: String,
    @SerializedName("game_id") val gameId: Int,
    val score: Int,
    val date: String,
    @SerializedName("created_at") val createdAt: String? = null
)

// Modelo para crear un score (request al servidor)
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

// Modelo unificado para la UI (local)
data class Score(
    val playerName: String,
    val points: Int,
    val date: String,
    val gameType: String,
    val userId: String = "",
    val gameId: Int = 1
)

// Extensión para convertir ApiScore a Score
fun ApiScore.toScore(games: List<Game>, userName: String): Score {
    val game = games.find { it.id == this.gameId }
    return Score(
        playerName = userName,
        points = this.score,
        date = this.date,
        gameType = game?.name ?: "Desconocido",
        userId = this.userId,
        gameId = this.gameId
    )
}

// Extensión para convertir Score a CreateScoreRequest
fun Score.toCreateScoreRequest(): CreateScoreRequest {
    return CreateScoreRequest(
        userId = this.userId,
        gameId = this.gameId,
        score = this.points,
        date = this.date
    )
}