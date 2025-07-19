package com.example.pokemonblitz.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

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