package com.vs18.kyivstartvmini.player

import android.util.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.*
import androidx.compose.ui.viewinterop.*
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.ext.ima.*
import com.google.android.exoplayer2.source.*
import com.google.android.exoplayer2.ui.*
import androidx.core.net.*
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.vs18.kyivstartvmini.data.model.PlayerUiState
import com.vs18.kyivstartvmini.ui.error.PlayerErrorOverlay

@Composable
fun ImaAdsVideoPlayer(
    channels: List<String>,
    streamUrl: String,
    onError: (String) -> Unit,
) {
    val context = LocalContext.current
    val tag = "ImaAds"

    var uiState by remember {
        mutableStateOf(PlayerUiState())
    }

    val adTagUri = remember {
        val correlator = System.currentTimeMillis()
        ("https://pubads.g.doubleclick.net/gampad/ads?" +
                "iu=/21775744923/exoplayer_preroll&" +
                "description_url=https%3A%2F%2Fdevelopers.google.com%2Finteractive-media-ads&" +
                "tfcd=0&npa=0&sz=640x480&gdfp_req=1&output=vast&" +
                "unviewed_position_start=1&env=vp&impl=s&" +
                "correlator=$correlator").toUri()
    }

    val trackSelector = remember {
        DefaultTrackSelector(context).apply {
            setParameters(
                buildUponParameters()
                    .setSelectUndeterminedTextLanguage(true)
            )
        }
    }

    val renderersFactory = remember {
        DefaultRenderersFactory(context)
            .setEnableDecoderFallback(true)
    }

    val adsLoader = remember {
        ImaAdsLoader.Builder(context).build()
    }

    val player = remember {
        ExoPlayer.Builder(context)
            .setRenderersFactory(renderersFactory)
            .setTrackSelector(trackSelector)
            .setMediaSourceFactory(
                DefaultMediaSourceFactory(context)
                    .setAdsLoaderProvider { adsLoader })
            .build()
            .apply {
                adsLoader.setPlayer(this)

                addListener(object : Player.Listener {
                    override fun onPlayerError(error: PlaybackException) {
                        Log.e(tag, "Stream error", error)

                        uiState = uiState.copy(
                            hasError = true,
                            errorMessage = error.errorCodeName
                        )

                        playNextChannel()
                    }

                    override fun onPlaybackStateChanged(playbackState: Int) {
                        if (playbackState == Player.STATE_ENDED) {
                            playNextChannel()
                        }
                    }

                    private fun playNextChannel() {
                        if (channels.isEmpty()) return

                        val nextIndex =
                            (uiState.channelIndex + 1) % channels.size

                        uiState = uiState.copy(
                            channelIndex = nextIndex,
                            hasError = false,
                            errorMessage = null,
                            playbackPosition = 0L
                        )

                        val mediaItem = MediaItem.Builder()
                            .setUri(channels[nextIndex])
                            .setAdTagUri(adTagUri)
                            .build()

                        setMediaItem(mediaItem)
                        prepare()
                        playWhenReady = true

                        Log.d(tag, "Switched to channel $nextIndex")
                    }
                })
            }
    }

    val playerView = remember {
        PlayerView(context).apply {
            useController = true
            setShowBuffering(PlayerView.SHOW_BUFFERING_WHEN_PLAYING)
        }
    }

    LaunchedEffect(streamUrl) {
        try {
            if (channels.isEmpty()) return@LaunchedEffect

            val mediaItem = MediaItem.Builder()
                .setUri(channels[uiState.channelIndex])
                .setAdTagUri(adTagUri)
                .build()

            player.setMediaItem(mediaItem)
            player.prepare()
            player.playWhenReady = true

            Log.d(tag, "Playback started with IMA ads")

            Log.d(tag, "Playing with ad: $adTagUri")
        } catch (e: Exception) {
            onError("Playback failed: ${e.message}")
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            uiState = uiState.copy(
                playbackPosition = player.currentPosition,
                playWhenReady = player.playWhenReady
            )

            player.release()
            adsLoader.setPlayer(null)
            adsLoader.release()

            Log.d(tag, "Player released with state saved")
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { playerView },
            modifier = Modifier.fillMaxSize()
        ) { it.player = player}

        if (uiState.hasError) {
            PlayerErrorOverlay(
                message = uiState.errorMessage ?: "Помилка відтворення",
                onRetry = {
                    uiState = uiState.copy(hasError = false)
                    player.prepare()
                    player.playWhenReady = true
                })
        }
    }
}