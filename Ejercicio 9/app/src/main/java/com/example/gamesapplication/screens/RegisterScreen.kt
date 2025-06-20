package com.example.minigames.ui.register.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gamesapplication.viewModels.RegisterViewModel
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(viewModel: RegisterViewModel, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Register(Modifier.align(Alignment.Center), viewModel)
    }
}

@Composable
fun Register(modifier: Modifier, viewModel: RegisterViewModel) {

    val name: String by viewModel.name.observeAsState(initial = "")
    val email: String by viewModel.email.observeAsState(initial = "")
    val password: String by viewModel.password.observeAsState(initial = "")
    val confirmPassword: String by viewModel.confirmPassword.observeAsState(initial = "")
    val registerEnable: Boolean by viewModel.registerEnable.observeAsState(initial = false)
    val isLoading: Boolean by viewModel.isLoading.observeAsState(initial = false)
    val coroutineScope = rememberCoroutineScope()

    if (isLoading) {
        Box(Modifier.fillMaxSize()) {
            CircularProgressIndicator(Modifier.align(Alignment.Center))
        }
    } else {
        Column(modifier = modifier) {
            Text(
                text = "¡Vamos a crear tu Cuenta!  ",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF636262),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.padding(16.dp))

            Text(
                text = "Es el momento de crear tu cuenta para acceder a increibles juegos!",
                fontSize = 14.sp,
                color = Color(0xFF888888),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.padding(16.dp))

            // Campo Nombre
            NameField(name) {
                viewModel.onRegisterChanged(it, email, password, confirmPassword)
            }
            Spacer(modifier = Modifier.padding(4.dp))

            // Campo Email
            EmailField(email) {
                viewModel.onRegisterChanged(name, it, password, confirmPassword)
            }
            Spacer(modifier = Modifier.padding(4.dp))

            // Campo Contraseña
            PasswordField(password) {
                viewModel.onRegisterChanged(name, email, it, confirmPassword)
            }
            Spacer(modifier = Modifier.padding(4.dp))

            // Campo Confirmar Contraseña
            ConfirmPasswordField(confirmPassword) {
                viewModel.onRegisterChanged(name, email, password, it)
            }
            Spacer(modifier = Modifier.padding(16.dp))

            // Botón Registrar
            RegisterButton(registerEnable) {
                coroutineScope.launch {
                    viewModel.onRegisterSelected()
                }
            }
            Spacer(modifier = Modifier.padding(8.dp))

            // Link para ir al login
            GoToLogin(Modifier.align(Alignment.CenterHorizontally))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NameField(name: String, onTextFieldChanged: (String) -> Unit) {
    OutlinedTextField(
        value = name,
        onValueChange = { onTextFieldChanged(it) },
        placeholder = { Text(text = "Nombre completo") },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color(0xFF636262),
            unfocusedTextColor = Color(0xFF636262),
            focusedContainerColor = Color(0xFFDEDDDD),
            unfocusedContainerColor = Color(0xFFDEDDDD),
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmailField(email: String, onTextFieldChanged: (String) -> Unit) {
    OutlinedTextField(
        value = email,
        onValueChange = { onTextFieldChanged(it) },
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(text = "Email") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color(0xFF636262),
            unfocusedTextColor = Color(0xFF636262),
            focusedContainerColor = Color(0xFFDEDDDD),
            unfocusedContainerColor = Color(0xFFDEDDDD),
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordField(password: String, onTextFieldChanged: (String) -> Unit) {
    OutlinedTextField(
        value = password,
        onValueChange = { onTextFieldChanged(it) },
        placeholder = { Text(text = "Contraseña") },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation = PasswordVisualTransformation(),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color(0xFF636262),
            unfocusedTextColor = Color(0xFF636262),
            focusedContainerColor = Color(0xFFDEDDDD),
            unfocusedContainerColor = Color(0xFFDEDDDD),
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmPasswordField(confirmPassword: String, onTextFieldChanged: (String) -> Unit) {
    OutlinedTextField(
        value = confirmPassword,
        onValueChange = { onTextFieldChanged(it) },
        placeholder = { Text(text = "Confirmar contraseña") },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation = PasswordVisualTransformation(),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color(0xFF636262),
            unfocusedTextColor = Color(0xFF636262),
            focusedContainerColor = Color(0xFFDEDDDD),
            unfocusedContainerColor = Color(0xFFDEDDDD),
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent
        )
    )
}

@Composable
fun RegisterButton(registerEnable: Boolean, onRegisterSelected: () -> Unit) {
    Button(
        onClick = { onRegisterSelected() },
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFFF4303),
            disabledContainerColor = Color(0xFFF78058),
            contentColor = Color.White,
            disabledContentColor = Color.White
        ),
        enabled = registerEnable
    ) {
        Text(text = "Crear Cuenta")
    }
}

@Composable
fun GoToLogin(modifier: Modifier) {
    Row(modifier = modifier) {
        Text(
            text = "¿Ya tienes cuenta? ",
            fontSize = 12.sp,
            color = Color(0xFF636262)
        )
        Text(
            text = "Inicia sesión",
            modifier = Modifier.clickable { /* Navegar a login */ },
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFFB9600)
        )
    }
}