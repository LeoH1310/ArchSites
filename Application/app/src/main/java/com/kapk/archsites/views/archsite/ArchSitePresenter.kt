package com.kapk.archsites.views.archsite

import android.content.Intent
import com.kapk.archsites.helpers.showImagePicker
import com.kapk.archsites.models.ArchSiteModel
import com.kapk.archsites.views.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class ArchSitePresenter(view: BaseView) : BasePresenter(view) {

    var archSite = ArchSiteModel()
    var edit = false
    var imageHolder = ""

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

    fun doAddImage() {
        view?.let {
            showImagePicker(view!!, ADD_IMAGE_REQUEST)
        }
    }

    fun doChangeImage(pos: Int) {
        view?.let {
            showImagePicker(view!!, CHANGE_IMAGE_REQUEST + pos)
        }
    }

    fun doDeleteImage(pos:Int){
        archSite.images[pos] = ""
        for (x in pos until archSite.images.size - 1){
            archSite.images[pos] = archSite.images[pos+1]
        }
        view?.showArchSite(archSite)
    }

    override fun doActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        when (requestCode) {
            ADD_IMAGE_REQUEST -> {
                //add image as last image in the list
                for (x in 0 until 4) {
                    if (archSite.images[x] == "") {
                        archSite.images[x] = (data.data.toString())
                        break
                    }
                }
                view?.showArchSite(archSite)
            }
            LOCATION_REQUEST -> {
                /*val location = data.extras?.getParcelable<Location>("location")!!
                placemark.location = location
                locationUpdate(location)*/
            }
            else ->{
                //change image at one position in the list
                archSite.images[requestCode - CHANGE_IMAGE_REQUEST] = (data.data.toString())
                view?.showArchSite(archSite)
            }
        }
    }
}