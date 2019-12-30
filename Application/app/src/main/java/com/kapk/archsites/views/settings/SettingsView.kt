package com.kapk.archsites.views.settings

import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.kapk.archsites.R
import com.kapk.archsites.main.MainApp
import com.kapk.archsites.views.BaseView
import kotlinx.android.synthetic.main.activity_settings.*
import org.jetbrains.anko.toast

class SettingsView : BaseView() {

    lateinit var presenter: SettingsPresenter
    var auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        init(toolbar, true)

        presenter = initPresenter(SettingsPresenter(this))as SettingsPresenter

        btn_save_email.setOnClickListener {
            val email = txt_user_email.text.toString()
            if (email == "") {
                toast("Please provide email")
            }
            else {
                presenter.doEmailChange(email)
            }
        }
        btn_save_password.setOnClickListener {
            val password = txt_password.text.toString()
            if (password == "") {
                toast("Please provide password")
            }
            else {
                presenter.doPasswordChange(password)
            }
        }

        txt_user_email.setText(auth.currentUser!!.email)
        txt_sitesCounter.setText(presenter.doCountSites().toString())
        txt_sitesVisitedCounter.setText(presenter.doCountVisitedSites().toString())
    }

}