package com.kapk.archsites.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kapk.archsites.models.ArchSiteModel

@Database(entities = arrayOf(ArchSiteModel::class), version = 1,  exportSchema = false)
abstract class Database : RoomDatabase() {

    abstract fun ArchSiteDao(): ArchSiteDao
}