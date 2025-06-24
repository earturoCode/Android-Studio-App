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
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.gamesapplication.R
import com.example.gamesapplication.RetrofitInstance
import com.example.gamesapplication.models.LoginRequest
import com.example.gamesapplication.navigation.Routes
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
            Image(
                painter = painterResource(id = R.drawable.poker_image),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(200.dp)
                    .clip(CircleShape)
            )
            Spacer(Modifier.height(40.dp))

            // Campo de usuario
            TextFieldWithPlaceHolder("Nombre de usuario", username) { username = it }
            Spacer(Modifier.height(16.dp))

            // Campo de contraseña
            TextFieldWithPlaceHolder("Contraseña", password, isPassword = true) { password = it }
            Spacer(Modifier.height(24.dp))

            // Botón de iniciar sesión
            IniciarSesionButton("Iniciar Sesión", username, password, navController)
            Spacer(Modifier.height(16.dp))

            // Botón de registro
            RegisterButton("Registrarse", navController)
        }
    }
}

@Composable
fun IniciarSesionButton(
    text: String,
    username: String,
    password: String,
    navController: NavHostController
) {
    val coroutineScope = rememberCoroutineScope()

    Button(
        onClick = {
            coroutineScope.launch {
                sendLoginRequest(username, password, navController)
            }
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        modifier = Modifier.width(200.dp)
    ) {
        Text(text)
    }
}

@Composable
fun RegisterButton(text: String, navController: NavHostController) {
    OutlinedButton(
        onClick = {
            navController.navigate(Routes.REGISTER)
        },
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = MaterialTheme.colorScheme.primary
        ),
        modifier = Modifier.width(200.dp)
    ) {
        Text(text)
    }
}

suspend fun sendLoginRequest(username: String, password: String, navController: NavHostController) {
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

@Composable
fun TextFieldWithPlaceHolder(
    text: String,
    value: String,
    isPassword: Boolean = false,
    onValueChange: (String) -> Unit
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.width(280.dp),
        placeholder = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = text,
                textAlign = TextAlign.Center
            )
        },
        textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
        visualTransformation = if (isPassword) PasswordVisualTransformation() else androidx.compose.ui.text.input.VisualTransformation.None
    )
}

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    GamesApplicationTheme {
        LoginInterface(rememberNavController())
    }
}