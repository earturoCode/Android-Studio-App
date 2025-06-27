package com.example.gamesapplication.service

import com.example.gamesapplication.RetrofitInstance
import com.example.gamesapplication.models.LoginRequest
import com.example.gamesapplication.models.LoginResponse
import okhttp3.ResponseBody
import retrofit2.Response

class AuthenticationService(){
    suspend fun loginService(loginRequest:LoginRequest): Response<LoginResponse> {
        return RetrofitInstance.authApi.login(loginRequest)
    }
}