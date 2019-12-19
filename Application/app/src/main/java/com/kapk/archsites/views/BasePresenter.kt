package com.kapk.archsites.views

import com.kapk.archsites.main.MainApp

open class BasePresenter(var view: BaseView?) {

    var app: MainApp =  view?.application as MainApp

}