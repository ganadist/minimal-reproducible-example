package com.example.myapplication

import com.linecorp.lich.component.ComponentFactory

abstract class ExtensionAccess<T : Any>(
    private val compoentFactory: ComponentFactory<T>,
    private val dummyComponent: T? = null
) {
    private val testLibrary = false
    private var extensionModule: T? = null

    fun getExtensionModule(): T {
        if (testLibrary) {
            extensionModule = dummyComponent
        }
        if (extensionModule == null) {
            kotlin.runCatching {
                extensionModule = ApplicationGraph.getComponent(compoentFactory)
            }.onFailure {
                extensionModule = dummyComponent
            }
        }
        return dummyComponent!!
    }
}