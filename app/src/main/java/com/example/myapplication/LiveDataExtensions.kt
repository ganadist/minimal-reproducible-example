package com.example.myapplication

import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.observe

fun <T> LifecycleOwner.observeNotNull(
    liveData: LiveData<T>,
    lifecycleOwner: LifecycleOwner = if (this is Fragment) this.viewLifecycleOwner else this,
    func: (T) -> Unit
): Observer<T> = liveData.observe(lifecycleOwner) { it?.let(func) }

