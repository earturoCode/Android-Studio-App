package com.example.gamesapplication.viewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.example.gamesapplication.RetrofitInstance
import com.example.gamesapplication.models.LoginRequest
import com.example.gamesapplication.navigation.Routes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    var username = mutableStateOf("")
    var password = mutableStateOf("")
    suspend fun sendLoginRequest(navController: NavHostController) {
        val loginRequest = LoginRequest(
            email = username.value,
            password = password.value
        )
        try {
            val response = RetrofitInstance.api.login(loginRequest)
            if (response.isSuccessful) {
                val loginResponse = response.body()
                navController.navigate(Routes.HOME)
                Log.d("LOGIN", "Token: ${loginResponse?.access_token}")
                Log.d("LOGIN", "id: ${loginResponse?.user?.id}")
            } else {
                Log.e("LOGIN", "Error: ${response.code()} ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            Log.e("LOGIN", "Excepción: ${e.localizedMessage}")
        }
    }
}