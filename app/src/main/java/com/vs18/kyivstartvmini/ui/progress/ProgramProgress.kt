package com.vs18.kyivstartvmini.ui.progress

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.vs18.kyivstartvmini.data.model.parseEpgTime
import java.time.ZonedDateTime

@SuppressLint("NewApi")
@Composable
fun ProgramProgress(start: String, end: String) {
    val startTime = parseEpgTime(start)
    val endTime = parseEpgTime(end)
    val now = ZonedDateTime.now()

    val progress = ((java.time.Duration.between(startTime, now).toMillis()
        .toFloat() / java.time.Duration.between(startTime, endTime).toMillis())
        .coerceIn(0f, 1f))

    LinearProgressIndicator(
    progress = { progress },
    modifier = Modifier
                .fillMaxWidth()
                .height(4.dp),
    color = Color(0xFF64B5F6),
    trackColor = ProgressIndicatorDefaults.linearTrackColor,
    strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
    )
}