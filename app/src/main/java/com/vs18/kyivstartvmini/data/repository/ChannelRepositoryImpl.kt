
package com.vs18.kyivstartvmini.data.repository
import com.vs18.kyivstartvmini.data.model.FavoriteChannel
import com.vs18.kyivstartvmini.data.room.FavoriteChannelDao
import com.vs18.kyivstartvmini.data.source.ChannelRemoteSource

class ChannelRepositoryImpl (
    private val remote: ChannelRemoteSource = ChannelRemoteSource(),
    private val favoriteDao: FavoriteChannelDao,
    private val userId: String
) : ChannelRepository {

    override suspend fun getChannels() = runCatching {
        val channels = remote.fetchChannels()
        val favoriteIds = favoriteDao.getAllIds(userId)
        channels.map {
            it.copy(isFavorite = it.id in favoriteIds)
        }
    }
    override suspend fun toggleFavorite(channelId: Int) {
        if (favoriteDao.isFavorite(userId, channelId)) {
            favoriteDao.remove(userId, channelId)
        } else {
            favoriteDao.add(
                FavoriteChannel(
                    userId = userId,
                    channelId = channelId
                )
            )
        }
    }
    override suspend fun getAllIds(): List<Int> =
        favoriteDao.getAllIds(userId)
}