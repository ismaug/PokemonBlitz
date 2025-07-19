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
import androidx.navigation.NavHostController
import com.example.pokemonblitz.R
import com.example.pokemonblitz.navigation.NavDestinations
import com.example.pokemonblitz.ui.LoginViewModel
import com.example.pokemonblitz.ui.LoginViewModelFactory
import com.example.pokemonblitz.ui.components.MyTextField
import com.example.pokemonblitz.ui.components.PrimaryButton
import com.example.pokemonblitz.ui.components.TitleText

@Composable
fun LoginScreen(navController: NavHostController) {
    val context = LocalContext.current
    val viewModel: LoginViewModel = viewModel(factory = LoginViewModelFactory(context.applicationContext))
    val loginStatus by viewModel.loginStatus.observeAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

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

        loginStatus?.let {
            Text(it, color = MaterialTheme.colorScheme.secondary)
        }

        TextButton(onClick = {
            navController.navigate(NavDestinations.Register)
        }) {
            Text("No tienes cuenta? Regístrate")
        }
    }
}
