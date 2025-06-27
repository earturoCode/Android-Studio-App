package com.example.gamesapplication.viewModels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.example.gamesapplication.navigation.Routes
import androidx.compose.runtime.*
import com.example.gamesapplication.caseOfUser.AuthCaseOfUse

class LoginViewModel(private val authCaseOfUse: AuthCaseOfUse=AuthCaseOfUse()) : ViewModel() {

    var username by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set
    var showDialog by mutableStateOf(false)
        private set
    var dialogText by mutableStateOf("")
        private set

    fun updateDialogState(text:String){
        showDialog=!showDialog
        dialogText=text
    }

    fun updateDialogState(){
        showDialog=!showDialog
    }

    private fun clearUsernameAndPassword(){
        username=""
        password=""
    }

    fun updateUsername(newUsername:String){
        username = newUsername
    }
    fun updatePassword(newPassword:String){
        password=newPassword
    }

    suspend fun sendLoginRequest():String {
        val textToReturn = authCaseOfUse.loginCaseOfUse(username,password)
        if (textToReturn=="Se ha loggeado correctamente") clearUsernameAndPassword()
        return textToReturn
    }
}