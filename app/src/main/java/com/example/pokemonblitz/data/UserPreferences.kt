package com.example.pokemonblitz.data

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Extension para el contexto
val Context.dataStore by preferencesDataStore(name = "user_prefs")

// Claves
val KEY_USER_ID = stringPreferencesKey("user_id")
val KEY_USERNAME = stringPreferencesKey("username")
val KEY_EMAIL = stringPreferencesKey("email")
val KEY_TOKEN = stringPreferencesKey("auth_token")

// Clase de manejo de DataStore
class UserPreferences(private val context: Context) {

    // Flows para leer los valores
    val userIdFlow: Flow<String?> = context.dataStore.data.map { it[KEY_USER_ID] }
    val usernameFlow: Flow<String?> = context.dataStore.data.map { it[KEY_USERNAME] }
    val emailFlow: Flow<String?> = context.dataStore.data.map { it[KEY_EMAIL] }
    val tokenFlow: Flow<String?> = context.dataStore.data.map { it[KEY_TOKEN] }

    // Guardar toda la información del usuario (incluyendo token)
    suspend fun saveUserInfo(userId: String, username: String, email: String, token: String) {
        context.dataStore.edit { preferences ->
            preferences[KEY_USER_ID] = userId
            preferences[KEY_USERNAME] = username
            preferences[KEY_EMAIL] = email
            preferences[KEY_TOKEN] = token
        }
    }
    data class StoredUser(val id: String, val username: String, val email: String)

    fun getUser(): Flow<StoredUser?> {
        return context.dataStore.data.map { preferences ->
            val id = preferences[KEY_USER_ID]
            val username = preferences[KEY_USERNAME]
            val email = preferences[KEY_EMAIL]
            if (id != null && username != null && email != null) {
                StoredUser(id, username, email)
            } else {
                null
            }
        }
    }

    // Borrar toda la información (para logout)
    suspend fun clearUserInfo() {
        context.dataStore.edit { preferences ->
            preferences.remove(KEY_USER_ID)
            preferences.remove(KEY_USERNAME)
            preferences.remove(KEY_EMAIL)
            preferences.remove(KEY_TOKEN)
        }
    }
}
