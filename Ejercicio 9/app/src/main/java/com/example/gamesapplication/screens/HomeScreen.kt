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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.gamesapplication.R
import com.example.gamesapplication.comons.ButtonWithText
import com.example.gamesapplication.comons.GenerateImage
import com.example.gamesapplication.navigation.Routes
import com.example.gamesapplication.ui.theme.GamesApplicationTheme


@Composable
fun HomeInterace(navController: NavHostController){
    Column {
        Spacer(Modifier.height(80.dp))
        HorizontalPagerGames(navController)
    }
}

@Composable
fun HorizontalPagerGames(navController: NavHostController){
    val pagerState = rememberPagerState( 0 , pageCount ={ 2 } )
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth().height(200.dp)
        ) { page ->
            Box(modifier = Modifier.fillMaxSize()) {
                GenerateImage(
                    Modifier.clip(CircleShape).fillMaxHeight().align(Alignment.Center),
                    if (page == 0) R.drawable.poker_image
                    else R.drawable.tocame_image, ContentScale.Crop
                )
            }
        }
        Row(verticalAlignment = Alignment.CenterVertically, modifier=Modifier.padding(top = 8.dp)){
            BoxForPager(if(pagerState.currentPage==0)Color.Blue else Color.Black )
            BoxForPager(if(pagerState.currentPage==1)Color.Blue else Color.Black )
        }
        Spacer(Modifier.height(32.dp))
        ButtonWithText("Jugar") {if (pagerState.currentPage==0)navController.navigate(Routes.POKER)else navController.navigate(Routes.TOCAME)}
        ButtonWithText("Puntajes") { }
    }
}
@Composable
fun BoxForPager(color: Color){
    Box(Modifier.clip(CircleShape).size(8.dp).background(color)){}
}


@Preview(showBackground = true)
@Composable
fun HomePreview() {
    GamesApplicationTheme {
        HomeInterace(rememberNavController())
    }
}