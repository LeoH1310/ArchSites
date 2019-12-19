package com.kapk.archsites.views.archsitelist

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.kapk.archsites.R
import com.kapk.archsites.models.ArchSiteModel
import com.kapk.archsites.views.BaseView
import com.kapk.archsites.views.archsite.ArchSiteAdapter
import com.kapk.archsites.views.archsite.ArchSiteListener
import kotlinx.android.synthetic.main.activity_archsite_list.*

class ArchSiteListView : BaseView(), ArchSiteListener {

    lateinit var presenter: ArchSiteListPresenter

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
}