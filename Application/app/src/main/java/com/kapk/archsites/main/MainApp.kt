package com.kapk.archsites.main

import android.app.Application
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import com.kapk.archsites.models.ArchSiteStore
import com.kapk.archsites.models.firebase.ArchSiteFireStore

class MainApp : Application(), AnkoLogger {

    lateinit var archSites: ArchSiteStore

    override fun onCreate() {
        super.onCreate()
        archSites = ArchSiteFireStore(applicationContext)
        info("ArchSites started")
    }

}