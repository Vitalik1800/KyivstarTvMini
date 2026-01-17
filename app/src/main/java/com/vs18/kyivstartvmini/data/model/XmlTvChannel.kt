package com.vs18.kyivstartvmini.data.model

import org.simpleframework.xml.*

@Root(name = "channel", strict = false)
data class XmlTvChannel(

    @field:Attribute(name = "id")
    var id: String = "",

    @field:ElementList(
        entry = "display-name",
        inline = true,
        required = false
    )
    var displayNames: MutableList<String> = mutableListOf()
)
