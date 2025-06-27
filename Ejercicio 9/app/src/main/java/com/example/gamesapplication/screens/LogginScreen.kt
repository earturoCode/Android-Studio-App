package com.example.gamesapplication.screens


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.gamesapplication.R
import com.example.gamesapplication.comons.ButtonWithText
import com.example.gamesapplication.navigation.Routes
import com.example.gamesapplication.ui.theme.GamesApplicationTheme
import com.example.gamesapplication.viewModels.LoginViewModel
import kotlinx.coroutines.launch

@Composable
fun LoginInterface(navController: NavHostController, loginViewModel: LoginViewModel= viewModel()) {
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

            TextFieldWithPlaceHolder("Correo electronico", loginViewModel.username ) { loginViewModel.updateUsername(it) }
            Spacer(Modifier.height(16.dp))
            ShowAuthAlert(loginViewModel)
            TextFieldWithPlaceHolder("Contraseña", loginViewModel.password, isPassword = true) { loginViewModel.updatePassword(it) }
            Spacer(Modifier.height(24.dp))

            IniciarSesionButton("Iniciar Sesión", loginViewModel , navController)
            Spacer(Modifier.height(16.dp))

            RegisterButton("Registrarse", navController)
            Spacer(Modifier.height(200.dp))
            ButtonWithText("Top 10") {
                navController.navigate("${Routes.SCOREBOARD}/Top 10")
            }
        }
    }
}
@Composable
fun ShowAuthAlert(loginViewModel: LoginViewModel){
    if (loginViewModel.showDialog) {
        val onDismiss = {loginViewModel.updateDialogState()}
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text(text = "Alerta")
            },
            text = {
                Text(loginViewModel.dialogText)
            },
            confirmButton = {
                TextButton(onClick = onDismiss) {
                    Text("Aceptar")
                }
            }
        )
    }
}
@Composable
fun IniciarSesionButton(
    text: String,
    loginViewModel: LoginViewModel,
    navController: NavHostController) {
    val coroutineScope = rememberCoroutineScope()
    Button(
        onClick = {
            coroutineScope.launch {
                if(loginViewModel.username.isEmpty()){ loginViewModel.updateDialogState("Debe introducir su correo electronico")}
                else if (loginViewModel.password.isEmpty()) {loginViewModel.updateDialogState("Debe introducir su Password")}
                else {
                    val response=loginViewModel.sendLoginRequest()
                    if (response=="Se ha loggeado correctamente") navController.navigate(Routes.HOME)
                    else loginViewModel.updateDialogState(response)
                }
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
        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.primary),
        modifier = Modifier.width(200.dp)
    ) {
        Text(text)
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