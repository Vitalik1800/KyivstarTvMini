package com.vs18.kyivstartvmini.ui.player

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.*
import com.vs18.kyivstartvmini.data.model.*
import com.vs18.kyivstartvmini.player.*

@Composable
fun PlayerScreen(
    channel: Channel,
    onBack: () -> Unit) {
    var error by remember { mutableStateOf<String?>(null) }

    BackHandler {
        onBack()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(channel.name, style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(16.dp))
        Box(modifier = Modifier.weight(1f)) {
            ImaAdsVideoPlayer(
                channels = listOf(channel.streamUrl),
                streamUrl = channel.streamUrl,
                onError = { error = it}
            )
        }
        error?.let {
            Text(it, color = Color.Red, modifier = Modifier.padding(16.dp))
        }
    }
}