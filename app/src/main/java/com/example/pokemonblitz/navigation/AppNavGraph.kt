package com.example.pokemonblitz.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.pokemonblitz.screens.LoginScreen
import com.example.pokemonblitz.screens.RegisterScreen
import com.example.pokemonblitz.screens.PlayScreen


@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = NavDestinations.Play) {
        composable(NavDestinations.Login.toString()) {
            LoginScreen(navController)
        }
        composable(NavDestinations.Register.toString()) {
            RegisterScreen(navController)
        }
        composable(NavDestinations.Play) {
            PlayScreen()
        }
    }
}
