package com.vs18.kyivstartvmini.data.api

import com.vs18.kyivstartvmini.data.model.XmlTv
import retrofit2.http.*

interface EpgApi {

    @GET("epg2.xml")
    suspend fun getEpg(): XmlTv
}