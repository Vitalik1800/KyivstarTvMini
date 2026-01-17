package com.vs18.kyivstartvmini.data.room

import androidx.room.*
import com.vs18.kyivstartvmini.data.model.FavoriteChannel

@Dao
interface FavoriteChannelDao {

    @Query("SELECT channelId FROM favorite_channels WHERE userId = :userId")
    suspend fun getAllIds(userId: String): List<Int>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun add(favorite: FavoriteChannel)

    @Query("""
        DELETE FROM favorite_channels 
        WHERE userId = :userId AND channelId = :channelId
    """)
    suspend fun remove(userId: String, channelId: Int)

    @Query("""
        SELECT EXISTS(
            SELECT 1 FROM favorite_channels 
            WHERE userId = :userId AND channelId = :channelId
        )
    """)
    suspend fun isFavorite(userId: String, channelId: Int): Boolean
}