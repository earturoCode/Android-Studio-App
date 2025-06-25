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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.gamesapplication.comons.ButtonWithText
import com.example.gamesapplication.ui.theme.GamesApplicationTheme
import com.example.gamesapplication.viewModels.TocameViewModel
import kotlin.random.Random

@Composable
fun TocameInterace(navController: NavHostController,tocameViewModel: TocameViewModel= viewModel()) {

    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(Modifier.height(40.dp))
        RowNameScoreTimer(tocameViewModel)
        ContainerTocame(tocameViewModel)
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
            navController.navigate("scoreboard")
        }
    }
}
@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun ContainerTocame(tocameViewModel: TocameViewModel) {
    val density = LocalDensity.current
    BoxWithConstraints(
        modifier = Modifier.fillMaxWidth()
            .height(LocalConfiguration.current.screenHeightDp.dp - 120.dp)
            .background(MaterialTheme.colorScheme.secondary)
    ) {
        LaunchedEffect(Unit) {
            tocameViewModel.maxSize.value = maxWidth to maxHeight
            tocameViewModel.boxPosition = returnOffsetValues(tocameViewModel, density)
            Log.d(
                "TOCAME",
                "Asigno tamaño: $maxWidth x $maxHeight y posición: ${tocameViewModel.boxPosition}"
            )

        }
        if (tocameViewModel.isTimerActive) {
            Box(modifier = Modifier.offset(tocameViewModel.boxPosition.first,
                    tocameViewModel.boxPosition.second)
                    .clip(CircleShape)
                    .background(color = MaterialTheme.colorScheme.tertiary).size(40.dp)
                    .clickable {
                        Log.d("TOCAME", "Clickeo")
                        tocameViewModel.incrementarPuntaje()
                        tocameViewModel.boxPosition =
                            returnOffsetValues(tocameViewModel, density)
                    }
            )
        }
    }
}
fun returnOffsetValues(tocameViewModel : TocameViewModel,density: Density):Pair<Dp,Dp> {
    val maxWidthPx = with(density) { tocameViewModel.maxSize.value.first.toPx() }
    val maxHeightPx = with(density) { tocameViewModel.maxSize.value.second.toPx() }
    val boxSize = with(density) { 40.dp.toPx() }
    val offsetXPx = Random.nextInt(maxWidthPx.toInt() - boxSize.toInt())
    val offsetYPx = Random.nextInt(maxHeightPx.toInt() - boxSize.toInt())
    return with(density) { offsetXPx.toDp() } to with(density) { offsetYPx.toDp() }
}
@Composable
fun RowNameScoreTimer(tocameViewModel: TocameViewModel) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "Name")
        Text(
            text = "Puntaje: ${tocameViewModel.puntaje}",
            modifier = Modifier.align(Alignment.CenterVertically)
        )
        Text(text = tocameViewModel.timer.toString())
    }
}



@Preview(showBackground = true)
@Composable
fun TocamePreview() {
    GamesApplicationTheme {
        TocameInterace(rememberNavController())
    }
}



