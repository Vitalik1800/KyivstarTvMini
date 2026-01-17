package com.vs18.kyivstartvmini.data

import android.util.Log
import com.vs18.kyivstartvmini.data.model.Channel
import kotlinx.coroutines.*
import java.net.*

private val translateMap = mapOf(
    'а' to "a", 'б' to "b", 'в' to "v", 'г' to "h", 'ґ' to "g",
    'д' to "d", 'е' to "e", 'є' to "ye", 'ж' to "zh", 'з' to "z",
    'и' to "y", 'і' to "i", 'ї' to "yi", 'й' to "y", 'к' to "k",
    'л' to "l", 'м' to "m", 'н' to "n", 'о' to "o", 'п' to "p",
    'р' to "r", 'с' to "s", 'т' to "t", 'у' to "u", 'ф' to "f",
    'х' to "kh", 'ц' to "ts", 'ч' to "ch", 'ш' to "sh", 'щ' to "shch",
    'ь' to "", 'ю' to "yu", 'я' to "ya",
)

suspend fun loadChannels(url: String): List<Channel> = withContext(Dispatchers.IO) {
    val lines = URL(url).readText().lines()
    val channels = mutableListOf<Channel>()
    var id = 0
    var i = 0

    while (i < lines.size) {
        val line = lines[i]

        if (line.startsWith("#EXTINF")) {
            val name = parseChannelName(line)
            val logoUrl = parseLogoUrl(line) ?: buildLogoUrl(name)
            val category = parseCategory(line)
            val streamUrl = lines.getOrNull(i + 1)?.trim()

            val rawTvgId = parseTvgId(line)
            val tvgId = rawTvgId?.let { normalizeTvgId(it) }
                ?: normalizeTvgId(name)

            Log.d("LOGO", "$name -> $logoUrl")

                channels.add(
                    Channel(
                        id = id++,
                        tvgId = tvgId,
                        name = name,
                        logoUrl = logoUrl,
                        streamUrl = streamUrl!!,
                        category = category
                    )
                )
                Log.d("CATEGORY", "$name -> $category")
                i += 2
        } else i++
    }

    channels
}

fun parseTvgId(extinf: String): String? {
    val regex = Regex("""tvg-id=['"]([^'"]+)['"]""")
    return regex.find(extinf)?.groupValues?.get(1)
}

fun cleanChannelName(raw: String): String {
    return raw.substringBefore("|").trim()
}

fun parseChannelName(extinf: String): String {
    val rawName = extinf.substringAfter(",").trim()
    return cleanChannelName(rawName)
}

fun parseLogoUrl(extinf: String): String? {
    val regex = Regex("""tvg-logo=['"]([^'"]+)['"]""")
    return regex.find(extinf)?.groupValues?.get(1)
}

fun buildLogoUrl(name: String): String {
    val normalized = name
        .lowercase()
        .replace(" ", "-")
        .replace("+", "plus")
        .replace("'", "")
        .replace(",", "")

    return "https://iptvx.one/picons/$normalized.png"
}

fun normalizeTvgId(raw: String): String {
    val lowered = raw.lowercase()
    val transliterated = lowered.map { char ->
        translateMap[char] ?: char.toString() }.joinToString("")

    return transliterated
        .replace("ua:", "")
        .replace(" ua", "")
        .replace("ukraine", "ua")
        .replace("україна", "ukrayina")
        .replace("1+1", "1plus1")
        .replace("plus1", "1plus1")
        .replace("Cine+ Legend", "cine-plus-legend")
        .replace("+", "plus")
        .replace(" ", "")
        .replace("hd", "")
        .replace("fhd", "")
        .replace("uhd", "")
        .replace("4k", "")
        .replace("_", "")
        .replace("-", "")
        .replace("(", "")
        .replace(")", "")
        .trim()
}

fun parseCategory(extinf: String): String {
    val regex = Regex("""group-title=['"]([^'"]+)['"]""")
    return regex.find(extinf)?.groupValues?.get(1) ?: "General"
}