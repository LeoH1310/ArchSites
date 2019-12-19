package com.kapk.archsites.room

import android.content.Context
import androidx.room.Room
import com.kapk.archsites.models.ArchSiteModel
import com.kapk.archsites.models.ArchSiteStore

class ArchSiteStoreRoom(val context: Context) : ArchSiteStore{

    var dao: ArchSiteDao

    init {
        val database = Room.databaseBuilder(context, Database::class.java, "room_sample.db")
            .fallbackToDestructiveMigration()
            .build()
        dao = database.ArchSiteDao()
    }

    override fun findAll(): List<ArchSiteModel> {
        return dao.findAll()
    }

    override fun findById(id: Long): ArchSiteModel? {
        return dao.findById(id)
    }

    override fun create(archSite: ArchSiteModel) {
        dao.create(archSite)
    }

    override fun update(archSite: ArchSiteModel) {
        dao.update(archSite)
    }

    override fun delete(archSite: ArchSiteModel) {
        dao.deleteArchSite(archSite)
    }

    override fun clear() {
    }
}