package com.kapk.archsites.views.login

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.kapk.archsites.models.ArchSiteModel
import com.kapk.archsites.models.firebase.ArchSiteFireStore
import com.kapk.archsites.views.BasePresenter
import com.kapk.archsites.views.BaseView
import com.kapk.archsites.views.VIEW
import org.jetbrains.anko.toast

class LoginPresenter(view: BaseView) : BasePresenter(view) {

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private var fireStore: ArchSiteFireStore? = null

    init {
        if (app.archSites is ArchSiteFireStore) {
            fireStore = app.archSites as ArchSiteFireStore
        }
    }

    fun doLogin(email: String, password: String) {
        view?.showProgress()
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(view!!) { task ->
            if (task.isSuccessful) {
                if (fireStore != null) {
                    fireStore!!.fetchArchSites {
                        view?.hideProgress()
                        view?.navigateTo(VIEW.LIST)
                    }
                } else {
                    view?.hideProgress()
                    view?.navigateTo(VIEW.LIST)
                }
            } else {
                view?.hideProgress()
                view?.toast("Sign Up Failed: ${task.exception?.message}")
            }
        }
    }

    fun doSignUp(email: String, password: String) {
        view?.showProgress()
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(view!!) { task ->
            if (task.isSuccessful) {
                loadSiteSpecs()
                if (fireStore != null) {
                    fireStore!!.fetchArchSites {
                        view?.hideProgress()
                        view?.navigateTo(VIEW.LIST)
                    }
                }
            } else {
                view?.hideProgress()
                view?.toast("Sign Up Failed: ${task.exception?.message}")
            }
        }

    }

    private fun loadSiteSpecs(){

        db.collection("ArchSitesSpec")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        val archSiteSpec = ArchSiteModel()
                        archSiteSpec.name = document.getString("name").toString()
                        archSiteSpec.description = document.getString("description").toString()
                        archSiteSpec.images[0] = document.getString("image1").toString()
                        archSiteSpec.images[1] = document.getString("image2").toString()
                        archSiteSpec.images[2] = document.getString("image3").toString()
                        archSiteSpec.images[3] = document.getString("image4").toString()
                        archSiteSpec.location.lat = document.getDouble("locationLat")!!
                        archSiteSpec.location.lng = document.getDouble("locationLng")!!
                        archSiteSpec.editable = false

                        val userId = FirebaseAuth.getInstance().currentUser!!.uid
                        val udb = FirebaseDatabase.getInstance().reference

                        val key = udb.child("users").child(userId).child("archSites").push().key
                        key?.let {
                            archSiteSpec.fbId = key
                            udb.child("users").child(userId).child("archSites").child(key).setValue(archSiteSpec)
                        }
                    }
                } else {
                    Log.w(TAG, "Error getting documents.", task.exception)
                }
            }
    }

}