package com.example.gamesapplication.caseOfUser

import com.example.gamesapplication.service.AuthenticationService
import com.example.gamesapplication.models.LoginRequest
import com.example.gamesapplication.repository.AuthRepository

class AuthCaseOfUse(private val authRepository: AuthRepository = AuthRepository()){
    suspend fun loginCaseOfUse(username:String, password:String) : String =
        authRepository.authRepository(LoginRequest(username,password))

}