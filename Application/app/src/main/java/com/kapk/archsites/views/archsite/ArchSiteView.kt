package com.kapk.archsites.views.archsite

import android.app.AlertDialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.kapk.archsites.R
import com.kapk.archsites.helpers.checkLocationPermissions
import com.kapk.archsites.models.ArchSiteModel
import com.kapk.archsites.views.BaseView
import kotlinx.android.synthetic.main.activity_archsite.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.toast
import java.text.DateFormat.getDateInstance
import java.util.*
import kotlin.collections.ArrayList

class ArchSiteView : BaseView(), AnkoLogger {

    lateinit var presenter: ArchSitePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_archsite)
        init(toolbar, true)
        btn_addImage.setOnClickListener { presenter.doAddImage() }

        presenter = initPresenter (ArchSitePresenter(this)) as ArchSitePresenter

        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync {
            presenter.doConfigureMap(it)
            it.setOnMapClickListener {
                if (checkLocationPermissions(this))
                    presenter.doSetLocation() }
        }

        check_ArchSite_visited.setOnClickListener {
            if (check_ArchSite_visited.isChecked) {
                val dateVisited = "Visited\n" + getDateInstance().format(Date())
                txt_dateVisited.text = dateVisited
            }
            else
                txt_dateVisited.text = "Visited"
        }
    }

    override fun showArchSite(archSite: ArchSiteModel) {
        txt_ArchSite_Name.setText(archSite.name)
        txt_ArchSite_Description.setText(archSite.description)
        check_ArchSite_visited.isChecked = archSite.visited
        txt_dateVisited.text = archSite.dateVisited
        txt_ArchSite_Notes.setText(archSite.notes)
        btn_favorite.isChecked = archSite.favorite
        ratingBar.rating = archSite.rating
        setImageSlider(archSite)

        //Disable editing for ArchSitesSpecs
        txt_ArchSite_Name.isFocusable = archSite.editable
        txt_ArchSite_Description.isFocusable = archSite.editable

        var imageCounter = 0
        for (x in 0 until 4) {
            if (archSite.images[x] != "")
                imageCounter++
        }

        btn_addImage.isEnabled = (imageCounter < 4)

        this.showLocation(archSite.location)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.archsite_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_item_delete -> {
                presenter.doDelete()
            }
            R.id.menu_item_save -> {
                if (txt_ArchSite_Name.text.toString().isEmpty()) {
                    toast(R.string.toast_enterName)
                } else {
                    presenter.doAddOrSave(txt_ArchSite_Name.text.toString(), txt_ArchSite_Description.text.toString(), check_ArchSite_visited.isChecked,
                        txt_dateVisited.text.toString(), btn_favorite.isChecked, txt_ArchSite_Notes.text.toString(), ratingBar.rating)
                }
            }
        }
        return super.onOptionsItemSelected(item!!)
    }

    override fun setImageSlider(archSite: ArchSiteModel){
        val imageList = ArrayList<SlideModel>()
        // imageList.add(SlideModel("String Url" or R.drawable)
        // imageList.add(SlideModel("String Url" or R.drawable, "title") You can add title
        // imageList.add(SlideModel("String Url" or R.drawable, "title", true) Also you can add centerCrop scaleType for this image

        if (archSite.images[0] == ""){
            imageList.add(
                SlideModel(
                    R.drawable.add_new_image,true
                )
            )
        }
        else {
            for (x in 0 until archSite.images.size){
                if (archSite.images[x] != "") {
                    imageList.add(
                        SlideModel(
                            archSite.images[x], true
                        )
                    )
                }
            }
        }

        val imageSlider = findViewById<ImageSlider>(R.id.image_slider)
        imageSlider.setImageList(imageList)
        imageSlider.stopSliding()
        imageSlider.startSliding(5000)
        imageSlider.setItemClickListener(object : ItemClickListener {
            override fun onItemSelected(position: Int) {
                val alertDialogBuilder = AlertDialog.Builder(this@ArchSiteView)

                with(alertDialogBuilder){
                    setTitle("Change or Delete")
                    setMessage("Would you like to delete or replace the image?")
                    setPositiveButton(R.string.txt_btn_change) { _, _ ->
                        presenter.doChangeImage(position)
                    }

                    setNegativeButton(R.string.txt_btn_delete) { _, _ ->
                        presenter.doDeleteImage(position)
                    }

                    setNeutralButton(R.string.txt_btn_cancel) { _, _ ->
                    }

                    show()
                }
            }
        })
    }

    override fun onBackPressed() {
        presenter.doCancel()
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
        presenter.doRestartLocationUpdate()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }
}