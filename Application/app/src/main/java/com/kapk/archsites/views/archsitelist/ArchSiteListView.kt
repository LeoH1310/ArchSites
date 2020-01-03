package com.kapk.archsites.views.archsitelist

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.kapk.archsites.R
import com.kapk.archsites.models.ArchSiteModel
import com.kapk.archsites.views.BaseView
import com.kapk.archsites.views.archsite.ArchSiteAdapter
import com.kapk.archsites.views.archsite.ArchSiteListener
import kotlinx.android.synthetic.main.activity_archsite_list.*

class ArchSiteListView : BaseView(), ArchSiteListener {

    private lateinit var presenter: ArchSiteListPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_archsite_list)
        init(toolbar, false)

        presenter = initPresenter(ArchSiteListPresenter(this)) as ArchSiteListPresenter

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        presenter.loadArchSites()
    }

    override fun showArchSites(archSites: List<ArchSiteModel>) {
        recyclerView.adapter =
            ArchSiteAdapter(archSites, this)
        recyclerView.adapter?.notifyDataSetChanged()
    }

    override fun onArchSiteClick(archSite: ArchSiteModel) {
        presenter.doEditArchSite(archSite)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_item_add-> presenter.doAddArchSite()
            R.id.menu_item_map -> presenter.doShowMap()
            R.id.menu_item_settings ->presenter.doShowSettings()
            R.id.menu_item_logout ->presenter.doLogout()
        }
        return super.onOptionsItemSelected(item!!)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        presenter.loadArchSites()
        super.onActivityResult(requestCode, resultCode, data)
    }
}