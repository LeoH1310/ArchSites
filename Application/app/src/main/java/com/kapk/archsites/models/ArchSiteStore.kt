package com.kapk.archsites.models

interface ArchSiteStore {
    fun findAll(): List<ArchSiteModel>
    fun findFav(): List<ArchSiteModel>
    fun create(archSite: ArchSiteModel)
    fun update(archSite: ArchSiteModel)
    fun delete(archSite: ArchSiteModel)
    fun findById(id:Long) : ArchSiteModel?
    fun clear()
}