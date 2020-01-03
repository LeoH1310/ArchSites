package com.kapk.archsites.views

import android.content.Intent
import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.kapk.archsites.models.ArchSiteModel
import com.kapk.archsites.models.Location
import com.kapk.archsites.views.archsite.ArchSiteView
import com.kapk.archsites.views.archsitelist.ArchSiteFavoriteView
import com.kapk.archsites.views.archsitelist.ArchSiteListView
import com.kapk.archsites.views.editlocation.EditLocationView
import com.kapk.archsites.views.login.LoginView
import com.kapk.archsites.views.map.ArchSiteMapView
import com.kapk.archsites.views.settings.SettingsView
import org.jetbrains.anko.AnkoLogger

const val ADD_IMAGE_REQUEST = 1
const val LOCATION_REQUEST = 2
const val CHANGE_IMAGE_REQUEST = 3

enum class VIEW {
    LOCATION, ARCHSITE, LIST, LOGIN, SETTINGS, MAPS, FAVORITES
}

abstract class BaseView : AppCompatActivity(), AnkoLogger {

    private var basePresenter: BasePresenter? = null

    fun navigateTo(view: VIEW, code: Int = 0, key: String = "", value: Parcelable? = null) {
        val intent = when (view) {
            VIEW.LOCATION -> Intent(this, EditLocationView::class.java)
            VIEW.ARCHSITE -> Intent(this, ArchSiteView::class.java)
            VIEW.MAPS -> Intent(this, ArchSiteMapView::class.java)
            VIEW.LIST -> Intent(this, ArchSiteListView::class.java)
            VIEW.LOGIN -> Intent(this, LoginView::class.java)
            VIEW.SETTINGS ->  Intent(this, SettingsView::class.java)
            VIEW.FAVORITES -> Intent(this, ArchSiteFavoriteView::class.java)
        }
        if (key != "") {
            intent.putExtra(key, value)
        }
        startActivityForResult(intent, code)
    }

    fun initPresenter(presenter: BasePresenter): BasePresenter {
        basePresenter = presenter
        return presenter
    }

    fun init(toolbar: Toolbar, upEnabled: Boolean) {
        toolbar.title = title
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(upEnabled)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            basePresenter?.doActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        basePresenter?.doRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    open fun showLocation(location : Location) {}
    open fun showArchSite(archSite: ArchSiteModel) {}
    open fun showArchSites(archSites: List<ArchSiteModel>) {}
    open fun showProgress() {}
    open fun hideProgress() {}
    open fun setImageSlider(archSite: ArchSiteModel) {}
}