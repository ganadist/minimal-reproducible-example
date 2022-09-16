package com.example.myapplication;

import android.widget.TextView
import androidx.annotation.StringRes

object TextViewUtil2 {
    fun setText(view: TextView, @StringRes resId: Int) {
        if (resId > 0) {
            view.setText(resId)
        }
    }
}