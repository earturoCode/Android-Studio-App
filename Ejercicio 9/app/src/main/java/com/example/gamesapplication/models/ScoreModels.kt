package com.example.gamesapplication.models

import com.google.gson.annotations.SerializedName

// Modelo para Game recibido desde la API
data class Game(
    val id: Int,
    val name: String,
    val description: String? = null,
    @SerializedName("created_at") val createdAt: String? = null
)

// Modelo para Score recibido desde la API
data class ApiScore(
    val id: Int? = null,
    @SerializedName("user_id") val userId: String,
    @SerializedName("game_id") val gameId: Int,
    val score: Int,
    val date: String,
    @SerializedName("created_at") val createdAt: String? = null
)

// Modelo para enviar un nuevo Score (POST)
data class CreateScoreRequest(
    @SerializedName("user_id") val userId: String,
    @SerializedName("game_id") val gameId: Int,
    val score: Int,
    val date: String
)

// Estados de sincronización local
enum class SyncStatus {
    PENDING,
    SYNCED,
    FAILED
}

// Modelo para Score local con estado de sincronización
data class LocalScore(
    val id: Long = 0,
    val userId: String,
    val gameId: Int,
    val score: Int,
    val date: String,
    val syncStatus: SyncStatus = SyncStatus.PENDING,
    val localTimestamp: Long = System.currentTimeMillis()
)

// Modelo para mostrar Score en la UI
data class Score(
    val playerName: String,
    val points: Int,
    val date: String,
    val gameType: String,
    val userId: String = "",
    val gameId: Int = 1
)


// Extensión para convertir ApiScore + lista de juegos en Score para la UI
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

// Extensión para convertir Score (UI) en CreateScoreRequest (para API)
fun Score.toCreateScoreRequest(): CreateScoreRequest {
    return CreateScoreRequest(
        userId = this.userId,
        gameId = this.gameId,
        score = this.points,
        date = this.date
    )
}
