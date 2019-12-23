package com.kapk.archsites.views.archsite

import android.content.Intent
import com.kapk.archsites.helpers.showImagePicker
import com.kapk.archsites.models.ArchSiteModel
import com.kapk.archsites.views.BasePresenter
import com.kapk.archsites.views.BaseView
import com.kapk.archsites.views.IMAGE_REQUEST
import com.kapk.archsites.views.LOCATION_REQUEST
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

    fun doSelectImage() {
        view?.let {
            showImagePicker(view!!, IMAGE_REQUEST)
        }
    }

    override fun doActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        when (requestCode) {
            IMAGE_REQUEST -> {
                //TODO: entscheiden ob ändern oder hinzufügen
                archSite.images[0] = (data.data.toString())
                view?.setImageSlider(archSite)
            }
            LOCATION_REQUEST -> {
                /*val location = data.extras?.getParcelable<Location>("location")!!
                placemark.location = location
                locationUpdate(location)*/
            }
        }
    }
}