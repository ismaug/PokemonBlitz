package com.example.pokemonblitz.ui.views

import android.content.Context
import androidx.lifecycle.*
import com.example.pokemonblitz.data.UserPreferences
import com.example.pokemonblitz.network.LoginRequest
import com.example.pokemonblitz.network.LoginResponse
import com.example.pokemonblitz.network.RetrofitInstance
import kotlinx.coroutines.launch

class LoginViewModel(
    context: Context
) : ViewModel() {

    private val userPreferences = UserPreferences(context)

    private val _loginStatus = MutableLiveData<String?>()
    val loginStatus: LiveData<String?> = _loginStatus

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val _loginSuccess = MutableLiveData<Boolean>()
    val loginSuccess: LiveData<Boolean> = _loginSuccess

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // 1. Login → Obtener token
                val loginResponse = RetrofitInstance.api.login(LoginRequest(email, password))
                if (loginResponse.isSuccessful) {
                    val token = loginResponse.body()?.token
                    if (!token.isNullOrBlank()) {
                        // 2. Obtener info del usuario usando el token
                        val userInfoResponse = RetrofitInstance.api.getUserInfo("Bearer $token")
                        if (userInfoResponse.isSuccessful) {
                            val user = userInfoResponse.body()
                            if (user != null) {
                                // 3. Guardar en DataStore: token + info del usuario
                                userPreferences.saveUserInfo(
                                    userId = user.id,
                                    username = user.username,
                                    email = user.email,
                                    token = token
                                )
                                _loginSuccess.value = true
                            } else {
                                _errorMessage.value = "Error: la respuesta del usuario es nula."
                            }
                        } else {
                            _errorMessage.value = "Error al obtener la información del usuario."
                        }
                    } else {
                        _errorMessage.value = "Error: token vacío o inválido."
                    }
                } else {
                    _errorMessage.value = "Credenciales inválidas o error del servidor."
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error inesperado: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearStatus() {
        _loginStatus.value = null
        _errorMessage.value = null
        _loginSuccess.value = false
    }
}

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
