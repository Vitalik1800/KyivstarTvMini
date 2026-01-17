package com.vs18.kyivstartvmini.ui.channel

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.vs18.kyivstartvmini.R
import com.vs18.kyivstartvmini.data.model.Channel

@Composable
fun ChannelCard(
    channel: Channel,
    onChannelClick: (Channel) -> Unit,
    onEpgClick: (Channel) -> Unit,
    onFavoriteClick: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .size(180.dp)
            .clickable { onChannelClick(channel) },
        shape = RoundedCornerShape(12.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {

            IconButton(
                onClick = { onFavoriteClick(channel.id) },
                modifier = Modifier
                    .align(Alignment.TopEnd)
            ) {
                Icon(
                    imageVector = if (channel.isFavorite)
                        Icons.Default.Star
                    else
                        Icons.Default.StarBorder,
                    contentDescription = "Favorite",
                    tint = Color.Yellow
                )
            }

            IconButton(
                onClick = { onEpgClick(channel) },
                modifier = Modifier.align(Alignment.TopStart)
            ) {
                Icon(
                    imageVector = Icons.Default.Schedule,
                    tint = Color.White,
                    contentDescription = "EPG"
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(channel.logoUrl)
                        .crossfade(true)
                        .diskCachePolicy(CachePolicy.ENABLED)
                        .memoryCachePolicy(CachePolicy.ENABLED)
                        .listener(
                            onError = { _, e ->
                                Log.e("IMG", "fail ${channel.logoUrl}", e.throwable)
                            }
                        )
                        .build(),
                    contentDescription = channel.name,
                    modifier = Modifier.size(96.dp),
                    placeholder = painterResource(R.drawable.ic_channel_placeholder),
                    error = painterResource(R.drawable.ic_channel_placeholder)
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    channel.name,
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}