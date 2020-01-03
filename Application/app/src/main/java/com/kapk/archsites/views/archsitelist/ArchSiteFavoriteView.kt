package com.kapk.archsites.views.archsitelist

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.kapk.archsites.R
import com.kapk.archsites.models.ArchSiteModel
import com.kapk.archsites.views.BaseView
import kotlinx.android.synthetic.main.activity_archsite_list.*
import org.jetbrains.anko.toast

class ArchSiteFavoriteView : BaseView(),
    ArchSiteListener {

    private lateinit var presenter: ArchSiteListPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_archsite_list)
        init(toolbar, true)

        presenter = initPresenter(ArchSiteListPresenter(this)) as ArchSiteListPresenter

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        presenter.loadArchSites(onlyFavorites = true)
    }

    override fun showArchSites(archSites: List<ArchSiteModel>) {
        recyclerView.adapter =
            ArchSiteAdapter(
                archSites,
                this
            )
        recyclerView.adapter?.notifyDataSetChanged()
        if (archSites.isEmpty())
            toast(R.string.toast_noFavorites)
    }

    override fun onArchSiteClick(archSite: ArchSiteModel) {
        presenter.doEditArchSite(archSite)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        presenter.loadArchSites(onlyFavorites = true)
        super.onActivityResult(requestCode, resultCode, data)
    }
}