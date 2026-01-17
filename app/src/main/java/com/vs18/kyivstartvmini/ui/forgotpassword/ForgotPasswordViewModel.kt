package com.vs18.kyivstartvmini.ui.forgotpassword

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vs18.kyivstartvmini.data.repository.auth.AuthRepository
import kotlinx.coroutines.launch

class ForgotPasswordViewModel(
    private val repository: AuthRepository
) : ViewModel() {

    var uiState by mutableStateOf<ForgotPasswordState>(ForgotPasswordState.Idle)
        private set

    fun sendReset(email: String) {
        if (email.isBlank()) return

        uiState = ForgotPasswordState.Loading

        viewModelScope.launch {
            repository.forgotPassword(email)
                .onSuccess {
                    uiState = ForgotPasswordState.Success(
                        "If the email address is valid, the email has been sent"
                    )
                }
                .onFailure {
                    uiState = ForgotPasswordState.Error(
                        "Try again later"
                    )
                }
        }
    }
}

sealed class ForgotPasswordState {
    object Idle : ForgotPasswordState()
    object Loading : ForgotPasswordState()
    data class Success(val message: String) : ForgotPasswordState()
    data class Error(val message: String) : ForgotPasswordState()
}