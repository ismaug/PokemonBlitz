package com.example.pokemonblitz.ui

import android.content.Context
import androidx.lifecycle.*
import com.example.pokemonblitz.data.UserPreferences
import com.example.pokemonblitz.network.LoginRequest
import com.example.pokemonblitz.network.RetrofitInstance
import kotlinx.coroutines.launch

class LoginViewModel(
    context: Context
) : ViewModel() {

    private val prefs = UserPreferences(context)

    private val _loginStatus = MutableLiveData<String?>()
    val loginStatus: LiveData<String?> = _loginStatus

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.login(LoginRequest(email, password))
                if (response.isSuccessful) {
                    response.body()?.let {
                        prefs.saveToken(it.token)
                        _loginStatus.value = "Token recibido ✅"
                    } ?: run {
                        _loginStatus.value = "Respuesta vacía ❌"
                    }
                } else {
                    _loginStatus.value = "Login inválido ❌"
                }
            } catch (e: Exception) {
                _loginStatus.value = "Error: ${e.localizedMessage}"
            }
        }
    }

    fun clearStatus() {
        _loginStatus.value = null
    }
}

// Este es el factory y crea el viewmodel para insertar en LoginScreen
class LoginViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
