package com.vs18.kyivstartvmini.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vs18.kyivstartvmini.data.repository.ChannelRepository

class ChannelViewModelFactory(
    private val repository: ChannelRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ChannelViewModel(repository) as T
    }
}
