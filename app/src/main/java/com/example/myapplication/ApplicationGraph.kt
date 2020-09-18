package com.example.myapplication

import android.app.Application
import com.linecorp.lich.component.ComponentFactory
import com.linecorp.lich.component.getComponent


object ApplicationGraph {
    private lateinit var application: Application

    @JvmStatic
    fun <T : Any> getComponent(factory: ComponentFactory<T>): T = application.getComponent(factory)

    @JvmStatic
    fun init(app: Application) {
        application = app
    }
}