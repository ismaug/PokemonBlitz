package com.example.pokemonblitz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.pokemonblitz.screens.LoginScreen
import com.example.pokemonblitz.ui.theme.PokemonBlitzTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PokemonBlitzTheme {
                LoginScreen()
            }
        }
    }
}
