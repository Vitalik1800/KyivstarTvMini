package com.vs18.kyivstartvmini.data.repository

import com.vs18.kyivstartvmini.data.model.Channel

interface ChannelRepository {
    suspend fun getAllIds(): List<Int>
    suspend fun getChannels(): Result<List<Channel>>
    suspend fun toggleFavorite(channelId: Int)

}