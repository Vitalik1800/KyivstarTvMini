package com.vs18.kyivstartvmini.ui.channel

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.vs18.kyivstartvmini.data.model.Channel

@Composable
fun ChannelGrid(
    channels: List<Channel>,
    onChannelClick: (Channel) -> Unit,
    onEpgClick: (Channel) -> Unit,
    onFavoriteClick: (Int) -> Unit
) {

    val gridState = rememberLazyGridState()

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        state = gridState
    ) {
        items(
            channels,
            key = { it.id} ) { channel ->
            ChannelCard(
                channel = channel,
                onChannelClick = onChannelClick,
                onEpgClick = onEpgClick,
                onFavoriteClick = onFavoriteClick)
        }
    }
}