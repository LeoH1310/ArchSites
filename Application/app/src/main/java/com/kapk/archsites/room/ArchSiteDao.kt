package com.kapk.archsites.room

import androidx.room.*
import com.kapk.archsites.models.ArchSiteModel

@Dao
interface ArchSiteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun create(archSite: ArchSiteModel)

    @Query("SELECT * FROM ArchSiteModel")
    fun findAll(): List<ArchSiteModel>

    @Query("select * from ArchSiteModel where id = :id")
    fun findById(id: Long): ArchSiteModel

    @Update
    fun update(archSite: ArchSiteModel)

    @Delete
    fun deleteArchSite(archSite: ArchSiteModel)
}