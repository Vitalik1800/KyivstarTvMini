package com.vs18.kyivstartvmini.ui.forgotpassword

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.vs18.kyivstartvmini.viewmodel.auth.AuthUiState
import com.vs18.kyivstartvmini.viewmodel.auth.AuthViewModel

@Composable
fun ForgotPasswordScreen(
    viewModel: AuthViewModel,
    onBack: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    val state = viewModel.uiState

    val brandBlue = Color(0xFF00B7EB)
    val darkBg = Color(0xFF0F172A)
    val textPrimary = Color(0xFFE5E7EB)
    val textSecondary = Color(0xFF9CA3AF)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Recovering password",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = brandBlue,
                focusedLabelColor = brandBlue,
                cursorColor = brandBlue,
                unfocusedBorderColor = textSecondary,
                unfocusedLabelColor = textSecondary,
                focusedTextColor = textPrimary,
                unfocusedTextColor = textPrimary
            )
        )

        Spacer(Modifier.height(16.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = brandBlue,
                contentColor = Color.Black
            ),
            enabled = state !is AuthUiState.Loading,
            onClick = {
                viewModel.forgotPassword(email)
            }
        ) {
            if (state is AuthUiState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp
                )
            } else {
                Text("Send")
            }
        }

        Spacer(Modifier.height(8.dp))

        TextButton(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = onBack
        ) {
            Text(
                "Back",
                color = brandBlue
            )
        }

        when (state) {
            is AuthUiState.Message -> {
                Spacer(Modifier.height(12.dp))
                Text(
                    text = state.text,
                    color = Color.Gray,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }

            is AuthUiState.Error -> {
                Spacer(Modifier.height(12.dp))
                Text(
                    text = state.text,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }

            else -> Unit
        }
    }
}
