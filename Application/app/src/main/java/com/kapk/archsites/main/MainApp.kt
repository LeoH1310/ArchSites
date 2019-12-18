package com.kapk.archsites.main

import android.app.Application
import com.kapk.archsites.models.ArchSiteModel
import org.jetbrains.anko.AnkoLogger

class MainApp : Application(),AnkoLogger {

    val archSites = ArrayList<ArchSiteModel>()

}