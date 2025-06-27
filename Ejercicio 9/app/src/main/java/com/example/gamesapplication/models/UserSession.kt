package com.example.gamesapplication.models

object UserSession {
    var userId: String? = null
        private set

    var userEmail: String? = null
        private set

    var accessToken: String? = null
        private set

    var userName: String? = null
        private set

    fun getToken(): String {
        return accessToken ?: ""
    }

    fun login(userId: String, email: String, token: String) {
        this.userId = userId
        this.userEmail = email
        this.accessToken = token
        this.userName = email.substringBefore("@")
    }

    fun logout() {
        userId = null
        userEmail = null
        accessToken = null
        userName = null
    }

    fun isLoggedIn(): Boolean {
        return userId != null && accessToken != null
    }

    fun getCurrentUserId(): String {
        return userId ?: ""
    }

    fun getCurrentUserName(): String {
        return userName ?: "Usuario"
    }
}