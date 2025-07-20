package com.example.pokemonblitz.ui

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemonblitz.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class QuizViewModel : ViewModel() {

    private val correctPokemonName = "pikachu" // Si quieres, puedes eliminarlo porque usas lista

    // Estado de texto ingresado (nombre del Pokémon que se intenta adivinar)
    private val _pokemonName = MutableStateFlow("")
    val pokemonName: StateFlow<String> get() = _pokemonName

    // Imagen actual para mostrar (recurso drawable)
    private val _guessedPokemonImageRes = MutableStateFlow(R.drawable.pokeballplaceholder)
    val guessedPokemonImageRes: StateFlow<Int> get() = _guessedPokemonImageRes

    // Score actual
    private val _score = MutableStateFlow(0)
    val score: StateFlow<Int> get() = _score

    // Total de Pokémon en el juego (para mostrar fracción)
    private val _totalPokemon = MutableStateFlow(151)
    val totalPokemon: StateFlow<Int> get() = _totalPokemon

    // Tiempo transcurrido en segundos
    private val _timeElapsed = MutableStateFlow(0)
    val timeElapsed: StateFlow<Int> get() = _timeElapsed

    // Resultado del intento (Correcto / Incorrecto / null si nada)
    private val _guessResult = MutableStateFlow<String?>(null)
    val guessResult: StateFlow<String?> get() = _guessResult

    // Color de fondo que cambia según acierto o error
    private val _backgroundColor = MutableStateFlow(Color(0xFF1D1E33))
    val backgroundColor: StateFlow<Color> get() = _backgroundColor

    // Estado de carga si deseas mostrar spinner (no usado en UI actualmente)
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    // Estado general UI (opcional)
    private val _uiState = MutableStateFlow(QuizUiState())
    val uiState: StateFlow<QuizUiState> get() = _uiState

    private var isTimerRunning = false

    // Cambiar el nombre escrito en el input
    fun onPokemonNameChange(newName: String) {
        _pokemonName.value = newName
    }

    // Función que se llama al presionar "Guess"
    fun guessPokemon() {
        val guess = _pokemonName.value.trim().lowercase()

        if (guess.isEmpty()) return

        viewModelScope.launch {
            _isLoading.value = true

            val isCorrect = validateGuess(guess)

            _isLoading.value = false

            if (isCorrect) {
                _score.value += 1
                _guessedPokemonImageRes.value = getImageForPokemon(guess)
                _guessResult.value = "Correct!"
                _backgroundColor.value = Color(0xFF4CAF50) // Verde
            } else {
                _guessedPokemonImageRes.value = R.drawable.pokeballplaceholder
                _guessResult.value = "Incorrect!"
                _backgroundColor.value = Color(0xFFE53935) // Rojo
            }

            // Actualizar estado general (opcional)
            _uiState.value = QuizUiState(
                score = _score.value,
                timeElapsed = _timeElapsed.value,
                pokemonName = "",
                guessedPokemonImageRes = _guessedPokemonImageRes.value,
                guessResult = _guessResult.value,
                backgroundColor = _backgroundColor.value
            )

            // Mostrar resultado por un instante
            delay(800)

            // Resetear entrada y color
            _pokemonName.value = ""
            _backgroundColor.value = Color(0xFF1D1E33)
            _guessResult.value = null
        }
    }

    // Valida si el nombre está en la lista de Pokémon válidos
    private suspend fun validateGuess(name: String): Boolean {
        delay(300)
        val validPokemon = listOf("pikachu", "bulbasaur", "charmander", "squirtle")
        return name in validPokemon
    }

    // Retorna la imagen drawable correspondiente al nombre de Pokémon
    private fun getImageForPokemon(name: String): Int {
        return when (name) {
            "pikachu" -> R.drawable.pikachu
            "squirtle" -> R.drawable.squirtle
            else -> R.drawable.pokeballplaceholder
        }
    }

    // Inicia un timer que cuenta segundos y actualiza estado
    fun startTimer() {
        if (isTimerRunning) return
        isTimerRunning = true

        viewModelScope.launch {
            while (true) {
                delay(1000)
                _timeElapsed.value += 1

                // Actualizar UI State (opcional)
                _uiState.value = _uiState.value.copy(
                    timeElapsed = _timeElapsed.value
                )
            }
        }
    }

    // Reinicia el juego a estado inicial
    fun resetGame() {
        _score.value = 0
        _pokemonName.value = ""
        _guessedPokemonImageRes.value = R.drawable.pokeballplaceholder
        _timeElapsed.value = 0
        _guessResult.value = null
        isTimerRunning = false
        _backgroundColor.value = Color(0xFF1D1E33)
        _uiState.value = QuizUiState()
    }

    // Data class para estado UI (opcional)
    data class QuizUiState(
        val score: Int = 0,
        val timeElapsed: Int = 0,
        val pokemonName: String = "",
        val guessedPokemonImageRes: Int = R.drawable.pokeballplaceholder,
        val guessResult: String? = null,
        val backgroundColor: Color = Color(0xFF1D1E33)
    )
}
