package com.example.myapplication

import android.content.Context
import com.linecorp.lich.component.ComponentFactory

interface SomeExtensionModule {
    val isEnabled: Boolean

    fun getTimeStamp(): Long

    companion object : ComponentFactory<SomeExtensionModule>() {
        override fun createComponent(context: Context): SomeExtensionModule =
            SomeExtensionModule.delegateToServiceLoader(context)
    }
}