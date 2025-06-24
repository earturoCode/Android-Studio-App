package com.example.gamesapplication.logica

import com.example.gamesapplication.models.Carta
import com.example.gamesapplication.models.Jugador

// Función para detectar si hay una escalera
fun esEscalera(cartas: List<Carta>): Boolean {
    // Obtener los valores y ordenarlos
    val valores = cartas.map { it.valoresCartas() }.toMutableList()
    valores.sort()

    // Eliminar duplicados para manejar posibles errores
    val valoresUnicos = valores.toSet().sorted()

    // Caso especial: A-2-3-4-5
    if (valoresUnicos == listOf(2, 3, 4, 5, 14)) {
        return true
    }

    // Debe haber 5 valores distintos
    if (valoresUnicos.size != 5) {
        return false
    }

    // Comprobar si cada valor es uno más que el anterior
    for (i in 0 until valoresUnicos.size - 1) {
        if (valoresUnicos[i + 1] != valoresUnicos[i] + 1) {
            return false
        }
    }

    return true
}

// Función simple para detectar si hay color (mismo palo)
fun esColor(cartas: List<Carta>): Boolean {
    val primerPalo = cartas[0].palo

    // Si alguna carta tiene palo diferente, no es color
    return cartas.all { it.palo == primerPalo }
}

// Función para contar repeticiones de cada valor
fun contarRepeticiones(cartas: List<Carta>): List<Int> {
    val conteo = mutableMapOf<Int, Int>()

    // Contar cuántas veces aparece cada valor
    for (carta in cartas) {
        val valor = carta.valoresCartas()
        conteo[valor] = conteo.getOrDefault(valor, 0) + 1
    }

    // Ordenar las repeticiones de mayor a menor
    return conteo.values.sortedDescending()
}

// Función para encontrar el tipo de jugada
fun analizarJugada(cartas: List<Carta>): String {
    val hayEscalera = esEscalera(cartas)
    val hayColor = esColor(cartas)
    val repeticiones = contarRepeticiones(cartas)

    // Verificar cada jugada posible en orden de importancia
    return when {
        hayEscalera && hayColor -> "Escalera de Color"
        repeticiones == listOf(4, 1) -> "Póker"
        repeticiones == listOf(3, 2) -> "Full House"
        hayColor -> "Color"
        hayEscalera -> "Escalera"
        repeticiones.first() == 3 -> "Trío"
        repeticiones == listOf(2, 2, 1) -> "Doble Par"
        repeticiones.first() == 2 -> "Par"
        else -> "Carta Alta"
    }
}

// Se evalúa cada jugada posible
fun valorJugada(jugada: String): Int {
    return when (jugada) {
        "Escalera de Color" -> 8
        "Póker" -> 7
        "Full House" -> 6
        "Color" -> 5
        "Escalera" -> 4
        "Trío" -> 3
        "Doble Par" -> 2
        "Par" -> 1
        else -> 0 // Carta Alta
    }
}

// Función para el desempate
fun compararCartasAltas(cartas1: List<Carta>, cartas2: List<Carta>): Int {
    val valores1 = cartas1.map { it.valoresCartas() }.sortedDescending()
    val valores2 = cartas2.map { it.valoresCartas() }.sortedDescending()

    // Comparar carta por carta
    for (i in 0 until minOf(valores1.size, valores2.size)) {
        when {
            valores1[i] > valores2[i] -> return 1
            valores2[i] > valores1[i] -> return 2
        }
    }

    return 0 // Empate verdadero
}

// Función para mostrar el valor como texto
fun mostrarValor(valor: Int): String {
    return when (valor) {
        14 -> "As"
        13 -> "Rey"
        12 -> "Reina"
        11 -> "Jota"
        10 -> "10"
        else -> "$valor"
    }
}

// Función para determinar quién gana
fun quienGana(jugador1: Jugador, jugador2: Jugador): String {
    // Asegurarse de que hay jugadas detectadas
    val jugada1 = jugador1.tipoJugada ?: return "Error: falta analizar las jugadas"
    val jugada2 = jugador2.tipoJugada ?: return "Error: falta analizar las jugadas"

    val valor1 = valorJugada(jugada1)
    val valor2 = valorJugada(jugada2)

    // Comparar los valores de las jugadas
    return when {
        valor1 > valor2 -> "${jugador1.nombre} gana con $jugada1"
        valor2 > valor1 -> "${jugador2.nombre} gana con $jugada2"
        else -> {
            // Si tienen la misma jugada, comparar todas las cartas en orden
            when (val resultado = compararCartasAltas(jugador1.cartas, jugador2.cartas)) {
                1 -> "${jugador1.nombre} gana con $jugada1"
                2 -> "${jugador2.nombre} gana con $jugada2"
                else -> "¡Empate perfecto!"
            }
        }
    }
}