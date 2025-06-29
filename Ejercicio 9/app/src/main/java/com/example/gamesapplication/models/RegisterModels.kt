package com.example.gamesapplication.models

data class RegisterRequest(
    val email: String,
    val password: String,
    val data: Map<String, Any>? = null
)


data class RegisterResponse(
    val access_token: String?,
    val token_type: String?,
    val expires_in: Int?,
    val refresh_token: String?,
    val user: UserData?
)

data class UserData(
    val id: String,
    val email: String,
    val email_confirmed_at: String?,
    val created_at: String?
)