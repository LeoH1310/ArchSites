package com.kapk.archsites.views

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.kapk.archsites.models.ArchSiteModel
import com.kapk.archsites.models.Location
import org.jetbrains.anko.AnkoLogger

val IMAGE_REQUEST = 1
val LOCATION_REQUEST = 2

enum class VIEW {
    LOCATION, ARCHSITE, LIST, LOGIN
}

abstract class BaseView : AppCompatActivity(), AnkoLogger {

    var basePresenter: BasePresenter? = null

    fun initPresenter(presenter: BasePresenter): BasePresenter {
        basePresenter = presenter
        return presenter
    }

    fun init(toolbar: Toolbar, upEnabled: Boolean) {
        toolbar.title = title
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(upEnabled)
    }

    open fun showLocation(location : Location) {}
    open fun showArchSite(archSite: ArchSiteModel) {}
    open fun showArchSites(archSites: List<ArchSiteModel>) {}
    open fun showProgress() {}
    open fun hideProgress() {}
}