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

//    private val _scores = MutableStateFlow<List<Score>>(emptyList())
//    val scores: StateFlow<List<Score>> = _scores.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _currentUser = MutableStateFlow("Usuario")
    val currentUser: StateFlow<String> = _currentUser.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
//        cargarPuntajes()
    }

//    private fun cargarPuntajes() {
//        viewModelScope.launch {
//            _isLoading.value = true
//            _errorMessage.value = null
//
//            try {
//                val response = RetrofitInstance.authApi.getAllScores()
//                if (response.isSuccessful) {
//                    val apiScores = response.body() ?: emptyList()
//
//                    // Solo filtrar puntajes de Tocame (gameId = 2)
//                    val tocameScores = apiScores.filter { it.gameId == 2 }
//
//                    val scores = tocameScores.map { apiScore ->
//                        Score(
//                            playerName = getUserName(apiScore.userId),
//                            points = apiScore.score,
//                            date = apiScore.date,
//                            gameType = "Tocame",
//                            userId = apiScore.userId,
//                            gameId = apiScore.gameId
//                        )
//                    }
//                    _scores.value = scores.sortedByDescending { it.points }
//                    Log.d("ScoresViewModel", "Puntajes de Tocame cargados: ${scores.size}")
//                } else {
//                    _errorMessage.value = "Error al cargar puntajes del servidor"
//                    _scores.value = emptyList()
//                    Log.e("ScoresViewModel", "Error API: ${response.code()}")
//                }
//            } catch (e: Exception) {
//                _errorMessage.value = "Sin conexión a internet"
//                _scores.value = emptyList()
//                Log.e("ScoresViewModel", "Error cargando puntajes", e)
//            } finally {
//                _isLoading.value = false
//            }
//        }
//    }

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
            try {
                if (UserSession.isLoggedIn()) {
                    val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

//                    val request = CreateScoreRequest(
//                        userId = UserSession.getCurrentUserId(),
//                        gameId = 2,
//                        score = points,
//                        date = currentDate
//                    )

//                    val response = RetrofitInstance.authApi.createScore(request)
//                    if (response.isSuccessful) {
//                        Log.d("ScoresViewModel", "Puntaje de Tocame guardado: $points")
//                        cargarPuntajes() // Recargar lista
//                    } else {
//                        Log.e("ScoresViewModel", "Error guardando puntaje: ${response.code()}")
//                        _errorMessage.value = "Error al guardar puntaje"
//                    }
                } else {
                    _errorMessage.value = "Debes iniciar sesión para guardar puntajes"
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

//    fun getFilteredScores(filterType: String): List<Score> {
//        val allScores = _scores.value.sortedByDescending { it.points }
//
//        return when (filterType) {
//            "Top 10" -> allScores.take(10)
//            "Top 5" -> allScores.take(5)
//            "Mis Puntajes" -> allScores.filter {
//                it.userId == UserSession.getCurrentUserId() ||
//                        it.playerName == UserSession.getCurrentUserName()
//            }
//
//            else -> allScores.take(10)
//        }
//    }

    fun refresh() {
//        cargarPuntajes()
    }

    fun clearError() {
        _errorMessage.value = null
    }
}