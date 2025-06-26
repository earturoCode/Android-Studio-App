package com.example.gamesapplication.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.gamesapplication.models.Score
import com.example.gamesapplication.models.UserSession
import com.example.gamesapplication.viewModels.ScoresViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScoreboardScreen(
    viewModel: ScoresViewModel = viewModel()
) {
    val isLoading by viewModel.isLoading.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()

    var selectedFilter by remember { mutableStateOf("Top 10") }
    val filterTypes = listOf("Top 10", "Top 5", "Mis Puntajes")

    // Obtener puntajes filtrados según la selección
    val scores by viewModel.scores.collectAsState()
    val filteredScores = viewModel.getFilteredScores(selectedFilter)


    // Obtener el nombre real del usuario desde el login
    LaunchedEffect(Unit) {
        viewModel.setCurrentUser(   UserSession.getCurrentUserName()) // Usar el usuario real
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Título y botón de regreso
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "Tabla de Puntajes",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

//            // Espacio vacío para balancear el diseño
//            Spacer (modifier = Modifier.weight(0.1f))
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (selectedFilter == "Mis Puntajes") {
            Text(
                text = "Puntajes de: $currentUser",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        // Filtros por tipo
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            filterTypes.forEach { filterType ->
                FilterChip(
                    onClick = { selectedFilter = filterType },
                    label = { Text(filterType) },
                    selected = selectedFilter == filterType,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (filteredScores.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = when (selectedFilter) {
                            "Mis Puntajes" -> "No tienes puntajes registrados"
                            else -> "No hay puntajes registrados"
                        },
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Gray
                    )
                    if (selectedFilter == "Mis Puntajes") {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "¡Empieza a jugar para ver tus puntajes aquí!",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                    }
                }
            }
        } else {
            // Mostrar resultados
            Text(
                text = when (selectedFilter) {
                    "Top 10" -> "Mostrando top ${filteredScores.size} puntajes"
                    "Top 5" -> "Mostrando top ${filteredScores.size} puntajes"
                    "Mis Puntajes" -> "${filteredScores.size} puntaje(s) encontrado(s)"
                    else -> "${filteredScores.size} resultados"
                },
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Encabezado de la tabla
            ScoreTableHeader()

            Spacer(modifier = Modifier.height(8.dp))

            // Lista de puntajes
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(filteredScores) { index, score ->
                    ScoreItem(
                        position = index + 1,
                        score = score,
                        isCurrentUser = score.playerName == currentUser
                    )
                }
            }
        }
    }
}

@Composable
fun ScoreTableHeader() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Top",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(0.5f),
                textAlign = TextAlign.Center
            )
            Text(
                text = "Jugador",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(2f)
            )
            Text(
                text = "Puntaje",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.End
            )
            Text(
                text = "Fecha",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1.5f),
                textAlign = TextAlign.End
            )
        }
    }
}

@Composable
fun ScoreItem(
    position: Int,
    score: Score,
    isCurrentUser: Boolean = false,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isCurrentUser) {
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
            } else {
                MaterialTheme.colorScheme.surface
            }
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Nombre del jugador
            Row(
                modifier = Modifier.weight(2f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = score.playerName,
                    fontSize = 16.sp,
                    fontWeight = if (position <= 3 || isCurrentUser) FontWeight.Bold else FontWeight.Normal,
                    color = if (isCurrentUser) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    }
                )
                if (isCurrentUser) {
                    Text(
                        text = " (Tú)",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Puntaje
            Text(
                text = "${score.points}",
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.End,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            // Fecha
            Text(
                text = score.date,
                modifier = Modifier.weight(1.5f),
                textAlign = TextAlign.End,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}