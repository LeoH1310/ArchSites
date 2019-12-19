package com.kapk.archsites.views.archsitelist

import com.kapk.archsites.models.ArchSiteModel
import com.kapk.archsites.views.BasePresenter
import com.kapk.archsites.views.BaseView
import com.kapk.archsites.views.VIEW
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class ArchSiteListPresenter(view: BaseView) : BasePresenter(view) {

    fun loadArchSites() {
        doAsync {
            val archSites = app.archSites.findAll()
            uiThread {
                view?.showArchSites(archSites)
            }
        }
    }

    //fun doAddArchSite() {
        //view?.navigateTo(VIEW.PLACEMARK)
    //}

    fun doEditArchSite(archSite: ArchSiteModel) {
        //view?.navigateTo(VIEW.ARCHSITE, 0, "archSite_edit", archSite)
    }

    //fun doShowArchSitesMap() {
        //view?.navigateTo(VIEW.MAPS)
    //}
}