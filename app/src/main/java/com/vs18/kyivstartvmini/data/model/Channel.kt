package com.vs18.kyivstartvmini.data.model

data class Channel(
    val id: Int,
    val tvgId: String?,
    val name: String,
    val logoUrl: String?,
    val streamUrl: String,
    val category: String,
    val isFavorite: Boolean = false
)