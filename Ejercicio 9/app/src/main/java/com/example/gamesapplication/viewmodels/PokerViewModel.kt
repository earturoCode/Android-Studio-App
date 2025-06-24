package com.example.gamesapplication.viewmodels


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamesapplication.logica.analizarJugada
import com.example.gamesapplication.logica.quienGana
import com.example.gamesapplication.models.GameState
import com.example.gamesapplication.models.Jugador
import com.example.gamesapplication.models.MazoDeCartas
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PokerViewModel : ViewModel() {

    private val _gameState = MutableStateFlow(GameState())
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    fun iniciarJuego(nombreJugador: String) {
        viewModelScope.launch {
            _gameState.value = _gameState.value.copy(isLoading = true)

            // Simular tiempo de mezclado y reparto
            delay(1000)

            val nuevoMazo = MazoDeCartas()
            nuevoMazo.mezclar()

            val nuevoJugador1 = Jugador(nombreJugador)
            val nuevoJugador2 = Jugador("CPU")

            nuevoJugador1.cartas = nuevoMazo.darMano()
            nuevoJugador2.cartas = nuevoMazo.darMano()

            nuevoJugador1.tipoJugada = analizarJugada(nuevoJugador1.cartas)
            nuevoJugador2.tipoJugada = analizarJugada(nuevoJugador2.cartas)

            val resultado = quienGana(nuevoJugador1, nuevoJugador2)

            _gameState.value = GameState(
                jugador1 = nuevoJugador1,
                jugador2 = nuevoJugador2,
                gameStarted = true,
                gameFinished = true,
                resultado = resultado,
                mazo = nuevoMazo,
                isLoading = false
            )
        }
    }

    fun reiniciarJuego() {
        _gameState.value = GameState()
    }

    fun jugarDeNuevo() {
        val nombreActual = _gameState.value.jugador1.nombre
        _gameState.value = GameState()
        iniciarJuego(nombreActual)
    }

    // Función para determinar si el jugador humano ganó (para puntajes)
    fun jugadorGano(): Boolean {
        val resultado = _gameState.value.resultado
        return resultado.contains(_gameState.value.jugador1.nombre) &&
                !resultado.contains("CPU")
    }

    // Función para obtener el puntaje basado en la jugada
    fun obtenerPuntaje(): Int {
        val jugador1 = _gameState.value.jugador1
        val jugada = jugador1.tipoJugada ?: return 0

        val puntajeBase = when (jugada) {
            "Escalera de Color" -> 100
            "Póker" -> 80
            "Full House" -> 60
            "Color" -> 50
            "Escalera" -> 40
            "Trío" -> 30
            "Doble Par" -> 20
            "Par" -> 10
            else -> 5 // Carta Alta
        }

        // Bonus si ganó
        return if (jugadorGano()) puntajeBase * 2 else puntajeBase
    }
}