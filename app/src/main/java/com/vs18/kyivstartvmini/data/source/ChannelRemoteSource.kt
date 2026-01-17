package com.vs18.kyivstartvmini.data.source

import com.vs18.kyivstartvmini.data.loadChannels
import com.vs18.kyivstartvmini.data.model.Channel

class ChannelRemoteSource() {

    suspend fun fetchChannels(): List<Channel> {

        return loadChannels(
            "https://raw.githubusercontent.com/Vitalik1800/iptv/refs/heads/main/ua.m3u"
        )
    }

}