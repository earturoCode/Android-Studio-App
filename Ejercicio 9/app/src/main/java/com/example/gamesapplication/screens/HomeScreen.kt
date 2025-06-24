package com.example.gamesapplication.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.gamesapplication.R
import com.example.gamesapplication.navigation.Routes
import com.example.gamesapplication.ui.theme.GamesApplicationTheme

@Composable
fun HomeInterace(navController: NavHostController) {
    val context = LocalContext.current
    var nombreUsuario by remember { mutableStateOf("Jugador") }

    // Obtener el nombre del usuario guardado
    LaunchedEffect(Unit) {
        val sharedPrefs = context.getSharedPreferences("user_prefs", android.content.Context.MODE_PRIVATE)
        nombreUsuario = sharedPrefs.getString("username", "Jugador") ?: "Jugador"
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Spacer(Modifier.height(60.dp))

        // Título de bienvenida
        Text(
            text = "🎮 Bienvenido, $nombreUsuario",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = "SELECCIONA UN JUEGO",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(Modifier.height(20.dp))

        // Carrusel de juegos
        HorizontalPagerGames(navController, nombreUsuario)

        Spacer(Modifier.height(40.dp))

        // Botón para Poker
        Button(
            onClick = {
                navController.navigate("poker/$nombreUsuario")
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF0F5132), // Verde poker
                contentColor = Color.White
            ),
            modifier = Modifier
                .width(220.dp)
                .height(55.dp)
        ) {
            Text(
                text = "🃏 JUGAR POKER",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.height(20.dp))
    }
}

@Composable
fun HorizontalPagerGames(navController: NavHostController, nombreUsuario: String) {
    val pagerState = rememberPagerState(0, pageCount = { 2 })

    HorizontalPager(
        state = pagerState,
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    ) { page ->
        Image(
            painter = painterResource(
                id = if (page == 0) R.drawable.poker_image else R.drawable.tocame_image
            ),
            contentDescription = if (page == 0) "Poker Game" else "Tocame Game",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .clip(CircleShape)
                .fillMaxHeight()
                .clickable {
                    when (page) {
                        0 -> {
                            // Navegar a Poker
                            navController.navigate("poker/$nombreUsuario")
                        }
                        1 -> {
                            // Aquí tu compañero puede agregar navegación para Tocame
                            // navController.navigate("tocame/$nombreUsuario")
                        }
                    }
                }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    GamesApplicationTheme {
        HomeInterace(rememberNavController())
    }
}