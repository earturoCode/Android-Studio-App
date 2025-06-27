package com.example.gamesapplication.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.gamesapplication.comons.ButtonWithText
import com.example.gamesapplication.navigation.Routes
import com.example.gamesapplication.ui.theme.GamesApplicationTheme
import com.example.gamesapplication.viewModels.TocameViewModel
import kotlin.random.Random

@Composable
fun TocameInterface(navController: NavHostController, tocameViewModel: TocameViewModel= viewModel()) {
    LaunchedEffect (Unit) { tocameViewModel.updateName() }
    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Spacer(Modifier.height(40.dp))
        RowNameScoreTimer(tocameViewModel)
        Spacer(Modifier.height(12.dp))
        ShowScoreAlert(tocameViewModel)
        ContainerTocame(tocameViewModel)
        Spacer(Modifier.height(30.dp))
        RowButtons(tocameViewModel, navController)

    }
}

@Composable
fun RowButtons(tocameViewModel: TocameViewModel, navController: NavHostController) {
    Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
        ButtonWithText("Jugar") {
            if (!tocameViewModel.isTimerActive) tocameViewModel.iniciarTimer()
        }
        ButtonWithText("Puntajes") {
            navController.navigate("${Routes.SCOREBOARD}/Top 5")
        }
    }
}
@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun ContainerTocame(tocameViewModel: TocameViewModel) {
    val density = LocalDensity.current
    BoxWithConstraints(
        modifier = Modifier.fillMaxWidth()
            .height(LocalConfiguration.current.screenHeightDp.dp - 200.dp)
            .background(MaterialTheme.colorScheme.secondary)
    ) {
        LaunchedEffect(Unit) {
            tocameViewModel.updateMaxSize(maxWidth to maxHeight)
            tocameViewModel.updateBoxPosition(returnOffsetValues(tocameViewModel, density))
        }
        if (tocameViewModel.isTimerActive) {
            Box(modifier = Modifier.offset(tocameViewModel.boxPosition.first,
                    tocameViewModel.boxPosition.second)
                    .clip(CircleShape)
                    .background(color = MaterialTheme.colorScheme.onSecondary).size(60.dp)
                    .clickable {
                        Log.d("TOCAME", "Clickeo")
                        tocameViewModel.incrementarPuntaje()
                        tocameViewModel.updateBoxPosition(returnOffsetValues(tocameViewModel, density))
                    }
            )
        }
    }
}
fun returnOffsetValues(tocameViewModel : TocameViewModel,density: Density):Pair<Dp,Dp> {
    val maxWidthPx = with(density) { tocameViewModel.maxSize.first.toPx() }
    val maxHeightPx = with(density) { tocameViewModel.maxSize.second.toPx() }
    val boxSize = with(density) { 60.dp.toPx() }
    val offsetXPx = Random.nextInt(maxWidthPx.toInt() - boxSize.toInt())
    val offsetYPx = Random.nextInt(maxHeightPx.toInt() - boxSize.toInt())
    return with(density) { offsetXPx.toDp() } to with(density) { offsetYPx.toDp() }
}
@Composable
fun RowNameScoreTimer(tocameViewModel: TocameViewModel) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = tocameViewModel.name, color= MaterialTheme.colorScheme.onBackground, fontSize = 24.sp)
        Text(
            text = "Puntaje: ${tocameViewModel.puntaje}", fontSize = 24.sp,color=MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
        Text(text = tocameViewModel.timer.toString(), fontSize = 24.sp, color= MaterialTheme.colorScheme.onBackground)
    }
}
@Composable
fun ShowScoreAlert(tocameViewModel: TocameViewModel){
    if (tocameViewModel.showScoreAlert) {
        val onDismiss = {tocameViewModel.updateScoreAlert()}
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text(text = "Enhorabuena")
            },
            text = {
                Text(tocameViewModel.scoreAlertText)
            },
            confirmButton = {
                TextButton(onClick = onDismiss) {
                    Text("Aceptar")
                }
            }
        )
    }
}
@Preview(showBackground = true)
@Composable
fun TocamePreview() {
    GamesApplicationTheme {
        TocameInterface(rememberNavController())
    }
}



