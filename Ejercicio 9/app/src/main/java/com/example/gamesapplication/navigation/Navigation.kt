package com.example.gamesapplication.navigation


import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.gamesapplication.screens.HomeInterace
import com.example.gamesapplication.screens.LoginInterface

object Routes {
    const val LOGIN = "login"
    const val HOME = "home"
}

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Routes.LOGIN) {
        composable(Routes.LOGIN) {
            LoginInterface(navController)
        }
        composable(Routes.HOME) {
            HomeInterace(navController)
        }
    }
}
