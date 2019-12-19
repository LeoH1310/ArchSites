package com.kapk.archsites.views.archsite

import com.kapk.archsites.models.ArchSiteModel
import com.kapk.archsites.views.BasePresenter
import com.kapk.archsites.views.BaseView
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class ArchSitePresenter(view: BaseView) : BasePresenter(view) {

    var archSite = ArchSiteModel()
    var edit = false
    //var map: GoogleMap? = null
    //var defaultLocation = Location(52.245696, -7.139102, 15f)
    //var locationService: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(view)
    //val locationRequest = createDefaultLocationRequest()

    init {
        if (view.intent.hasExtra("archSite_edit")) {
            edit = true
            archSite = view.intent.extras?.getParcelable<ArchSiteModel>("archSite_edit")!!
            view.showArchSite(archSite)
        } else {
            //if (checkLocationPermissions(view)) {
                //doSetCurrentLocation()
            //}
        }
    }

    fun doAddOrSave(title: String, description: String, visited: Boolean) {
        archSite.name = title
        archSite.description = description
        archSite.visited = visited
        doAsync {
            if (edit) {
                app.archSites.update(archSite)
            } else {
                app.archSites.create(archSite)
            }
            uiThread {
                view?.finish()
            }
        }
    }

    fun doCancel() {
        view?.finish()
    }

    fun doDelete() {
        doAsync {
            app.archSites.delete(archSite)
            uiThread {
                view?.finish()
            }
        }
    }
}