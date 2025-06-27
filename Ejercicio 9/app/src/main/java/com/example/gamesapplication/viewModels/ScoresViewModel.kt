package com.example.gamesapplication.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamesapplication.RetrofitInstance
import com.example.gamesapplication.models.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class ScoresViewModel : ViewModel() {

    private val _scores = MutableStateFlow<List<Score>>(emptyList())
    val scores: StateFlow<List<Score>> = _scores.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _currentUser = MutableStateFlow("Usuario")
    val currentUser: StateFlow<String> = _currentUser.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        cargarPuntajes()
    }

    private fun cargarPuntajes() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                val response = RetrofitInstance.restApi.getAllScores(
                    apiKey = RetrofitInstance.API_KEY,
                    token = "Bearer ${UserSession.getToken()}"
                )
                if (response.isSuccessful) {
                    val apiScores = response.body() ?: emptyList()
                    val tocameScores = apiScores.filter { it.gameId == 2 }

                    val scores = tocameScores.map {
                        Score(
                            playerName = getUserName(it.userId),
                            points = it.score,
                            date = it.date,
                            gameType = "Tocame",
                            userId = it.userId,
                            gameId = it.gameId
                        )
                    }
                    _scores.value = scores.sortedByDescending { it.points }
                    Log.d("ScoresViewModel", "Puntajes cargados: ${scores.size}")
                } else {
                    _errorMessage.value = "Error al cargar puntajes"
                    Log.e("ScoresViewModel", "Error código: ${response.code()}")
                }
            } catch (e: Exception) {
                _errorMessage.value = "Sin conexión"
                Log.e("ScoresViewModel", "Error excepción", e)
            } finally {
                _isLoading.value = false
            }
        }
    }


    private fun getUserName(userId: String): String {
        return if (userId == UserSession.getCurrentUserId()) {
            UserSession.getCurrentUserName()
        } else {
            "Usuario#${userId.take(8)}"
        }
    }

    // Solo acepta puntajes de Tocame
    fun agregarPuntajeTocame(points: Int) {
        viewModelScope.launch {
            if (!UserSession.isLoggedIn()) {
                _errorMessage.value = "Debes iniciar sesión para guardar puntajes"
                return@launch
            }

            try {
                val date = SimpleDateFormat("MM-dd-yyyy", Locale.getDefault()).format(Date())
                val request = CreateScoreRequest(
                    userId = UserSession.getCurrentUserId(),
                    gameId = 2,
                    score = points,
                    date = date
                )

                val response = RetrofitInstance.restApi.createScore(
                    apiKey = RetrofitInstance.API_KEY,
                    token = "Bearer ${UserSession.getToken()}",
                    request = request
                )

                if (response.isSuccessful) {
                    Log.d("ScoresViewModel", "Puntaje guardado: $points")
                    cargarPuntajes()
                } else {
                    Log.e("ScoresViewModel", "Error guardando: ${response.code()}")
                    _errorMessage.value = "Error al guardar puntaje"
                }
            } catch (e: Exception) {
                Log.e("ScoresViewModel", "Excepción guardando puntaje", e)
                _errorMessage.value = "Error de conexión al guardar"
            }
        }
    }


    fun setCurrentUser(username: String) {
        _currentUser.value = username
    }

    fun getFilteredScores(filterType: String): List<Score> {
        val allScores = _scores.value.sortedByDescending { it.points }

        return when (filterType) {
            "Top 10" -> allScores.take(10)
            "Top 5" -> allScores.take(5)
            "Mis Puntajes" -> allScores.filter {
                it.userId == UserSession.getCurrentUserId() ||
                        it.playerName == UserSession.getCurrentUserName()
            }

            else -> allScores.take(10)
        }
    }

    fun refresh() {
        cargarPuntajes()
    }

    fun clearError() {
        _errorMessage.value = null
    }
}