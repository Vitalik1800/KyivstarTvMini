package com.vs18.kyivstartvmini

import com.vs18.kyivstartvmini.data.cleanChannelName
import org.junit.Assert.assertEquals
import org.junit.Test

class ChannelNameParserTest {

    @Test
    fun `parseChannelName removes metadata`() {
        assertEquals("ICTV", cleanChannelName("ICTV | HD | Kyiv"))
    }

    @Test
    fun `parseChannelName returns original if no delimiter`() {
        assertEquals("1+1", cleanChannelName("1+1"))
    }
}