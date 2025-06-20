package com.example.gamesapplication.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TocameViewModel : ViewModel() {
    var puntaje by mutableStateOf(0)
        private set

    var timer by mutableStateOf(30)
        private set

    var isTimerActive by mutableStateOf(false)
        private set
    var maxWidth = 0.dp
    var maxHeight = 0.dp
    var boxPosition by mutableStateOf(Pair(0.dp,0.dp))

    fun incrementarPuntaje() {
        puntaje++
    }

    fun reiniciarJuego() {
        puntaje = 0
        timer = 30
        isTimerActive = false
    }

    fun iniciarTimer() {
        if (isTimerActive) return
        isTimerActive = true

        CoroutineScope(Dispatchers.Main).launch {
            while (timer > 0) {
                delay(1000)
                timer--
            }
            timer = 30
            isTimerActive = false
        }
    }
}