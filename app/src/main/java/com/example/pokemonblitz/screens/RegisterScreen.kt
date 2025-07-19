package com.example.pokemonblitz.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.pokemonblitz.R
import com.example.pokemonblitz.navigation.NavDestinations
import com.example.pokemonblitz.ui.RegisterViewModel
import com.example.pokemonblitz.ui.RegisterViewModelFactory
import com.example.pokemonblitz.ui.components.*


@Composable
fun RegisterScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val viewModel: RegisterViewModel = viewModel(factory = RegisterViewModelFactory(context.applicationContext))
    val registerSuccess = viewModel.registerSuccess

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Image(
            painter = painterResource(id = R.drawable.rockets),
            contentDescription = "Fondo Rockets",
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TitleText(text = "Registro de entrenadores Pokémon")

            Spacer(modifier = Modifier.height(16.dp))

            MyTextField(
                value = viewModel.username,
                onValueChange = { viewModel.username = it },
                label = "Username",
                isError = viewModel.usernameError,
                errorMessage = "Debe tener entre 3 y 12 letras"
            )

            Spacer(modifier = Modifier.height(8.dp))

            MyTextField(
                value = viewModel.email,
                onValueChange = { viewModel.email = it },
                label = "Email",
                isError = viewModel.emailError,
                errorMessage = "El email no puede estar vacío"
            )

            Spacer(modifier = Modifier.height(8.dp))

            MyTextField(
                value = viewModel.password,
                onValueChange = { viewModel.password = it },
                label = "Password",
                isError = viewModel.passwordError,
                errorMessage = "El password no puede estar vacío",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                isPassword = true
            )

            Spacer(modifier = Modifier.height(24.dp))

            PrimaryButton("Registrarse") {
                viewModel.registerUser()
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = { navController.navigate(NavDestinations.Login) }) {
                Text("¿Ya tienes cuenta? Inicia sesión")
            }
        }

        // Diálogo
        if (registerSuccess != null) {
            AlertDialog(
                onDismissRequest = { viewModel.resetStatus() },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.resetStatus()
                        if (registerSuccess) {
                            navController.navigate("login")
                        }
                    }) {
                        Text("OK")
                    }
                },
                icon = {
                    Image(
                        painter = painterResource(
                            id = if (registerSuccess) R.drawable.squirtle else R.drawable.meowth
                        ),
                        contentDescription = null,
                        modifier = Modifier.size(64.dp)
                    )
                },
                title = {
                    Text(
                        text = if (registerSuccess) "✅ Registro exitoso"
                        else "❌ Ocurrió un error"
                    )
                },
                text = {
                    Text(
                        text = if (registerSuccess) "Tu cuenta fue creada con éxito"
                        else "No se pudo registrar, verifica los datos"
                    )
                }
            )
        }
    }
}
