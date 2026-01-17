package com.vs18.kyivstartvmini.data.repository

import android.util.Log
import com.vs18.kyivstartvmini.data.api.client.EpgClient
import com.vs18.kyivstartvmini.data.model.ProgramItem
import com.vs18.kyivstartvmini.data.normalizeTvgId

class EpgRepository {

    private var cache: Map<String, List<ProgramItem>> = emptyMap()

    suspend fun loadOnce() {
        if (cache.isNotEmpty()) return

        val epg = EpgClient.api.getEpg()

        val nameToId = mutableMapOf<String, String>()

        epg.channel.forEach { xmlTvChannel ->
            xmlTvChannel.displayNames.forEach { rawName ->
                val normalized = normalizeTvgId(rawName)
                nameToId[normalized] = xmlTvChannel.id
            }
        }

        val programsByEpgId = epg.programme
            .groupBy { it.channel }
            .mapValues { (_, list) ->
                list.map {
                    ProgramItem(
                        it.title ?: "Без назви",
                        it.desc ?: "",
                        it.start,
                        it.stop ?: ""
                    )
                }
            }

        cache = nameToId.mapValues { (_, epgId) ->
            programsByEpgId[epgId].orEmpty()
        }

        Log.d("EPG", "cache keys sample: ${cache.keys.take(10)}")
        Log.d("EPG", "nameToId size: ${nameToId.size}")
    }

    suspend fun getForChannel(tvgId: String): List<ProgramItem> {
        loadOnce()
        Log.d("EPG", "Request EPG for: $tvgId")
        return cache[tvgId].orEmpty()
    }
}
