package com.kapk.archsites.views.archsite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kapk.archsites.R
import com.kapk.archsites.models.ArchSiteModel
import kotlinx.android.synthetic.main.card_archsite.view.*
import com.bumptech.glide.Glide

interface ArchSiteListener {
    fun onArchSiteClick(archSite: ArchSiteModel)
}

class ArchSiteAdapter constructor(private var archSites: List<ArchSiteModel>,
                                   private val listener: ArchSiteListener
) : RecyclerView.Adapter<ArchSiteAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        return MainHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.card_archsite,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val archSite = archSites[holder.adapterPosition]
        holder.bind(archSite, listener)
    }

    override fun getItemCount(): Int = archSites.size

    class MainHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(archSite: ArchSiteModel, listener: ArchSiteListener) {
            itemView.txt_siteName.text = archSite.name
            itemView.check_visited.isChecked = archSite.visited
            val locationText = "Lat: " + "%.6f".format(archSite.location.lat) + " Lng: " + "%.6f".format(archSite.location.lng)
            itemView.txt_siteLocation.text = locationText
            Glide.with(itemView.context).load(archSite.images[0]).into(itemView.siteImage);
            itemView.setOnClickListener { listener.onArchSiteClick(archSite) }
        }
    }
}



