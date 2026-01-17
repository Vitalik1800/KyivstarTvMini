package com.vs18.kyivstartvmini.data.model

import org.simpleframework.xml.*

@Root(name = "tv", strict = false)
data class XmlTv(
    @field:ElementList(entry = "channel", inline = true, required = false)
    var channel: MutableList<XmlTvChannel> = mutableListOf(),

    @field:ElementList(
        entry = "programme",
        inline = true,
        required = false
    )
    var programme: MutableList<Programme> = mutableListOf()
)