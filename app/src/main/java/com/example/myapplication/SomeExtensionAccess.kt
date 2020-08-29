package com.example.myapplication


object SomeExtensionAccess : ExtensionAccess<SomeExtensionModule>(
    SomeExtensionModule,
    DummyExtensionModuleImpl()
)

private class DummyExtensionModuleImpl : SomeExtensionModule {
    override val isEnabled: Boolean
        get() = false

    override fun getTimeStamp(): Long = System.currentTimeMillis()
}