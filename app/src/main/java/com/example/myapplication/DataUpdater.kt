package com.example.myapplication

class DataManager {
    fun edit(block: (DataUpdater) -> Unit) {
        block (DataUpdater())
    }
}

class DataUpdater {

}