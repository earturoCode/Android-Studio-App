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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
    LaunchedEffect (Unit) { tocameViewModel.updateName() }
    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(Modifier.height(40.dp))
        RowNameScoreTimer(tocameViewModel)
        ContainerTocame(tocameViewModel)
        Spacer(Modifier.height(10.dp))
        RowButtons(tocameViewModel)
    }
}

@Composable
fun RowButtons(tocameViewModel: TocameViewModel) {
    Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
        ButtonWithText("Jugar") { if (!tocameViewModel.isTimerActive) tocameViewModel.iniciarTimer() }
        ButtonWithText("Puntajes") {}
    }
}
@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun ContainerTocame(tocameViewModel: TocameViewModel) {
    val density = LocalDensity.current
    BoxWithConstraints(
        modifier = Modifier.fillMaxWidth()
            .height(LocalConfiguration.current.screenHeightDp.dp - 160.dp)
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
                    .background(color = MaterialTheme.colorScheme.tertiary).size(60.dp)
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
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = tocameViewModel.name)
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



