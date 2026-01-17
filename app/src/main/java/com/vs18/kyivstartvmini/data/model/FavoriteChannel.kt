package com.vs18.kyivstartvmini.data.model

import androidx.room.*

@Entity(
    tableName = "favorite_channels",
    primaryKeys = ["userId", "channelId"]
)
data class FavoriteChannel(
    val userId: String,
    val channelId: Int
)