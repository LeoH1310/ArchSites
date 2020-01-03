package com.kapk.archsites.views.map

import android.os.Bundle
import com.bumptech.glide.Glide
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.kapk.archsites.R
import com.kapk.archsites.models.ArchSiteModel
import com.kapk.archsites.views.BaseView
import kotlinx.android.synthetic.main.activity_archsite_map.*
import kotlinx.android.synthetic.main.content_archsite_map.*

class ArchSiteMapView : BaseView(), GoogleMap.OnMarkerClickListener {

    private lateinit var presenter: ArchSiteMapPresenter
    private lateinit var map : GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_archsite_map)
        super.init(toolbar, true)

        presenter = initPresenter (ArchSiteMapPresenter(this)) as ArchSiteMapPresenter

        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync {
            map = it
            map.setOnMarkerClickListener(this)
            presenter.loadArchSites()
        }
    }

    override fun showArchSite(archSite: ArchSiteModel) {
        val locationText = "Lat: " + "%.6f".format(archSite.location.lat) + " Lng: " + "%.6f".format(archSite.location.lng)
        txt_archsite_name.text = archSite.name
        txt_archsite_location.text = locationText
        Glide.with(this).load(archSite.images[0]).into(img_archsite)
    }

    override fun showArchSites(archSites: List<ArchSiteModel>) {
        presenter.doPopulateMap(map, archSites)
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        presenter.doMarkerSelected(marker)
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }
}