package com.kapk.archsites.views.settings

import com.google.firebase.auth.FirebaseAuth
import com.kapk.archsites.models.firebase.ArchSiteFireStore
import com.kapk.archsites.views.BasePresenter
import com.kapk.archsites.views.BaseView
import org.jetbrains.anko.toast

class SettingsPresenter(view: BaseView) : BasePresenter(view) {

    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    var fireStore: ArchSiteFireStore? = null

    init {
        if (app.archSites is ArchSiteFireStore) {
            fireStore = app.archSites as ArchSiteFireStore
        }
    }

    fun doEmailChange(email: String){
        auth.currentUser!!.updateEmail(email).addOnCompleteListener(view!!) { task ->
            if (task.isSuccessful)
                view?.toast("E-mail address updated.")
            else
                view?.toast("Update email failed: ${task.exception?.message}")
        }
    }

    fun doPasswordChange(password: String){
        auth.currentUser!!.updatePassword(password).addOnCompleteListener(view!!) { task ->
            if (task.isSuccessful)
                view?.toast("Password updated.")
            else
                view?.toast("Update password failed: ${task.exception?.message}")
        }
    }

    fun doCountSites(): Int{
        return app.archSites.findAll().size
    }

    fun doCountVisitedSites():Int{
        val allSites = app.archSites.findAll()
        var counter = 0
        for(x in 0 until allSites.size){
            if(allSites[x].visited)
                counter++
        }
        return counter
    }

}