package com.kapk.archsites.main

import android.app.Application
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import com.kapk.archsites.models.ArchSiteStore
import com.kapk.archsites.room.ArchSiteStoreRoom

class MainApp : Application(), AnkoLogger {

    lateinit var archSites: ArchSiteStore

    override fun onCreate() {
        super.onCreate()
        archSites = ArchSiteStoreRoom(applicationContext)
        info("ArchSites started")
    }

}