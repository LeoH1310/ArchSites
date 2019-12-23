package org.wit.placemark.models.firebase

//import com.google.firebase.storage.FirebaseStorage
//import com.google.firebase.storage.StorageReference

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.kapk.archsites.helpers.readImageFromPath
import com.kapk.archsites.models.ArchSiteModel
import com.kapk.archsites.models.ArchSiteStore
import org.jetbrains.anko.AnkoLogger
import java.io.ByteArrayOutputStream
import java.io.File


class ArchSiteFireStore(val context: Context) : ArchSiteStore, AnkoLogger {

    var archSites = ArrayList<ArchSiteModel>()
    lateinit var userId: String
    lateinit var db: DatabaseReference
    lateinit var st: StorageReference

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
            updateImages(archSite)
        }
    }

    override fun update(archSite: ArchSiteModel) {

        val foundArchSite: ArchSiteModel? = archSites.find { p -> p.fbId == archSite.fbId }
        if (foundArchSite != null) {
            foundArchSite.name = archSite.name
            foundArchSite.description = archSite.description
            foundArchSite.visited = archSite.visited
            foundArchSite.images = archSite.images
            //foundArchSite.location = placemark.location
        }

        db.child("users").child(userId).child("archSites").child(archSite.fbId).setValue(archSite)
        updateImages(archSite)
    }

    override fun delete(archSite: ArchSiteModel) {

            archSite.images.forEach {
                if (it != "") {
                    val imageRef = FirebaseStorage.getInstance().getReferenceFromUrl(it)
                    imageRef.delete().addOnFailureListener {
                        println(it.message)
                    }
                }
            }
        db.child("users").child(userId).child("archSites").child(archSite.fbId).removeValue()
        archSites.remove(archSite)
    }

    override fun clear() {
        archSites.clear()
    }

    fun updateImages(archSite: ArchSiteModel) {

        for (x in 0 until archSite.images.size) {
            var curImageStr = archSite.images[x]
            val fileName = File(curImageStr)
            val imageName = fileName.getName()

            var imageRef = st.child(userId + '/' + imageName)
            val baos = ByteArrayOutputStream()
            val bitmap = readImageFromPath(context, curImageStr)

            bitmap?.let {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val data = baos.toByteArray()
                val uploadTask = imageRef.putBytes(data)
                uploadTask.addOnFailureListener {
                    println(it.message)
                }.addOnSuccessListener { taskSnapshot ->
                    taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
                        archSite.images[x] = it.toString()
                        db.child("users").child(userId).child("archSites").child(archSite.fbId)
                            .setValue(archSite)
                    }
                }
            }
        }
    }

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
        st = FirebaseStorage.getInstance().reference
        archSites.clear()
        db.child("users").child(userId).child("archSites").addListenerForSingleValueEvent(valueEventListener)
    }
}