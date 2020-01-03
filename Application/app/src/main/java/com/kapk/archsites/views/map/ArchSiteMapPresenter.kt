package com.kapk.archsites.views.map

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import com.kapk.archsites.models.ArchSiteModel
import com.kapk.archsites.views.BasePresenter
import com.kapk.archsites.views.BaseView
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class ArchSiteMapPresenter(view: BaseView) : BasePresenter(view) {

    private var coloredMarker: Marker? = null

    fun doPopulateMap(map: GoogleMap, archSites: List<ArchSiteModel>) {
        map.uiSettings.isZoomControlsEnabled = true

        val boundsBuilder = LatLngBounds.Builder()

        archSites.forEach {
            val loc = LatLng(it.location.lat, it.location.lng)
            val options = MarkerOptions().title(it.name).position(loc)
            map.addMarker(options).tag = it
            boundsBuilder.include(loc)
        }
        val bounds = boundsBuilder.build()
        map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50))
    }

    fun doMarkerSelected(marker: Marker) {
        doAsync {
            uiThread {
                val archSite = marker.tag as ArchSiteModel
                view?.showArchSite(archSite)
                if(coloredMarker != null)
                    coloredMarker!!.setIcon(BitmapDescriptorFactory.defaultMarker())
                marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                coloredMarker = marker
            }
        }
    }

    fun loadArchSites() {
        doAsync {
            val archSites = app.archSites.findAll()
            uiThread {
                view?.showArchSites(archSites)
            }
        }
    }
}