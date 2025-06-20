package com.example.gamesapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.gamesapplication.navigation.AppNavigation
import com.example.gamesapplication.ui.theme.GamesApplicationTheme



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GamesApplicationTheme(dynamicColor = false) {
                val navController = rememberNavController()
                AppNavigation(navController)
            }
        }
    }
}

