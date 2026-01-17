package com.vs18.kyivstartvmini.datastore

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.*

private val Context.dataStore by preferencesDataStore("auth_prefs")

class AuthPreferences(private val context: Context) {

    companion object {
        private val TOKEN = stringPreferencesKey("id_token")
        private val EMAIL = stringPreferencesKey("email")
    }

    val token: Flow<String?> = context.dataStore.data.map { it[TOKEN] }

    val email: Flow<String?> = context.dataStore.data.map { it[EMAIL] }

    suspend fun saveAuth(token: String, email: String) {
        context.dataStore.edit { prefs ->
            prefs[TOKEN] = token
            prefs[EMAIL] = email.lowercase().trim()
        }
    }

    suspend fun clear() {
        context.dataStore.edit { it.clear() }
    }

}