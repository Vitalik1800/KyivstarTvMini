package com.vs18.kyivstartvmini.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.*
import com.vs18.kyivstartvmini.data.model.Channel
import com.vs18.kyivstartvmini.data.repository.*
import com.vs18.kyivstartvmini.domain.error.AppError
import kotlinx.coroutines.launch

class ChannelViewModel(
    private val repository: ChannelRepository
) : ViewModel() {

    private val _allChannels = mutableStateListOf<Channel>()
    var state by mutableStateOf<ChannelState>(ChannelState.Loading)
        private set

    var searchQuery by mutableStateOf("")
        private set

    var showFavoritesOnly by mutableStateOf(false)
        private set

    var selectedCategory by mutableStateOf<String?>(null)
        private set

    val categories: List<String>
        get() = _allChannels.map { it.category }.distinct().sorted()

    init {
        load()
    }

    fun onUserChanged() {
        load()
    }

    fun onSearchChange(query: String) {
        searchQuery = query
        applyFilter()
    }

    fun toggleFavoritesFilter() {
        showFavoritesOnly = !showFavoritesOnly
        applyFilter()
    }

    fun toggleFavorite(channelId: Int) {
        viewModelScope.launch {
            repository.toggleFavorite(channelId)
            updateLocalFavorite(channelId)
            applyFilter()
        }
    }

    private fun load() {
        viewModelScope.launch {
            state = ChannelState.Loading

            repository.getChannels()
                .onSuccess { channels ->

                    val favoriteIds = repository.getAllIds()

                    _allChannels.clear()
                    _allChannels.addAll(
                        channels.map {
                            it.copy(isFavorite = it.id in favoriteIds)
                        }
                    )

                    applyFilter()
                }
                .onFailure {
                    state = ChannelState.Error(AppError.Unknown(it.message))
                }
        }
    }

    private fun applyFilter() {
        val filtered = _allChannels
            .filter {
                it.name.contains(searchQuery, ignoreCase = true)
            }
            .filter {
                !showFavoritesOnly || it.isFavorite
            }
            .filter {
                selectedCategory == null || it.category == selectedCategory
            }

        state = ChannelState.Success(filtered)
    }

    private fun updateLocalFavorite(channelId: Int) {
        val index = _allChannels.indexOfFirst { it.id == channelId }
        if (index != -1) {
            val channel = _allChannels[index]
            _allChannels[index] = channel.copy(isFavorite = !channel.isFavorite)
        }
    }

    fun currentChannelById(id: Int): Channel {
        return _allChannels.first { it.id == id }
    }

    fun selectCategory(category: String?) {
        selectedCategory = category
        applyFilter()
    }
}