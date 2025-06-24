package com.example.gamesapplication.viewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.example.gamesapplication.RetrofitInstance
import com.example.gamesapplication.dataStore.UserPreferencesManager
import com.example.gamesapplication.models.CreateScoreRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class TocameViewModel : ViewModel() {
    var puntaje by mutableStateOf(0)
        private set

    var timer by mutableStateOf(30)
        private set

    var isTimerActive by mutableStateOf(false)
        private set
    var maxSize = mutableStateOf(0.dp to 0.dp)
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
            sendCreateScoreRequest()
            isTimerActive = false
            puntaje = 0
        }
    }
    suspend fun sendCreateScoreRequest() {
        val userPreferences=UserPreferencesManager.get()
        val currentDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy")
        val formattedDate = currentDate.format(formatter)
        Log.e("LOGIN", "Exitoso: ${userPreferences.userData.first().first}${userPreferences.userData.first().second}")
        val createScoreRequest = CreateScoreRequest(
            user_id = userPreferences.userData.first().first?: "",
            game_id = "1",
            score = puntaje,
            date = formattedDate.toString()
        )
        try {
            val response = RetrofitInstance.restApi.createScore(
                "Bearer ${userPreferences.userData.first().second?: ""}",createScoreRequest)
            if (response.isSuccessful) {
                Log.e("LOGIN", "Exitoso: ${response.code()}")
            }
            else {
                Log.e("LOGIN", "Error: ${response.code()} ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            Log.e("LOGIN", "Excepción: ${e.localizedMessage}")
        }
    }
}