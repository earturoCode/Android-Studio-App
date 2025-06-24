package com.example.gamesapplication.navigation


import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.gamesapplication.screens.HomeInterace
import com.example.gamesapplication.screens.LoginInterface
import com.example.minigames.ui.register.ui.RegisterScreen
import com.example.gamesapplication.viewmodels.RegisterViewModel

object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"
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
            HomeInterace(navController)
        }
    }
}
