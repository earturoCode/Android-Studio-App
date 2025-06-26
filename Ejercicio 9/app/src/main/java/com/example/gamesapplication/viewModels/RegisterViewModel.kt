package com.example.gamesapplication.viewModels

import android.R.attr.delay
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamesapplication.RetrofitInstance
import com.example.gamesapplication.models.RegisterRequest
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
ViewModel para la pantalla de registro
Maneja la validación de datos y la comunicación con la API de Supabase
 */

class RegisterViewModel : ViewModel() {

    // Estados de los campos del formulario
    private val _name = MutableLiveData<String>()
    val name: LiveData<String> = _name

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

    private val _confirmPassword = MutableLiveData<String>()
    val confirmPassword: LiveData<String> = _confirmPassword

    // Estado del botón de registro
    private val _registerEnable = MutableLiveData<Boolean>()
    val registerEnable: LiveData<Boolean> = _registerEnable

    // Estados de carga y resultado
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _registerSuccess = MutableLiveData<Boolean>()
    val registerSuccess: LiveData<Boolean> = _registerSuccess

    // Estados de errores por campo
    private val _nameError = MutableLiveData<String?>()
    val nameError: LiveData<String?> = _nameError

    private val _emailError = MutableLiveData<String?>()
    val emailError: LiveData<String?> = _emailError

    private val _passwordError = MutableLiveData<String?>()
    val passwordError: LiveData<String?> = _passwordError

    private val _confirmPasswordError = MutableLiveData<String?>()
    val confirmPasswordError: LiveData<String?> = _confirmPasswordError

    // Error general del servidor
    private val _generalError = MutableLiveData<String?>()
    val generalError: LiveData<String?> = _generalError

    // Jobs para manejar el delay de validación
    private var validationJob: Job? = null

    private fun validarName(name: String): String? {
        return when {
            name.isBlank() -> "El nombre es obligatorio"
            name.trim().length < 2 -> "El nombre debe tener al menos 2 caracteres"
            name.trim().length > 50 -> "El nombre no puede tener más de 50 caracteres"
            else -> null
        }
    }

    private fun validarEmail(email: String): String? {
        val emailTrimmed = email.trim().lowercase()
        return when {
            emailTrimmed.isEmpty() -> "El email es obligatorio"
            !Patterns.EMAIL_ADDRESS.matcher(emailTrimmed).matches() -> "Ingresa un email válido"
            emailTrimmed.length > 254 -> "El email es demasiado largo"
            emailTrimmed.contains("..") -> "El email no puede contener puntos consecutivos"
            emailTrimmed.startsWith(".") || emailTrimmed.endsWith(".") -> "El email no puede comenzar o terminar con punto"
            else -> null
        }
    }

    private fun validarPassword(password: String): String? {
        return when {
            password.isEmpty() -> "La contraseña es obligatoria"
            password.length < 6 -> "La contraseña debe tener al menos 6 caracteres"
            password.length > 30 -> "La contraseña no puede tener más de 30 caracteres"
            !password.any { it.isDigit() } -> "La contraseña debe contener al menos un número"
            else -> null
        }
    }

    private fun validarConfirmPassword(password: String, confirmPassword: String): String? {
        return when {
            confirmPassword.isEmpty() -> "Confirma tu contraseña"
            password != confirmPassword -> "Las contraseñas no coinciden"
            else -> null
        }
    }


    fun onRegisterChanged(name: String, email: String, password: String, confirmPassword: String) {
        // Actualizar valores inmediatamente
        _name.value = name
        _email.value = email
        _password.value = password
        _confirmPassword.value = confirmPassword
        _generalError.value = null

        // Cancelar validación anterior
        validationJob?.cancel()

        // Actualizar botón inmediatamente
        _registerEnable.value = isValidForm(name, email, password, confirmPassword)

        // Validar después de 1 segundo de inactividad (solo si hay contenido)
        validationJob = viewModelScope.launch {
            delay(1000) // Espera 1 segundo después de que el usuario deje de escribir

            // Solo mostrar errores si el campo tiene contenido
            _nameError.value = if (name.isNotBlank()) validarName(name) else null
            _emailError.value = if (email.isNotBlank()) validarEmail(email) else null
            _passwordError.value = if (password.isNotBlank()) validarPassword(password) else null
            _confirmPasswordError.value = if (confirmPassword.isNotBlank()) {
                validarConfirmPassword(password, confirmPassword)
            } else null
        }
    }


    private fun isValidForm(
        name: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        return validarName(name) == null &&
                validarEmail(email) == null &&
                validarPassword(password) == null &&
                validarConfirmPassword(password, confirmPassword) == null
    }

    fun onRegisterSelected() {
        viewModelScope.launch {
            _isLoading.value = true
            _generalError.value = null

            try {
                // Crear request con datos sanitizados
                val registerRequest = RegisterRequest(
                    email = _email.value?.trim()?.lowercase() ?: "",
                    password = _password.value ?: ""
                )

                // Llamada a la API
                val response = RetrofitInstance.authApi.register(registerRequest)

                if (response.isSuccessful) {
                    val registerResponse = response.body()
                    Log.d("REGISTER", "Usuario registrado exitosamente")
                    Log.d("REGISTER", "Token: ${registerResponse?.access_token}")
                    Log.d("REGISTER", "User ID: ${registerResponse?.user?.id}")
                    _registerSuccess.value = true
                } else {
                    handleServerError(response.code())
                }
            } catch (e: Exception) {
                handleException(e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     *Maneja errores específicos del servidor Supabase
     */
    private fun handleServerError(code: Int) {
        val errorMessage = when (code) {
            400 -> "Los datos enviados no son válidos. Verifica la información."
            409, 422 -> "Este email ya está registrado. Intenta con otro o inicia sesión."
            429 -> "Demasiados intentos. Espera unos minutos antes de intentar nuevamente."
            500 -> "Error interno del servidor. Nuestro equipo ha sido notificado."
            502, 503, 504 -> "Servicio temporalmente no disponible. Intenta más tarde."
            else -> "Error inesperado (Código: $code). Contacta soporte si persiste."
        }
        _generalError.value = errorMessage
        _registerSuccess.value = false
    }

    /**
     * Maneja excepciones de red y conexión
     * @param e Excepción capturada
     */
    private fun handleException(e: Exception) {
        val errorMessage = when {
            e.message?.contains("timeout", ignoreCase = true) == true ->
                "La conexión tardó demasiado. Verifica tu internet e intenta nuevamente."

            e.message?.contains("network", ignoreCase = true) == true ->
                "Error de red. Verifica tu conexión a internet."

            e.message?.contains("host", ignoreCase = true) == true ->
                "No se pudo conectar al servidor. Verifica tu conexión."

            else -> "Error de conexión. Verifica tu internet e intenta nuevamente."
        }
        _generalError.value = errorMessage
        Log.e("REGISTER", "Excepción en registro: ${e.localizedMessage}", e)
    }

    /**
     * Limpia errores cuando el usuario empieza a escribir
     */
    fun clearErrors() {
        _generalError.value = null
    }
}


// Modelos de respuestas

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