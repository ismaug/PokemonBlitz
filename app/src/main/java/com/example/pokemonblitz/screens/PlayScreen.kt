package com.example.pokemonblitz.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.pokemonblitz.R
import com.example.pokemonblitz.ui.QuizViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.shape.RoundedCornerShape

@Composable
fun PlayScreen(
    onBackToHome: (() -> Unit)? = null,
    viewModel: QuizViewModel = viewModel()
) {
    var guess by remember { mutableStateOf("") }
    val pokemonName by viewModel.pokemonName.collectAsState()
    val guessResult by viewModel.guessResult.collectAsState()
    val guessedPokemonImage by viewModel.guessedPokemonImageRes.collectAsState()

    val retroFont = FontFamily.Monospace

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF121212), Color(0xFF333333))
                )
            )
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header
        Text(
            "QUIZ",
            fontSize = 28.sp,
            color = Color.Yellow,
            fontFamily = retroFont
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            "CATCH 'EM ALL!",
            fontSize = 20.sp,
            color = Color.Green,
            fontFamily = retroFont
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Guess input
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = guess,
                onValueChange = { guess = it },
                placeholder = { Text("Enter your guess", fontFamily = retroFont) },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Text
                ),
                modifier = Modifier.weight(1f),
                textStyle = TextStyle(fontFamily = retroFont)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                viewModel.guessPokemon()
                guess = ""
            }) {
                Text("Guess", fontFamily = retroFont)
            }
        }

        Spacer(modifier = Modifier.height(100.dp))

        // Image section
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                if (guessResult == "Correct!") {
                    Image(
                        painter = painterResource(id = guessedPokemonImage),
                        contentDescription = "Pokémon adivinado",
                        modifier = Modifier.size(80.dp),
                        contentScale = ContentScale.Fit
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.pikachu),
                        contentDescription = "Pokémon placeholder",
                        modifier = Modifier.size(80.dp),
                        contentScale = ContentScale.Fit
                    )
                }
                Text(
                    "Último Pokémon",
                    color = Color.White,
                    fontFamily = retroFont
                )
            }

            Image(
                painter = painterResource(id = R.drawable.pokeplaceholder),
                contentDescription = "Pokéball",
                modifier = Modifier.size(100.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Retro Box con bordes para Score y Time
        Box(
            modifier = Modifier
                .border(2.dp, Color.Cyan, RoundedCornerShape(8.dp))
                .padding(12.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "🎯 Score 0/151",
                    fontSize = 16.sp,
                    color = Color.White,
                    fontFamily = retroFont
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "⏱️ Time Elapsed 04:32",
                    fontSize = 16.sp,
                    color = Color.White,
                    fontFamily = retroFont
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Buttons
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(onClick = { /* Implementa función inspeccionar */ }) {
                Text("Inspect", fontFamily = retroFont)
            }
            Button(onClick = { /* Implementa función rendirse */ }) {
                Text("Give Up", fontFamily = retroFont)
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Bottom bar estilo retro
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.DarkGray)
                .padding(8.dp)
        ) {
            Text(
                "🏠 Home",
                fontFamily = retroFont,
                color = Color.White,
                modifier = Modifier.clickable {
                    onBackToHome?.invoke()
                }
            )
            Text("▶️ Quiz", fontFamily = retroFont, color = Color.White)
        }
    }
}