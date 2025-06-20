package com.example.gamesapplication.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
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
fun TocameInterace(navController: NavHostController,tocameViewModel: TocameViewModel= viewModel()){
        Column (modifier= Modifier.fillMaxSize()) {
        Spacer(Modifier.height(40.dp))
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val text = Text(text = "Name")
            Text(text = "Puntaje: ${tocameViewModel.puntaje}",modifier=Modifier.align(Alignment.CenterVertically))
            Text(text = tocameViewModel.timer.toString())
        }
        BoxWithConstraints (Modifier.fillMaxWidth().height(LocalConfiguration.current.screenHeightDp.dp-120.dp).
        background(MaterialTheme.colorScheme.secondary)){
            tocameViewModel.maxWidth = maxWidth
            tocameViewModel.maxHeight = maxHeight
            tocameViewModel.boxPosition = ReturnOffsetValues(tocameViewModel)
            Box(modifier=Modifier.background(color = Color.Blue).width(40.dp).height(40.dp).
            clip(CircleShape).offset(tocameViewModel.boxPosition.first,tocameViewModel.boxPosition.second))
        }
        Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()){
            ButtonWithText ("Jugar",{if(!tocameViewModel.isTimerActive) tocameViewModel.iniciarTimer()})
            ButtonWithText ("Puntajes",{})
        }
    }
}
@Composable
fun ReturnOffsetValues(tocameViewModel : TocameViewModel):Pair<Dp,Dp>{
    val localDensity = LocalDensity.current
    val maxWidthPx=with(localDensity) {tocameViewModel.maxWidth.toPx() }
    val maxHeightPx=with(localDensity) {tocameViewModel.maxHeight.toPx() }
    val boxSize = with(localDensity){40.dp.toPx()}
    val offsetXPx = Random.nextInt(maxWidthPx.toInt()-boxSize.toInt())
    val offsetYPx = Random.nextInt(maxHeightPx.toInt()-boxSize.toInt())
    return with(localDensity){offsetXPx.toDp()} to with(localDensity){offsetYPx.toDp()}
}
@Preview(showBackground = true)
@Composable
fun TocamePreview() {
    GamesApplicationTheme {
        TocameInterace(rememberNavController())
    }
}



