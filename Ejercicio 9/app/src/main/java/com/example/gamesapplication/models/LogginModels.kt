package com.example.gamesapplication.models

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val access_token: String,
    val user: User
)

data class User(
    val id: String,
    val user_metadata: UserMetadata
)

data class UserMetadata(val name: String)
