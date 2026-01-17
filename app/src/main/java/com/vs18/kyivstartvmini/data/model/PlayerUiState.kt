package com.vs18.kyivstartvmini.data.model

data class PlayerUiState(
    val channelIndex: Int = 0,
    val playbackPosition: Long = 0L,
    val playWhenReady: Boolean = true,
    val hasError: Boolean = false,
    val errorMessage: String? = null
)
