package com.example.gamesapplication.navigation


import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.gamesapplication.screens.HomeInterface
import com.example.gamesapplication.screens.LoginInterface
import com.example.gamesapplication.screens.PokerGameScreen
import com.example.gamesapplication.screens.RegisterScreen
import com.example.gamesapplication.screens.ScoreboardScreen
import com.example.gamesapplication.screens.TocameInterace
import com.example.gamesapplication.viewModels.RegisterViewModel

object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"
    const val POKER = "poker"
    const val TOCAME = "tocame"
    const val SCOREBOARD = "scoreboard"
}

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Routes.LOGIN) {
        composable(Routes.LOGIN) {
            LoginInterface(navController)
        }
        composable(Routes.REGISTER) {
            RegisterScreen(
                viewModel = viewModel<RegisterViewModel>(),
                navController = navController
            )
        }
        composable(Routes.HOME) {
            HomeInterface(navController)
        }
        composable(Routes.POKER) {
            PokerGameScreen(navController)
        }
        composable(Routes.TOCAME) {
            TocameInterace(navController)
        }
        composable(Routes.SCOREBOARD) {
            ScoreboardScreen( )
        }
    }
}
