package com.example.minigames.ui.register.ui

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamesapplication.RetrofitInstance
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

    private val _confirmPassword = MutableLiveData<String>()
    val confirmPassword: LiveData<String> = _confirmPassword

    private val _name = MutableLiveData<String>()
    val name: LiveData<String> = _name

    private val _registerEnable = MutableLiveData<Boolean>()
    val registerEnable: LiveData<Boolean> = _registerEnable

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _registerSuccess = MutableLiveData<Boolean>()
    val registerSuccess : LiveData<Boolean> = _registerSuccess

    fun onRegisterChanged(name: String, email: String, password: String, confirmPassword: String) {
        _name.value = name
        _email.value = email
        _password.value = password
        _confirmPassword.value = confirmPassword
        _registerEnable.value = isValidForm(name, email, password, confirmPassword)
    }

    // VALIDACIÓN DEL FORMULARIO
    private fun isValidForm(
        name: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        return name.isNotEmpty() &&
                isValidEmail(email) &&
                password.length >= 6 &&
                password == confirmPassword
    }

    private fun isValidEmail(email: String): Boolean =
        Patterns.EMAIL_ADDRESS.matcher(email).matches()

    // FUNCIÓN PRINCIPAL PARA REGISTRAR
    fun onRegisterSelected() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val registerRequest = RegisterRequest(
                    email = _email.value ?: "",
                    password = _password.value ?: ""
                )

                val response = RetrofitInstance.api.register(registerRequest)

                if (response.isSuccessful) {
                    val registerResponse = response.body()
                    Log.d("REGISTER", "Usuario registrado exitosamente")
                    Log.d("REGISTER", "Token: ${registerResponse?.access_token}")
                    Log.d("REGISTER", "User ID: ${registerResponse?.user?.id}")
                    _registerSuccess.value = true


                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("REGISTER", "Error: ${response.code()} $errorBody")
                    _registerSuccess.value = false
                }
            } catch (e: Exception) {
                Log.e("REGISTER", "Excepción: ${e.localizedMessage}")
            } finally {
                _isLoading.value = false
            }
        }
    }
}

data class RegisterRequest(
    val email: String,
    val password: String
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
