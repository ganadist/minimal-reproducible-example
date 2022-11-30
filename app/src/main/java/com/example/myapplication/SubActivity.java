package com.example.myapplication;

import android.app.Activity;
import android.os.Bundle;

public class SubActivity extends Activity {
    @Override
    public void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        final int resId = R.id.text;

        switch (resId) {
            case R.id.text: {
                break;
            }
            case R.id.bottom: {
                break;
            }
        }
    }
}
