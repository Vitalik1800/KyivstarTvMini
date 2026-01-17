package com.vs18.kyivstartvmini.data.api.client

import com.vs18.kyivstartvmini.data.api.EpgApi
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory

object EpgClient {

    private val retrofit = Retrofit.Builder()
        .baseUrl("http://epg.one/")
        .addConverterFactory(SimpleXmlConverterFactory.createNonStrict())
        .build()

    val api: EpgApi = retrofit.create(EpgApi::class.java)

}