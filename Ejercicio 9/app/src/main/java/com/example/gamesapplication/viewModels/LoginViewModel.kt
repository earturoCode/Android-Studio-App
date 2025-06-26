package com.example.gamesapplication.viewModels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.example.gamesapplication.RetrofitInstance
import com.example.gamesapplication.dataStore.UserPreferencesManager
import com.example.gamesapplication.models.LoginRequest
import com.example.gamesapplication.navigation.Routes
import androidx.compose.runtime.*

class LoginViewModel : ViewModel() {

    var username by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set
    fun updateUsername(newUsername:String){
        username = newUsername
    }
    fun updatePassword(newPassword:String){
        password=newPassword
    }

    suspend fun sendLoginRequest(navController: NavHostController) {
        val loginRequest = LoginRequest(
            email = username,
            password = password
        )
        try {
            val response = RetrofitInstance.authApi.login(loginRequest)
            if (response.isSuccessful) {
                val loginResponse = response.body()
                navController.navigate(Routes.HOME)
                val userDefaults = UserPreferencesManager.get()
                val userId = loginResponse?.user?.id?:""
                val token = loginResponse?.access_token ?: ""
                val name = loginResponse?.user?.user_metadata?.name?:""
                userDefaults.saveUserData(userId,token, name)
                Log.d("LOGIN", "Token: ${loginResponse?.access_token}")
                Log.d("LOGIN", "id: ${loginResponse?.user?.id}")
                Log.d("LOGIN", "id: $name")
            } else {
                Log.e("LOGIN", "Error: ${response.code()} ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            Log.e("LOGIN", "Excepción: ${e.localizedMessage}")
        }
    }
}