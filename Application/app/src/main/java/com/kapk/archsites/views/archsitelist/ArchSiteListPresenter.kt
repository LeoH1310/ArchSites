package com.kapk.archsites.views.archsitelist

import com.google.firebase.auth.FirebaseAuth
import com.kapk.archsites.models.ArchSiteModel
import com.kapk.archsites.views.BasePresenter
import com.kapk.archsites.views.BaseView
import com.kapk.archsites.views.VIEW
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class ArchSiteListPresenter(view: BaseView) : BasePresenter(view) {

    fun loadArchSites(onlyFavorites: Boolean) {
        doAsync {
            val archSites = if (onlyFavorites)
                app.archSites.findFav()
            else
                app.archSites.findAll()
            uiThread {
                view?.showArchSites(archSites)
            }
        }
    }

    fun doAddArchSite() {
        view?.navigateTo(VIEW.ARCHSITE)
    }

    fun doShowSettings() {
        view?.navigateTo(VIEW.SETTINGS)
    }

    fun doEditArchSite(archSite: ArchSiteModel) {
        view?.navigateTo(VIEW.ARCHSITE, 0, "archSite_edit", archSite)
    }

    fun doShowMap() {
       view?.navigateTo(VIEW.MAPS)
    }

    fun doLogout() {
        FirebaseAuth.getInstance().signOut()
        app.archSites.clear()
        view?.navigateTo(VIEW.LOGIN)
    }

    fun doShowFavorites(){
        view?.navigateTo(VIEW.FAVORITES)
    }
}