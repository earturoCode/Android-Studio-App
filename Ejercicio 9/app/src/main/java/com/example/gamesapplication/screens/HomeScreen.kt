package com.example.gamesapplication.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
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
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.gamesapplication.R
import com.example.gamesapplication.comons.ButtonWithText
import com.example.gamesapplication.comons.GenerateImage
import com.example.gamesapplication.navigation.Routes
import com.example.gamesapplication.ui.theme.GamesApplicationTheme
import com.example.gamesapplication.viewModels.HomeViewModel


@Composable
fun HomeInterface(navController: NavHostController, homeViewModel: HomeViewModel=viewModel()){
    LaunchedEffect (Unit){
        homeViewModel.setNameFromDataStore()
    }
    homeViewModel.updatePagerState(rememberPagerState (0, pageCount = {2}))
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize().background(
        MaterialTheme.colorScheme.background)) {
        val density = LocalDensity.current
        Spacer(Modifier.height(80.dp))
        ButtonWithText(text = "Cerrar sesion") {navController.navigate(Routes.LOGIN) }
        Text(text="Bienvenido: ${homeViewModel.name}")
        Spacer(modifier= Modifier.height(120.dp))
        HorizontalPagerGames(homeViewModel)
        Spacer(Modifier.height(32.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            ButtonWithText("Jugar") {
                if (homeViewModel.pagerState.currentPage == 0) navController.navigate(
                    Routes.POKER
                ) else navController.navigate(Routes.TOCAME)
            }
            ButtonWithText("Puntajes") {
                navController.navigate("${Routes.SCOREBOARD}/Mis Puntajes")
            }        }
        Spacer(Modifier.height(240.dp-homeViewModel.textAyudaHeight))
        ButtonWithText(text="Ayuda") { homeViewModel.changeAyudaState() }
        Text(text = homeViewModel.returnAyudaText(), textAlign = TextAlign.Justify, modifier = Modifier.padding(horizontal = 8.dp).onGloballyPositioned{
            layoutCoordinates -> homeViewModel.updateTextAyudaHeight(with(density){layoutCoordinates.size.height.toDp()})
        })
    }
}

@Composable
fun HorizontalPagerGames(homeViewModel:HomeViewModel){
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        HorizontalPager(
            state = homeViewModel.pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) { page ->
            Box(modifier = Modifier.fillMaxSize()) {
                GenerateImage(
                    Modifier
                        .clip(CircleShape)
                        .fillMaxHeight()
                        .align(Alignment.Center),
                    if (page == 0) R.drawable.poker_image
                    else R.drawable.tocame_image, ContentScale.Crop
                )
            }
        }
        Row(verticalAlignment = Alignment.CenterVertically, modifier=Modifier.padding(top = 8.dp)){
            BoxForPager(if(homeViewModel.pagerState.currentPage==0)Color.Blue else Color.Gray )
            Spacer(Modifier.width(8.dp))
            BoxForPager(if(homeViewModel.pagerState.currentPage==1)Color.Blue else Color.Gray )
        }
    }
}
@Composable
fun BoxForPager(color: Color){
    Box(Modifier
        .clip(CircleShape)
        .size(8.dp)
        .background(color)){}
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    GamesApplicationTheme {
        HomeInterface(rememberNavController())
    }
}