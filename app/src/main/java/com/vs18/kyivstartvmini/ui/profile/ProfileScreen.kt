package com.vs18.kyivstartvmini.ui.profile

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import com.vs18.kyivstartvmini.datastore.AuthPreferences
import kotlinx.coroutines.*

@Composable
fun ProfileScreen(
    onLogout: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val prefs = remember { AuthPreferences(context) }
    val email by prefs.email.collectAsState(initial = null)
    val scope = rememberCoroutineScope()

    BackHandler {
        onBack()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Профіль",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF00b7eb)
            )

            Spacer(Modifier.height(24.dp))

            Text(
                text = email ?: "Unknown user",
                fontSize = 18.sp,
                color = Color.White
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Ви авторизовані",
                fontSize = 14.sp,
                color = Color(0xFF9CA3AF)
            )

            Spacer(Modifier.height(32.dp))

            Button(
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color(0xFFD3232F),
                    containerColor = Color.White
                ),
                onClick = {
                    scope.launch {
                        prefs.clear()
                        onLogout()
                    }
                }
            ) {
                Text("Logout")
            }
        }
    }
}