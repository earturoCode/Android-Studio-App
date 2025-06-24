package com.example.gamesapplication.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.gamesapplication.R
import com.example.gamesapplication.comons.ButtonWithText
import com.example.gamesapplication.comons.GenerateImage
import com.example.gamesapplication.comons.TextFieldWithPlaceHolder
import com.example.gamesapplication.ui.theme.GamesApplicationTheme
import com.example.gamesapplication.viewModels.LoginViewModel
import kotlinx.coroutines.launch

    @Composable
    fun LoginInterface(navController: NavHostController,loginViewModel: LoginViewModel= viewModel()) {
        Surface(color = MaterialTheme.colorScheme.background, modifier = Modifier.fillMaxSize()) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(140.dp))
                GenerateImage(Modifier.size(200.dp).clip(CircleShape),R.drawable.poker_image,ContentScale.Crop)
                Spacer(Modifier.height(40.dp))
                TextFieldWithPlaceHolder("Nombre de usuario", loginViewModel.username.value, { loginViewModel.username.value = it })
                TextFieldWithPlaceHolder("Contraseña", loginViewModel.password.value, { loginViewModel.password.value = it },PasswordVisualTransformation())
                IniciarSesionButton(loginViewModel, navController)
                ButtonWithText("Registrarse"){}
            }
        }
    }

    @Composable
    fun IniciarSesionButton(loginViewModel: LoginViewModel,navController:NavHostController) {
        val coroutineScope = rememberCoroutineScope()

        Button(
            onClick = { coroutineScope.launch { loginViewModel.sendLoginRequest(navController) } },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            modifier = Modifier.width(160.dp)
        ) {
            Text("Iniciar Sesion")
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun LogginPreview() {
        GamesApplicationTheme {
            LoginInterface(rememberNavController())
        }
    }
