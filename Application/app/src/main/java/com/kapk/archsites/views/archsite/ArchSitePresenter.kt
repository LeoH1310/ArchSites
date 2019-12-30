package com.kapk.archsites.views.archsite

import android.annotation.SuppressLint
import android.content.Intent
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.kapk.archsites.helpers.checkLocationPermissions
import com.kapk.archsites.helpers.createDefaultLocationRequest
import com.kapk.archsites.helpers.isPermissionGranted
import com.kapk.archsites.helpers.showImagePicker
import com.kapk.archsites.models.ArchSiteModel
import com.kapk.archsites.models.Location
import com.kapk.archsites.views.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread

class ArchSitePresenter(view: BaseView) : BasePresenter(view) {

    var archSite = ArchSiteModel()
    var edit = false
    var locManualChanged = false

    var map: GoogleMap? = null
    var defaultLocation = Location(52.245696, -7.139102, 15f)
    var locationService: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(view)
    val locationRequest = createDefaultLocationRequest()

    lateinit var locationCallback: LocationCallback

    init {
        if (view.intent.hasExtra("archSite_edit")) {
            edit = true
            archSite = view.intent.extras?.getParcelable<ArchSiteModel>("archSite_edit")!!
            view.showArchSite(archSite)
        } else {
            if (checkLocationPermissions(view))
                doSetCurrentLocation()
        }
    }

    @SuppressLint("MissingPermission")
    fun doSetCurrentLocation() {
        locationService.lastLocation.addOnSuccessListener {
            locationUpdate(Location(it.latitude, it.longitude))
        }
    }

    @SuppressLint("MissingPermission")
    fun doRestartLocationUpdate() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                if (locationResult != null && locationResult.locations != null) {
                    val l = locationResult.locations.last()
                    locationUpdate(Location(l.latitude, l.longitude))
                }
            }
        }
        if (!edit and !locManualChanged) {
            locationService.requestLocationUpdates(locationRequest, locationCallback, null)
        }
    }

    override fun doRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (isPermissionGranted(requestCode, grantResults)) {
            doSetCurrentLocation()
        } else {
            locationUpdate(defaultLocation)
        }
    }

    fun doConfigureMap(m: GoogleMap) {
        map = m
        locationUpdate(archSite.location)
    }

    fun locationUpdate(location: Location) {
        archSite.location = location
        archSite.location.zoom = 15f
        map?.clear()
        val options = MarkerOptions().title(archSite.name).position(LatLng(archSite.location.lat, archSite.location.lng))
        map?.addMarker(options)
        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(archSite.location.lat, archSite.location.lng), archSite.location.zoom))
        view?.showLocation(archSite.location)
    }

    fun doSetLocation() {
        if(archSite.editable)
            view?.navigateTo(VIEW.LOCATION, LOCATION_REQUEST, "location", Location(archSite.location.lat, archSite.location.lng, archSite.location.zoom))
        else
            view?.toast("Location of given site is not editable")
   }

    fun doAddOrSave(title: String, description: String, visited: Boolean, dateVisited: String, favorite: Boolean, notes: String) {
        archSite.name = title
        archSite.description = description
        archSite.visited = visited
        archSite.dateVisited = dateVisited
        archSite.favorite = favorite
        archSite.notes = notes
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
        if(archSite.editable) {
            doAsync {
                app.archSites.delete(archSite)
                uiThread {
                    view?.finish()
                }
            }
        }
        else
            view?.toast("A given site can not be deleted")
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
                val location = data.extras?.getParcelable<Location>("location")!!
                locationService.removeLocationUpdates(locationCallback)
                locManualChanged = true
                archSite.location = location
                locationUpdate(location)
            }
            else ->{
                //change image at one position in the list
                archSite.images[requestCode - CHANGE_IMAGE_REQUEST] = (data.data.toString())
                view?.showArchSite(archSite)
            }
        }
    }
}