package com.example.gamesapplication.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.gamesapplication.models.Carta
import com.example.gamesapplication.models.Jugador
import com.example.gamesapplication.ui.theme.GamesApplicationTheme
import com.example.gamesapplication.viewModels.PokerViewModel

@Composable
fun PokerGameScreen(
    navController: NavHostController,
    nombreJugador: String = "Jugador"
) {
    val viewModel: PokerViewModel = viewModel()
    val gameState by viewModel.gameState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F5132))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Título del juego
        Text(
            text = "🃏 POKER GAME 🃏",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(16.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        when {
            !gameState.gameStarted -> {
                // Pantalla inicial
                PantallaInicial(
                    nombreJugador = nombreJugador,
                    onIniciarJuego = { viewModel.iniciarJuego(nombreJugador) }
                )
            }

            gameState.isLoading -> {
                // Pantalla de carga
                PantallaCarga()
            }

            else -> {
                // Pantalla del juego
                PantallaJuego(
                    gameState = gameState,
                    onJugarDeNuevo = { viewModel.jugarDeNuevo() },
                    onVolverAlMenu = { navController.popBackStack() }
                )
            }
        }
    }
}

@Composable
fun PantallaInicial(
    nombreJugador: String,
    onIniciarJuego: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "¡Bienvenido al Poker!",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "$nombreJugador vs CPU\nPrepárate para una batalla épica de cartas",
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = onIniciarJuego,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F5132))
            ) {
                Text(
                    text = "¡REPARTIR CARTAS!",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun PantallaCarga() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(50.dp),
                color = Color(0xFF0F5132)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Mezclando cartas...",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Repartiendo manos",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun PantallaJuego(
    gameState: com.example.gamesapplication.models.GameState,
    onJugarDeNuevo: () -> Unit,
    onVolverAlMenu: () -> Unit
) {
    // Jugador ChatGPT
    PlayerSection(
        jugador = gameState.jugador1,
        isWinner = gameState.resultado.contains(gameState.jugador1.nombre) &&
                !gameState.resultado.contains("ChatGPT")
    )

    Spacer(modifier = Modifier.height(20.dp))

    // Resultado
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = when {
                gameState.resultado.contains("Empate") -> Color(0xFFFFB74D)
                gameState.resultado.contains(gameState.jugador1.nombre) -> Color(0xFF4CAF50)
                else -> Color(0xFFE57373)
            }
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = gameState.resultado,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )

            // Mostrar tipos de jugadas
            if (gameState.jugador1.tipoJugada != null && gameState.jugador2.tipoJugada != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "${gameState.jugador1.nombre}: ${gameState.jugador1.tipoJugada}",
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.9f)
                )
                Text(
                    text = "ChatGPT: ${gameState.jugador2.tipoJugada}",
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.9f)
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(20.dp))

    // Jugador ChatGPT
    PlayerSection(
        jugador = gameState.jugador2,
        isWinner = gameState.resultado.contains("ChatGPT")
    )

    Spacer(modifier = Modifier.height(40.dp))


    // Botones de acción
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Button(
            onClick = onJugarDeNuevo,
            modifier = Modifier
                .weight(1f)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF6B35))
        ) {
            Text(
                text = "JUGAR DE NUEVO",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }

        OutlinedButton(
            onClick = onVolverAlMenu,
            modifier = Modifier
                .weight(1f)
                .height(50.dp),
            border = BorderStroke(2.dp, Color.White),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
        ) {
            Text(
                text = "HOME",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun PlayerSection(jugador: Jugador, isWinner: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isWinner) Color(0xFF81C784) else Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        border = if (isWinner) BorderStroke(3.dp, Color(0xFF4CAF50)) else null
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = jugador.nombre,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isWinner) Color.White else Color.Black
                )
                if (isWinner) {
                    Text(
                        text = "👑",
                        fontSize = 24.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = jugador.tipoJugada ?: "",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = if (isWinner) Color.White else Color(0xFF666666)
            )

            Spacer(modifier = Modifier.height(12.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(jugador.cartas) { carta ->
                    CartaComponent(carta = carta)
                }
            }
        }
    }
}

@Composable
fun CartaComponent(carta: Carta) {
    Card(
        modifier = Modifier
            .width(60.dp)
            .height(85.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        border = BorderStroke(1.dp, Color.Gray),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = carta.getDisplayValue(),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = carta.getSuitColor()
            )

            Text(
                text = carta.getSuitSymbol(),
                fontSize = 20.sp,
                color = carta.getSuitColor()
            )

            Text(
                text = carta.getDisplayValue(),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = carta.getSuitColor(),
                modifier = Modifier.rotate(180f)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PokerGamePreview() {
    GamesApplicationTheme {
        PokerGameScreen(rememberNavController(), "Jugador Test")
    }
}