package com.example.gamesapplication.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF90CAF9),        // Azul claro
    onPrimary = Color(0xFF0D47A1),      // Texto oscuro sobre azul claro
    secondary = Color(0xFF64B5F6),      // Azul más suave
    onSecondary = Color.Black,
    tertiary = Color(0xFF42A5F5),       // Azul intermedio
    onTertiary = Color.Black,
    background = Color(0xFF022800),     // Fondo gris oscuro
    onBackground = Color(0xFFECEFF1),   // Texto claro sobre fondo
    surface = Color(0xFF1E1E1E),        // Superficies un poco más claras que el fondo
    onSurface = Color(0xFFECEFF1)       // Texto claro sobre superficie
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF0D47A1),        // Azul oscuro
    onPrimary = Color.White,            // Texto sobre el primario
    secondary = Color(0xFF1976D2),      // Azul medio
    onSecondary = Color.White,
    tertiary = Color(0xFF64B5F6),       // Azul claro
    onTertiary = Color.Black,
    background = Color(0xFF4CAF50),     // Gris muy claro
    onBackground = Color(0xFF1A1A1A),   // Texto sobre fondo
    surface = Color(0xFFFFFFFF),        // Blanco puro para tarjetas/superficies
    onSurface = Color(0xFF1A1A1A)       // Texto sobre superficie
)

@Composable
fun GamesApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}