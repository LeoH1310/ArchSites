package com.kapk.archsites.models

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
class ArchSiteModel(@PrimaryKey(autoGenerate = true) var id: Long = 0,
                    var fbId : String = "",
                    var name: String = "",
                    var visited: Boolean = false,
                    var image: String = "",
                    @Embedded var location : Location = Location()): Parcelable

@Parcelize
data class Location(var lat: Double = 0.0,
                    var lng: Double = 0.0,
                    var zoom: Float = 0f) : Parcelable