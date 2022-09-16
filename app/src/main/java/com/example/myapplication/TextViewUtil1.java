package com.example.myapplication;

import android.widget.TextView;
import androidx.annotation.StringRes;

class TextViewUtil1 {
    public static void setText(TextView view, @StringRes int resId) {
        if (resId > 0) {
            view.setText(resId);
        }
    }
}
