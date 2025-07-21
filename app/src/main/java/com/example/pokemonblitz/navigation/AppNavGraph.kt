package com.example.pokemonblitz.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pokemonblitz.data.UserPreferences
import com.example.pokemonblitz.screens.HomeScreen
import com.example.pokemonblitz.screens.LoginScreen
import com.example.pokemonblitz.screens.RegisterScreen
import com.example.pokemonblitz.screens.PlayScreen
import com.example.pokemonblitz.ui.views.HomeViewModel
import com.example.pokemonblitz.ui.views.HomeViewModelFactory

@Composable
fun AppNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = NavDestinations.Login
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(NavDestinations.Login) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(NavDestinations.Home) {
                        popUpTo(NavDestinations.Login) { inclusive = true }
                    }
                },
                onRegisterClick = {
                    navController.navigate(NavDestinations.Register)
                }
            )
        }

        composable(NavDestinations.Register) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(NavDestinations.Home) {
                        popUpTo(NavDestinations.Register) { inclusive = true }
                    }
                },
                onAlreadyHaveAccount = {
                    navController.popBackStack()
                }
            )
        }

        composable(NavDestinations.Home) {
            val context = LocalContext.current
            val userPrefs = UserPreferences(context)
            val homeViewModel: HomeViewModel = viewModel(
                factory = HomeViewModelFactory(userPrefs)
            )

            LaunchedEffect(Unit) {
                homeViewModel.loadUserData()
            }

            HomeScreen(
                onPlayQuiz = {
                    navController.navigate(NavDestinations.Play)
                },
                onLogout = {
                    navController.navigate(NavDestinations.Login) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(NavDestinations.Play) {
            PlayScreen()
        }
    }
}