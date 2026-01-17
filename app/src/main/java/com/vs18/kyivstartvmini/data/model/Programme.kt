package com.vs18.kyivstartvmini.data.model

import org.simpleframework.xml.*

@Root(name = "programme", strict = false)
class Programme {

    @field:Attribute(name = "channel")
    lateinit var channel: String

    @field:Attribute(name = "start")
    lateinit var start: String

    @field:Attribute(name = "stop", required = false)
    var stop: String? = null

    @field:Element(name = "title", required = false)
    var title: String? = null

    @field:Element(name = "desc", required = false)
    var desc: String? = null
}
