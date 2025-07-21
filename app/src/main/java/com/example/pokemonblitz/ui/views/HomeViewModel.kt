package com.example.pokemonblitz.ui.views

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.pokemonblitz.data.UserPreferences
import com.example.pokemonblitz.network.RetrofitInstance
import com.example.pokemonblitz.network.ScoreResponse
import com.example.pokemonblitz.network.GlobalScoreResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class HomeViewModel(private val preferences: UserPreferences) : ViewModel() {

    private val _personalScores = MutableStateFlow<List<ScoreResponse>>(emptyList())
    val personalScores: StateFlow<List<ScoreResponse>> = _personalScores

    private val _globalScores = MutableStateFlow<List<GlobalScoreResponse>>(emptyList())
    val globalScores: StateFlow<List<GlobalScoreResponse>> = _globalScores

    private val _username = MutableStateFlow("Entrenador")
    val username: StateFlow<String> = _username

    private val api = RetrofitInstance.api

    fun loadUserData() {
        viewModelScope.launch {
            try {
                val user = preferences.getUser().first()
                val token = preferences.tokenFlow.first() ?: ""

                if (token.isBlank() || user == null) return@launch

                _username.value = user.username

                val bearer = "Bearer $token"

                val personal = api.getUserScores(bearer)
                _personalScores.value = personal.take(3)

                val global = api.getTopScores()
                _globalScores.value = global

            } catch (e: Exception) {
                _personalScores.value = emptyList()
                _globalScores.value = emptyList()
            }
        }
    }
}

class HomeViewModelFactory(
    private val preferences: UserPreferences
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(preferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
