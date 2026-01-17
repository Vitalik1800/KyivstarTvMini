package com.vs18.kyivstartvmini.viewmodel

import com.vs18.kyivstartvmini.data.model.Channel
import com.vs18.kyivstartvmini.domain.error.AppError

sealed class ChannelState {
    object Loading : ChannelState()
    data class Success(val channels: List<Channel>) : ChannelState()
    data class Error(val message: AppError) : ChannelState()
}