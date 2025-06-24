package com.example.gamesapplication.models

data class CreateScoreRequest(
    val user_id: String,
    val game_id: String,
    val score : Int,
    val date : String
)