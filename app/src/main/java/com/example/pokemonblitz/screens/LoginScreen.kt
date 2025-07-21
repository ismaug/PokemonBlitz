package com.example.pokemonblitz.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pokemonblitz.R
import com.example.pokemonblitz.ui.views.LoginViewModel
import com.example.pokemonblitz.ui.views.LoginViewModelFactory
import com.example.pokemonblitz.ui.components.MyTextField
import com.example.pokemonblitz.ui.components.PrimaryButton
import com.example.pokemonblitz.ui.components.TitleText

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onRegisterClick: () -> Unit
) {
    val context = LocalContext.current
    val viewModel: LoginViewModel = viewModel(factory = LoginViewModelFactory(context.applicationContext))
    val loginSuccess by viewModel.loginSuccess.observeAsState()
    val errorMessage by viewModel.errorMessage.observeAsState()
    val isLoading by viewModel.isLoading.observeAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Observar el estado de login exitoso
    LaunchedEffect(loginSuccess) {
        if (loginSuccess == true) {
            onLoginSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 48.dp, start = 16.dp, end = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(painter = painterResource(id = R.drawable.pikachu), contentDescription = "Pikachu")
        Spacer(modifier = Modifier.height(8.dp))
        TitleText("POKEBLITZ")
        Spacer(modifier = Modifier.height(24.dp))
        Text("Sign In", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(16.dp))

        MyTextField(value = email, onValueChange = { email = it }, label = "Email")
        Spacer(modifier = Modifier.height(8.dp))
        MyTextField(
            value = password,
            onValueChange = { password = it },
            label = "Password",
            isPassword = true
        )
        Spacer(modifier = Modifier.height(16.dp))

        PrimaryButton("Login") {
            viewModel.login(email, password)
        }

        Spacer(modifier = Modifier.height(8.dp))

        errorMessage?.let {
            Text(it, color = MaterialTheme.colorScheme.error)
        }

        if (isLoading == true) {
            CircularProgressIndicator()
        }

        TextButton(onClick = onRegisterClick) {
            Text("No tienes cuenta? Regístrate")
        }
    }
}