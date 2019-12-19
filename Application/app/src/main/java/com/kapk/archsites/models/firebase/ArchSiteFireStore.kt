package org.wit.placemark.models.firebase

import android.content.Context
import android.graphics.Bitmap
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
//import com.google.firebase.storage.FirebaseStorage
//import com.google.firebase.storage.StorageReference
import com.kapk.archsites.models.ArchSiteModel
import com.kapk.archsites.models.ArchSiteStore
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import java.io.ByteArrayOutputStream
import java.io.File


class ArchSiteFireStore(val context: Context) : ArchSiteStore, AnkoLogger {

    var archSites = ArrayList<ArchSiteModel>()
    lateinit var userId: String
    lateinit var db: DatabaseReference
    //lateinit var st: StorageReference

    override fun findAll(): List<ArchSiteModel> {
        return archSites
    }

    override fun findById(id: Long): ArchSiteModel? {
        return archSites.find { p -> p.id == id }
    }

    override fun create(archSite: ArchSiteModel) {
        val key = db.child("users").child(userId).child("archSites").push().key
        key?.let {
            archSite.fbId = key
            archSites.add(archSite)
            db.child("users").child(userId).child("archSites").child(key).setValue(archSite)
            //updateImage(archSite)
        }
    }

    override fun update(archSite: ArchSiteModel) {
        val foundArchSite: ArchSiteModel? = archSites.find { p -> p.fbId == archSite.fbId }
        if (foundArchSite != null) {
            foundArchSite.name = archSite.name
            foundArchSite.description = archSite.description
            foundArchSite.visited = archSite.visited
            //foundArchSite.image = placemark.image
            //foundArchSite.location = placemark.location
        }

        db.child("users").child(userId).child("archSites").child(archSite.fbId).setValue(archSite)
        /*
        if ((placemark.image.length) > 0 && (placemark.image[0] != 'h')) {
            updateImage(placemark)
        }
        */
    }

    override fun delete(archSite: ArchSiteModel) {

        db.child("users").child(userId).child("archSites").child(archSite.fbId).removeValue()
        archSites.remove(archSite)
        /*
        if (archSite.image != "") {
            val imageRef = FirebaseStorage.getInstance().getReferenceFromUrl(archSite.image)
            imageRef.delete().addOnFailureListener {
                println(it.message)
            }.addOnSuccessListener {
                db.child("users").child(userId).child("archSites").child(archSite.fbId).removeValue()
                archSites.remove(archSite)
            }
        }
        */
    }

    override fun clear() {
        archSites.clear()
    }

    /*
    fun updateImage(placemark: ArchSiteModel) {
        if (placemark.image != "") {
            val fileName = File(placemark.image)
            val imageName = fileName.getName()

            var imageRef = st.child(userId + '/' + imageName)
            val baos = ByteArrayOutputStream()
            val bitmap = readImageFromPath(context, placemark.image)

            bitmap?.let {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val data = baos.toByteArray()
                val uploadTask = imageRef.putBytes(data)
                uploadTask.addOnFailureListener {
                    println(it.message)
                }.addOnSuccessListener { taskSnapshot ->
                    taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
                        placemark.image = it.toString()
                        db.child("users").child(userId).child("placemarks").child(placemark.fbId).setValue(placemark)
                    }
                }
            }
        }
    }
    */

    fun fetchArchSites(archSitesReady: () -> Unit) {
        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(dataSnapshot: DatabaseError) {
            }
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot!!.children.mapNotNullTo(archSites) { it.getValue<ArchSiteModel>(ArchSiteModel::class.java) }
                archSitesReady()
            }
        }
        userId = FirebaseAuth.getInstance().currentUser!!.uid
        db = FirebaseDatabase.getInstance().reference
        //st = FirebaseStorage.getInstance().reference
        archSites.clear()
        db.child("users").child(userId).child("archSites").addListenerForSingleValueEvent(valueEventListener)
    }
}