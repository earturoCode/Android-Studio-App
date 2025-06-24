package com.example.gamesapplication.models

// Clase que representa una carta
data class Carta(val valor: String, val palo: String) {

    fun mostrar(): String {
        return "$valor$palo"
    }

    fun valoresCartas(): Int {
        return when (valor) {
            "A" -> 14
            "K" -> 13
            "Q" -> 12
            "J" -> 11
            "T" -> 10
            else -> valor.toIntOrNull() ?: 0
        }
    }

    fun getDisplayValue(): String {
        return when (valor) {
            "A" -> "A"
            "K" -> "K"
            "Q" -> "Q"
            "J" -> "J"
            "T" -> "10"
            else -> valor
        }
    }

    fun getSuitSymbol(): String {
        return when (palo) {
            "S" -> "♠"
            "H" -> "♥"
            "D" -> "♦"
            "C" -> "♣"
            else -> palo
        }
    }

    fun getSuitColor(): androidx.compose.ui.graphics.Color {
        return when (palo) {
            "H", "D" -> androidx.compose.ui.graphics.Color.Red
            "S", "C" -> androidx.compose.ui.graphics.Color.Black
            else -> androidx.compose.ui.graphics.Color.Black
        }
    }
}

// Jugador tiene un nombre y cartas en la mano
data class Jugador(
    val nombre: String,
    var cartas: MutableList<Carta> = mutableListOf(),
    var tipoJugada: String? = null
)

// Clase para gestionar el mazo de cartas
class MazoDeCartas {
    var cartas: MutableList<Carta> = mutableListOf()

    // Constructor que crea todas las cartas
    init {
        val valores = listOf("A", "2", "3", "4", "5", "6", "7", "8", "9", "T", "J", "Q", "K")
        val palos = listOf("S", "H", "D", "C")

        // Crear cada combinación de valor y palo
        for (palo in palos) {
            for (valor in valores) {
                val nuevaCarta = Carta(valor, palo)
                cartas.add(nuevaCarta)
            }
        }
    }

    // Mezcla las cartas con shuffle()
    fun mezclar() {
        cartas.shuffle()
    }

    // Reparte una carta del mazo
    fun sacarCarta(): Carta? {
        return if (cartas.isEmpty()) {
            null
        } else {
            cartas.removeAt(0)
        }
    }

    // Entrega 5 cartas para una mano
    fun darMano(): MutableList<Carta> {
        val mano = mutableListOf<Carta>()

        // Intenta dar 5 cartas
        repeat(5) {
            sacarCarta()?.let { carta ->
                mano.add(carta)
            }
        }

        return mano
    }
}

// Estado del juego
data class GameState(
    val jugador1: Jugador = Jugador("Usuario"),
    val jugador2: Jugador = Jugador("CPU"),
    val gameStarted: Boolean = false,
    val gameFinished: Boolean = false,
    val resultado: String = "",
    val mazo: MazoDeCartas = MazoDeCartas(),
    val isLoading: Boolean = false
)