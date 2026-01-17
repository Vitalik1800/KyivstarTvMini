package com.vs18.kyivstartvmini.ui.channel

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vs18.kyivstartvmini.data.model.*
import com.vs18.kyivstartvmini.data.repository.ChannelRepositoryImpl
import com.vs18.kyivstartvmini.data.room.AppDatabase
import com.vs18.kyivstartvmini.data.source.ChannelRemoteSource
import com.vs18.kyivstartvmini.domain.error.AppError
import com.vs18.kyivstartvmini.ui.categories.CategoryRow
import com.vs18.kyivstartvmini.ui.epg.EpgScreen
import com.vs18.kyivstartvmini.viewmodel.ChannelState
import com.vs18.kyivstartvmini.viewmodel.ChannelViewModel
import com.vs18.kyivstartvmini.ui.error.ErrorView
import com.vs18.kyivstartvmini.ui.search.SearchBar
import com.vs18.kyivstartvmini.viewmodel.ChannelViewModelFactory

@Composable
fun ChannelListScreen(
    email: String?,
    onChannelClick: (Channel) -> Unit,
    onProfileClick: () -> Unit,
) {

    if (email == null) {
        ErrorView(AppError.Unknown("Користувач не авторизований"))
        return
    }

    val userId = email.lowercase().trim()

    val context = LocalContext.current

    val db = remember {
        AppDatabase.getDatabase(context)
    }

    val repository = remember(userId) {
        ChannelRepositoryImpl(
            ChannelRemoteSource(),
            db.favoriteChannelDao(),
            userId
        )
    }

    val viewModel: ChannelViewModel = viewModel(
        key = "ChannelViewModel_$userId",
        factory = ChannelViewModelFactory(repository)
    )

    val state = viewModel.state

    var selectedChannel by remember { mutableStateOf<Channel?>(null) }

    BackHandler {
        when {
            selectedChannel != null -> {
                selectedChannel = null
            }

            viewModel.showFavoritesOnly -> {
                viewModel.toggleFavoritesFilter()
            }

            else -> {
                
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                "Kyivstar TV mini",
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium
            )

            Row {
                IconButton(onClick = { viewModel.toggleFavoritesFilter()  }) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = null,
                        tint = if (viewModel.showFavoritesOnly)
                            Color.Red else Color.White
                    )
                }

                IconButton(onClick = onProfileClick) {
                    Icon(Icons.Default.AccountCircle, null, tint = Color.White)
                }
            }

        }

        SearchBar(
            query = viewModel.searchQuery,
            onQueryChange = viewModel::onSearchChange
        )

        Spacer(Modifier.height(8.dp))

        CategoryRow(
            categories = viewModel.categories,
            selected = viewModel.selectedCategory,
            onSelect = viewModel::selectCategory
        )

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            when (val s = state) {
                is ChannelState.Loading -> {
                    CircularProgressIndicator(color = Color.White)
                }

                is ChannelState.Error -> {
                    Text(
                        text = "Помилка: ${s.message}",
                        color = Color.Red
                    )
                }

                is ChannelState.Success -> {
                    ChannelGrid(
                        channels = s.channels,
                        onChannelClick = { channel ->
                            onChannelClick(channel)
                        },
                        onEpgClick = { channel ->
                            selectedChannel = channel
                        },
                        onFavoriteClick = { id ->
                            viewModel.toggleFavorite(id)
                        }
                    )

                    selectedChannel?.let { channel ->
                        Dialog(onDismissRequest = { selectedChannel = null }) {
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = Color.Black,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight(0.8f)
                            ) {
                                if (channel.tvgId != null) {
                                    EpgScreen(tvgId = channel.tvgId)
                                } else {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            "Для цього каналу немає EPG",
                                            color = Color.Gray
                                        )
                                    }
                                }
                            }
                        }
                    }

                }

                is ChannelState.Error -> {
                    ErrorView(s.message)
                }
            }
        }
    }
}