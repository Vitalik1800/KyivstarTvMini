package com.vs18.kyivstartvmini.viewmodel.auth

import androidx.compose.runtime.*
import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import com.vs18.kyivstartvmini.datastore.AuthPreferences
import com.vs18.kyivstartvmini.data.repository.auth.AuthRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: AuthRepository,
    private val prefs: AuthPreferences
): ViewModel() {

    private val _authorized = MutableStateFlow(false)
    val authorized: StateFlow<Boolean> = _authorized

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    var uiState by mutableStateOf<AuthUiState>(AuthUiState.Idle)
        private set

    fun login(email: String, password: String) {
        viewModelScope.launch {
            repository.login(email, password)
                .onSuccess {
                    prefs.saveAuth(it.idToken, it.email)
                    _authorized.value = true
                }
                .onFailure { _error.value = it.message }
        }
    }

    fun register(email: String, password: String) {
        viewModelScope.launch {
            repository.register(email, password)
                .onSuccess {
                    prefs.saveAuth(it.idToken, it.email)
                    _authorized.value = true
                }
                .onFailure { _error.value = it.message }
        }
    }

    fun forgotPassword(email: String) {
        if (email.isBlank()) {
            uiState = AuthUiState.Error("Email is empty")
            return
        }

        uiState = AuthUiState.Loading

        FirebaseAuth.getInstance()
            .sendPasswordResetEmail(email)
            .addOnSuccessListener {
                uiState = AuthUiState.Message(
                    "If the email exists, the email has been sent 📧"
                )
            }
            .addOnFailureListener {
                uiState = AuthUiState.Error(
                    it.message ?: "Try again later"
                )
            }
    }
}