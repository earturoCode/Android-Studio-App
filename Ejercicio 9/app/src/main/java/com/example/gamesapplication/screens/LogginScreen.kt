package com.example.gamesapplication.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.gamesapplication.navigation.Routes
import com.example.gamesapplication.R
import com.example.gamesapplication.RetrofitInstance
import com.example.gamesapplication.comons.ButtonWithText
import com.example.gamesapplication.comons.GenerateImage
import com.example.gamesapplication.comons.TextFieldWithPlaceHolder
import com.example.gamesapplication.models.LoginRequest
import com.example.gamesapplication.ui.theme.GamesApplicationTheme
import kotlinx.coroutines.launch

    @Composable
    fun LoginInterface(navController: NavHostController) {
        var username by rememberSaveable { mutableStateOf("") }
        var password by rememberSaveable { mutableStateOf("") }
        Surface(color = MaterialTheme.colorScheme.background, modifier = Modifier.fillMaxSize()) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(140.dp))
                GenerateImage(Modifier.size(200.dp).clip(CircleShape),R.drawable.poker_image,ContentScale.Crop)
                Spacer(Modifier.height(40.dp))
                TextFieldWithPlaceHolder("Nombre de usuario", username, { username = it })
                TextFieldWithPlaceHolder("Contraseña", password, { password = it },PasswordVisualTransformation())
                IniciarSesionButton("Iniciar Sesion", username, password, navController)
                ButtonWithText("Registrarse",{})
            }
        }
    }

    @Composable
    fun IniciarSesionButton(text: String, username: String, password: String,navController:NavHostController) {
        val coroutineScope = rememberCoroutineScope()

        Button(
            onClick = { coroutineScope.launch { sendLoginRequest(username, password,navController) } },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            modifier = Modifier.width(160.dp)
        ) {
            Text(text)
        }
    }

    suspend fun sendLoginRequest(username: String, password: String,navController: NavHostController) {
        val loginRequest = LoginRequest(
            email = username,
            password = password
        )
        try {
            val response = RetrofitInstance.api.login(loginRequest)
            if (response.isSuccessful) {
                val loginResponse = response.body()
                navController.navigate(Routes.HOME)
                Log.d("LOGIN", "Token: ${loginResponse?.access_token}")
                Log.d("LOGIN", "id: ${loginResponse?.user?.id}")
            } else {
                Log.e("LOGIN", "Error: ${response.code()} ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            Log.e("LOGIN", "Excepción: ${e.localizedMessage}")
        }
    }
    @Preview(showBackground = true)
    @Composable
    fun LogginPreview() {
        GamesApplicationTheme {
            LoginInterface(rememberNavController())
        }
    }
