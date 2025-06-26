package com.example.gamesapplication.viewModels

import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.example.gamesapplication.constants.Constants
import com.example.gamesapplication.dataStore.UserPreferencesManager
import kotlinx.coroutines.flow.first

class HomeViewModel: ViewModel() {
    var name by mutableStateOf("")
        private set
    var isAyuda by mutableStateOf(false)
        private set
    var textAyudaHeight by mutableStateOf(0.dp)
        private set
    lateinit var pagerState : PagerState
        private set
    fun updatePagerState(newPagerState:PagerState){
        pagerState = newPagerState
    }
    fun updateTextAyudaHeight(newAyudaHeight: Dp){
        textAyudaHeight = newAyudaHeight
    }
    fun changeAyudaState(){
        isAyuda=!isAyuda
    }
    fun returnAyudaText()= when {
        pagerState.currentPage == 0 && isAyuda -> Constants.ayudaPoker
        pagerState.currentPage == 1 && isAyuda -> Constants.ayudaTocame
        else -> ""
    }
    suspend fun setNameFromDataStore() {
        name = UserPreferencesManager.get().userData.first().third?:""
    }
}