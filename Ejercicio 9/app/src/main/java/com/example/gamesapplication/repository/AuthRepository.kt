package com.example.gamesapplication.repository

import com.example.gamesapplication.dataStore.UserPreferencesManager
import com.example.gamesapplication.models.LoginRequest
import com.example.gamesapplication.service.AuthenticationService

class AuthRepository (private val authenticationService: AuthenticationService=AuthenticationService()){
    suspend fun authRepository(loginRequest: LoginRequest):String{
        try {
            val response = authenticationService.loginService(loginRequest)
            if (response.isSuccessful) {
                val loginResponse = response.body()
                val userId = loginResponse?.user?.id ?: ""
                val token = loginResponse?.access_token ?: ""
                val name = loginResponse?.user?.user_metadata?.name ?: ""
                UserPreferencesManager.get().saveUserData(userId, token, name)
                return "Se ha loggeado correctamente"
            } else {
                return when  (response.code()){
                    400 -> "correo o contraseña incorrecta"
                    401 -> "Correo o contraseña incorrecta"
                    402 -> "Correo no encontrado"
                    else -> "error desconocido"
                }
            }
        }
        catch (e:Exception){
            return "Error de conexion"
        }
    }

}