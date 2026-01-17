package com.vs18.kyivstartvmini.ui.auth

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vs18.kyivstartvmini.data.api.auth.FirebaseAuthModule
import com.vs18.kyivstartvmini.data.repository.auth.AuthRepository
import com.vs18.kyivstartvmini.datastore.AuthPreferences
import com.vs18.kyivstartvmini.ui.forgotpassword.ForgotPasswordScreen
import com.vs18.kyivstartvmini.viewmodel.auth.AuthViewModel
import com.vs18.kyivstartvmini.viewmodel.auth.AuthViewModelFactory

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AuthScreen(
    onAuthorized: () -> Unit
) {
    val context = LocalContext.current

    val viewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(
            repository = AuthRepository(
                api = FirebaseAuthModule.api
            ),
            prefs = AuthPreferences(context)
        )
    )

    var showForgotPassword by remember { mutableStateOf(false) }

    val authorized by viewModel.authorized.collectAsState()
    val error by viewModel.error.collectAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLogin by remember { mutableStateOf(true) }

    val brandBlue = Color(0xFF00B7EB)
    val darkBg = Color(0xFF0F172A)
    val textPrimary = Color(0xFFE5E7EB)
    val textSecondary = Color(0xFF9CA3AF)

    if (authorized) {
        onAuthorized()
    }

    if (showForgotPassword) {
        ForgotPasswordScreen(
            viewModel = viewModel,
            onBack = { showForgotPassword = false }
        )
        return
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(darkBg)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = if (isLogin) "Login" else "Register",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = brandBlue
            )

            Spacer(Modifier.height(24.dp))

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

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
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

            if (isLogin) {
                TextButton(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    onClick = { showForgotPassword = true }
                ) {
                    Text(
                        text = "Forgot password?",
                        color = brandBlue,
                        fontSize = 14.sp
                    )
                }

                Spacer(Modifier.height(8.dp))
            }

            Button(
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = brandBlue,
                    contentColor = Color.Black
                ),
                onClick = {
                    if (isLogin) {
                        viewModel.login(email, password)
                    } else {
                        viewModel.register(email, password)
                    }
                }
            ) {
                Text(if (isLogin) "Sign In" else "Sign Up")
            }

            TextButton(onClick = { isLogin = !isLogin }) {
                Text(
                    if (isLogin)
                        "No account? Register"
                    else
                        "Already have account? Login",
                    color = brandBlue
                )
            }

            error?.let {
                Spacer(Modifier.height(12.dp))
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }

}