package com.example.gamesapplication.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamesapplication.models.Score
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

    // Usuario actual logueado
    private val _currentUser = MutableStateFlow("Usuario")
    val currentUser: StateFlow<String> = _currentUser.asStateFlow()

    init {
        cargarPuntajes()
    }

    fun setCurrentUser(username: String) {
        _currentUser.value = username
    }

    private fun cargarPuntajes() {
        viewModelScope.launch {
            _isLoading.value = true
            _scores.value = getSampleScores()
            _isLoading.value = false
        }
    }

    fun agregarPuntaje(playerName: String, points: Int, gameType: String) {
        viewModelScope.launch {
            val currentDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                .format(Date())

            val newScore = Score(
                playerName = playerName,
                points = points,
                date = currentDate,
                gameType = gameType
            )

            val updatedScores = (_scores.value + newScore)
                .sortedByDescending { it.points }

            _scores.value = updatedScores
        }
    }

    fun limpiarPuntajes() {
        _scores.value = emptyList()
    }

    fun getFilteredScores(filterType: String): List<Score> {
        val allScores = _scores.value.sortedByDescending { it.points }

        return when (filterType) {
            "Top 10" -> allScores.take(10)
            "Top 5" -> allScores.take(5)
            "Mis Puntajes" -> allScores.filter { it.playerName == _currentUser.value }
            else -> allScores.take(10) // Por defecto Top 10
        }
    }

    private fun getSampleScores(): List<Score> {
        return listOf(
            Score("Ana", 1250, "25/06/2025", "Poker"),
            Score("Usuario", 1200, "25/06/2025", "Tocame"), // Puntajes del usuario actual
            Score("Carlos", 1100, "24/06/2025", "Tocame"),
            Score("Usuario", 1050, "24/06/2025", "Poker"), // Otro puntaje del usuario
            Score("María", 950, "23/06/2025", "Poker"),
            Score("Pedro", 900, "22/06/2025", "Tocame"),
            Score("Sofía", 850, "21/06/2025", "Poker"),
            Score("Usuario", 800, "21/06/2025", "Tocame"), // Otro del usuario
            Score("Diego", 750, "20/06/2025", "Tocame"),
            Score("Laura", 700, "19/06/2025", "Poker"),
            Score("Miguel", 650, "18/06/2025", "Tocame"),
            Score("Carmen", 600, "17/06/2025", "Poker"),
            Score("Roberto", 550, "16/06/2025", "Tocame"),
            Score("Lucía", 500, "15/06/2025", "Poker"),
            Score("Fernando", 450, "14/06/2025", "Tocame")
        ).sortedByDescending { it.points }
    }
}