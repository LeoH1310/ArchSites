package com.kapk.archsites.models

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class ArchSiteModel(@PrimaryKey(autoGenerate = true) var id: Long = 0,
                         var fbId : String = "",
                         var editable: Boolean = true,
                         var name: String = "",
                         var description: String = "",
                         var notes: String = "",
                         var favorite: Boolean = false,
                         var visited: Boolean = false,
                         var dateVisited: String = "Visited",
                         var rating: Float = 0f,
                         var images: MutableList<String> = MutableList(4) { "" },
                         @Embedded var location : Location = Location()): Parcelable

@Parcelize
data class Location(var lat: Double = 0.0,
                    var lng: Double = 0.0,
                    var zoom: Float = 10f) : Parcelable

