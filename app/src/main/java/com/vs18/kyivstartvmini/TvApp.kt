package com.vs18.kyivstartvmini

import android.app.*
import coil.*
import com.vs18.kyivstartvmini.data.room.*

class TvApp : Application() {
    lateinit var database: AppDatabase

    override fun onCreate() {
        super.onCreate()
        database = AppDatabase.getDatabase(this)
        Coil.setImageLoader(
            ImageLoader.Builder(this)
                .crossfade(true)
                .build()
        )
    }
}