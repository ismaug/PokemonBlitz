package com.example.pokemonblitz.ui.views

import android.content.Context
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.pokemonblitz.network.RetrofitInstance
import kotlinx.coroutines.launch

class RegisterViewModel(context: Context) : ViewModel() {

    // Campos del formulario
    var username by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")

    // Validaciones
    var usernameError by mutableStateOf(false)
    var emailError by mutableStateOf(false)
    var passwordError by mutableStateOf(false)

    // Resultado
    var registerSuccess by mutableStateOf<Boolean?>(null)

    private val api = RetrofitInstance.api

    fun validate(): Boolean {
        usernameError = username.length !in 3..12
        emailError = email.isBlank()
        passwordError = password.isBlank()
        return !(usernameError || emailError || passwordError)
    }

    fun registerUser() {
        if (!validate()) return

        viewModelScope.launch {
            try {
                val response = api.register(
                    mapOf(
                        "username" to username,
                        "email" to email,
                        "password" to password
                    )
                )
                registerSuccess = response.isSuccessful
            } catch (e: Exception) {
                registerSuccess = false
            }
        }
    }

    fun resetStatus() {
        registerSuccess = null
    }
}
class RegisterViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RegisterViewModel(context) as T
    }
}

