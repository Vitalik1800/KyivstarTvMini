package com.vs18.kyivstartvmini.ui.error

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.vs18.kyivstartvmini.domain.error.AppError

@Composable
fun ErrorView(error: AppError) {
    val message = when(error) {
        AppError.Network -> "Немає інтернету"
        AppError.NotFound -> "Дані не знайдено"
        AppError.Parse -> "Помилка обробки даних"
        is AppError.Unknown -> "Щось пішло не так"
    }

    Text(
        text = message,
        color = Color.Red,
        style = MaterialTheme.typography.bodyLarge
    )
}