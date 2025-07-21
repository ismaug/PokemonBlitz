@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.pokemonblitz.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pokemonblitz.data.UserPreferences
import com.example.pokemonblitz.network.GlobalScoreResponse
import com.example.pokemonblitz.network.ScoreResponse
import com.example.pokemonblitz.ui.components.BottomNavBar
import com.example.pokemonblitz.ui.components.ScoreEntryItem
import com.example.pokemonblitz.ui.views.HomeViewModel
import com.example.pokemonblitz.ui.views.HomeViewModelFactory

enum class ViewMode { PERSONAL, GLOBAL }

@Composable
fun HomeScreen(
    onPlayQuiz: () -> Unit,
    onLogout: () -> Unit,
    viewModel: HomeViewModel? = null
) {
    // Si no se pasa viewModel, crear uno aquí
    val actualViewModel = viewModel ?: run {
        val context = LocalContext.current
        val userPrefs = UserPreferences(context)
        viewModel(factory = HomeViewModelFactory(userPrefs))
    }

    // Cuando se llega a la pantalla se recupera la data del usuario del DataStore
    LaunchedEffect(Unit) {
        actualViewModel.loadUserData()
    }

    // Flows de datos para cada data class que usaremos aqui
    val personal by actualViewModel.personalScores.collectAsState()
    val global by actualViewModel.globalScores.collectAsState()
    val username by actualViewModel.username.collectAsState()

    var viewMode by remember { mutableStateOf(ViewMode.PERSONAL) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding())
    ) {
        TopAppBar(
            title = { Text("Bienvenido entrenador $username") },
            actions = {
                IconButton(onClick = {
                    // Aquí deberías llamar a actualViewModel.clearUserInfo() si tienes ese método
                    onLogout()
                }) {
                    Icon(imageVector = Icons.Default.Logout, contentDescription = "Logout")
                }
            }
        )

        SingleChoiceSegmentedButtonRow {
            listOf("Personal", "Global").forEachIndexed { i, label ->
                SegmentedButton(
                    shape = SegmentedButtonDefaults.itemShape(i, 2),
                    selected = viewMode.ordinal == i,
                    onClick = {
                        viewMode = if (i == 0) ViewMode.PERSONAL else ViewMode.GLOBAL
                    },
                    label = { Text(label) }
                )
            }
        }

        val displayScores = if (viewMode == ViewMode.PERSONAL) personal.take(3) else global.take(5)

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp)
        ) {
            itemsIndexed(displayScores) { index, item ->
                if (viewMode == ViewMode.PERSONAL && item is ScoreResponse) {
                    ScoreEntryItem(
                        rank = index + 1,
                        time = item.time,
                        correctAnswers = item.correctAnswers,
                        stars = item.score,
                        modifier = Modifier.fillMaxWidth()
                    )
                } else if (viewMode == ViewMode.GLOBAL && item is GlobalScoreResponse) {
                    ScoreEntryItem(
                        rank = index + 1,
                        time = item.time,
                        correctAnswers = item.correctAnswers,
                        stars = item.score,
                        showMeta = true,
                        username = item.username,
                        date = item.createdAt,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        BottomNavBar(
            currentRoute = "home", // Temporal - deberías pasar la ruta actual como parámetro
            onNavigateHome = {},   // no hace nada si ya estás en Home
            onNavigatePlay = onPlayQuiz
        )
    }
}