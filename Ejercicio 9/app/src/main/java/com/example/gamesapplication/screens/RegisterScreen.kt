package com.example.minigames.ui.register.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import androidx.navigation.NavController
import com.example.gamesapplication.navigation.Routes
import kotlinx.coroutines.delay

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Register(Modifier.align(Alignment.Center), viewModel, navController)
    }
}

@Composable
fun Register(modifier: Modifier, viewModel: RegisterViewModel, navController: NavController) {

    // Estados observados del ViewModel
    val name: String by viewModel.name.observeAsState(initial = "")
    val email: String by viewModel.email.observeAsState(initial = "")
    val password: String by viewModel.password.observeAsState(initial = "")
    val confirmPassword: String by viewModel.confirmPassword.observeAsState(initial = "")
    val registerEnable: Boolean by viewModel.registerEnable.observeAsState(initial = false)
    val isLoading: Boolean by viewModel.isLoading.observeAsState(initial = false)
    val registerSuccess: Boolean? by viewModel.registerSuccess.observeAsState()

    // Estados de errores
    val nameError: String? by viewModel.nameError.observeAsState()
    val emailError: String? by viewModel.emailError.observeAsState()
    val passwordError: String? by viewModel.passwordError.observeAsState()
    val confirmPasswordError: String? by viewModel.confirmPasswordError.observeAsState()
    val generalError: String? by viewModel.generalError.observeAsState()

    // Navegación automática cuando el registro es exitoso
    LaunchedEffect(registerSuccess) {
        if (registerSuccess == true) {
            delay(1000) // Espera 1 segundo para mostrar éxito
            navController.navigate(Routes.LOGIN)
        }
    }

    // Mostrar indicador de carga mientras se procesa el registro
    if (isLoading) {
        Box(Modifier.fillMaxSize()) {
            CircularProgressIndicator(Modifier.align(Alignment.Center))
        }
    } else {
        // Formulario principal
        Column(modifier = modifier) {

            // Título principal
            Text(
                text = "¡Vamos a crear tu Cuenta!",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF636262),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.padding(16.dp))

            // Subtítulo descriptivo
            Text(
                text = "Es el momento de crear tu cuenta para acceder a increíbles juegos!",
                fontSize = 14.sp,
                color = Color(0xFF888888),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.padding(16.dp))

            // Mostrar error general del servidor si existe
            generalError?.let { error ->
                Text(
                    text = error,
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    textAlign = TextAlign.Center
                )
            }

            // Campos del formulario
            NameField(name, nameError) {
                viewModel.onRegisterChanged(it, email, password, confirmPassword)
            }
            Spacer(modifier = Modifier.padding(4.dp))

            EmailField(email, emailError) {
                viewModel.onRegisterChanged(name, it, password, confirmPassword)
            }
            Spacer(modifier = Modifier.padding(4.dp))

            PasswordField(password, passwordError) {
                viewModel.onRegisterChanged(name, email, it, confirmPassword)
            }
            Spacer(modifier = Modifier.padding(4.dp))

            ConfirmPasswordField(confirmPassword, confirmPasswordError) {
                viewModel.onRegisterChanged(name, email, password, it)
            }
            Spacer(modifier = Modifier.padding(16.dp))

            // Botón de registro
            RegisterButton(registerEnable) {
                viewModel.onRegisterSelected()
            }
            Spacer(modifier = Modifier.padding(8.dp))

            // Link para navegar al login
            GoToLogin(Modifier.align(Alignment.CenterHorizontally)) {
                navController.navigate(Routes.LOGIN)
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NameField(name: String, error: String?, onTextFieldChanged: (String) -> Unit) {
    Column {
        OutlinedTextField(
            value = name,
            onValueChange = { onTextFieldChanged(it) },
            placeholder = { Text(text = "Nombre completo") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            singleLine = true,
            isError = error != null,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color(0xFF636262),
                unfocusedTextColor = Color(0xFF636262),
                focusedContainerColor = Color(0xFFDEDDDD),
                unfocusedContainerColor = Color(0xFFDEDDDD),
                focusedBorderColor = if (error != null) Color.Red else Color.Transparent,
                unfocusedBorderColor = if (error != null) Color.Red else Color.Transparent,
                errorBorderColor = Color.Red
            )
        )

        // Mensaje de error si existe
        error?.let {
            Text(
                text = it,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmailField(email: String, error: String?, onTextFieldChanged: (String) -> Unit) {
    Column {
        OutlinedTextField(
            value = email,
            onValueChange = { onTextFieldChanged(it) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(text = "Email") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true,
            isError = error != null,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color(0xFF636262),
                unfocusedTextColor = Color(0xFF636262),
                focusedContainerColor = Color(0xFFDEDDDD),
                unfocusedContainerColor = Color(0xFFDEDDDD),
                focusedBorderColor = if (error != null) Color.Red else Color.Transparent,
                unfocusedBorderColor = if (error != null) Color.Red else Color.Transparent,
                errorBorderColor = Color.Red
            )
        )

        // Mensaje de error si existe
        error?.let {
            Text(
                text = it,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordField(password: String, error: String?, onTextFieldChanged: (String) -> Unit) {
    Column {
        OutlinedTextField(
            value = password,
            onValueChange = { onTextFieldChanged(it) },
            placeholder = { Text(text = "Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            isError = error != null,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color(0xFF636262),
                unfocusedTextColor = Color(0xFF636262),
                focusedContainerColor = Color(0xFFDEDDDD),
                unfocusedContainerColor = Color(0xFFDEDDDD),
                focusedBorderColor = if (error != null) Color.Red else Color.Transparent,
                unfocusedBorderColor = if (error != null) Color.Red else Color.Transparent,
                errorBorderColor = Color.Red
            )
        )

        // Mensaje de error si existe
        error?.let {
            Text(
                text = it,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmPasswordField(
    confirmPassword: String,
    error: String?,
    onTextFieldChanged: (String) -> Unit
) {
    Column {
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { onTextFieldChanged(it) },
            placeholder = { Text(text = "Confirmar contraseña") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            isError = error != null,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color(0xFF636262),
                unfocusedTextColor = Color(0xFF636262),
                focusedContainerColor = Color(0xFFDEDDDD),
                unfocusedContainerColor = Color(0xFFDEDDDD),
                focusedBorderColor = if (error != null) Color.Red else Color.Transparent,
                unfocusedBorderColor = if (error != null) Color.Red else Color.Transparent,
                errorBorderColor = Color.Red
            )
        )

        // Mensaje de error si existe
        error?.let {
            Text(
                text = it,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
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
fun GoToLogin(modifier: Modifier, onNavigateToLogin: () -> Unit) {
    Row(modifier = modifier) {
        Text(
            text = "¿Ya tienes cuenta? ",
            fontSize = 12.sp,
            color = Color(0xFF636262)
        )
        Text(
            text = "Inicia sesión",
            modifier = Modifier.clickable { onNavigateToLogin() },
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFFB9600)
        )
    }
}