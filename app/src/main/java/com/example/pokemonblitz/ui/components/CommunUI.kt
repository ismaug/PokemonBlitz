package com.example.pokemonblitz.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.pokemonblitz.navigation.NavDestinations


@Composable
fun MyTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isError: Boolean = false,
    errorMessage : String = "",
    isPassword: Boolean = false,
    keyboardOptions: KeyboardOptions =
        KeyboardOptions.Default
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        isError = isError,
        supportingText = {
            Row {
                if (isError) { Text(errorMessage)} else Text("")
            }
        },
        modifier = Modifier.fillMaxWidth(),
        visualTransformation = if (isPassword)
            PasswordVisualTransformation()
        else
            VisualTransformation.None,
        keyboardOptions = keyboardOptions
    )
}

@Composable
fun ErrorText(message: String) {
    Text(text = message, color = Color.Red, style = MaterialTheme.typography.bodySmall)
}

@Composable
fun PrimaryButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
    ) {
        Text(text)
    }
}
@Composable
fun TitleText(text: String, color: Color = Color.Black) {
    Text(text, color = color, textAlign = TextAlign.Center)}

@Composable
fun ToggleScoreScope(
    options: List<String>,
    selectedIndex: Int,
    onOptionSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    SingleChoiceSegmentedButtonRow(modifier = modifier) {
        options.forEachIndexed { index, label ->
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
                onClick = { onOptionSelected(index) },
                selected = index == selectedIndex,
            ) {
                Text(label)
            }
        }
    }
}

@Composable
fun ScoreMetricBox(label: String, value: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(4.dp)
            )
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
@Composable
fun ScoreEntryItem(
    modifier: Modifier = Modifier,
    rank: Int,
    time: Int,
    correctAnswers: Int,
    stars: Int,
    showMeta: Boolean = false,
    username: String? = null,
    date: String? = null
) {
    Column(modifier = modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp)) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("#$rank", style = MaterialTheme.typography.titleMedium)
            Text("$stars/5", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.weight(1f))
            ScoreMetricBox(label = "Time", value = "$time s")
            ScoreMetricBox(label = "Correct", value = correctAnswers.toString())
        }

        if (showMeta && username != null && date != null) {
            Spacer(modifier = Modifier.height(4.dp))
            Text("$username • $date", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
@Composable
fun BottomNavBar(
    currentRoute: String,
    onNavigateHome: () -> Unit,
    onNavigatePlay: () -> Unit
) {
    NavigationBar {
        NavigationBarItem(
            selected = (currentRoute == NavDestinations.Home),
            onClick = onNavigateHome,
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") }
        )
        NavigationBarItem(
            selected = (currentRoute == NavDestinations.Play),
            onClick = onNavigatePlay,
            icon = { Icon(Icons.Default.PlayCircle, contentDescription = "Play") },
            label = { Text("Play") }
        )
    }
}


