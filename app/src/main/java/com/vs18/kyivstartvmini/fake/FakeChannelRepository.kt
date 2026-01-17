package com.vs18.kyivstartvmini.fake

import com.vs18.kyivstartvmini.data.model.Channel
import com.vs18.kyivstartvmini.data.repository.ChannelRepository

class FakeChannelRepository : ChannelRepository {

    override suspend fun getChannels(): Result<List<Channel>> {
        return Result.success(
            listOf(
                Channel(
                    id = 1,
                    name = "ICTV",
                    streamUrl = "url",
                    category = "News",
                    isFavorite = false,
                    tvgId = "1",
                    logoUrl = "url"
                )
            )
        )
    }

    override suspend fun toggleFavorite(channelId: Int) = Unit

    override suspend fun getAllIds(): List<Int> = emptyList()
}