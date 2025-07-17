package com.example.pokemonblitz.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemonblitz.data.UserPreferences
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val preferences = UserPreferences(application.applicationContext)

    // StateFlow del token actual
    val tokenFlow: StateFlow<String?> = preferences.token
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    // Función para guardar token
    fun saveTokenToPrefs(token: String) {
        viewModelScope.launch {
            preferences.saveToken(token)
        }
    }

    // Función para borrar token (logout)
    fun clearToken() {
        viewModelScope.launch {
            preferences.clearToken()
        }
    }
}
