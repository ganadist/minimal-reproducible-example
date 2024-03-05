package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.example.constant.BuildConfig

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        findViewById<Button>(R.id.login_button).setOnClickListener {
            val pref = getSharedPreferences("login", MODE_PRIVATE)
            pref.edit().putBoolean("login", true).apply()
            finish()
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (BuildConfig.ALLOW_DEEPLINK_LOGIN) {
            // handle login infomation
        } else {
            Log.e(TAG, "This binary cannot handle login deeplink")
        }
    }

    companion object {
        private const val TAG = "LoginActivity"
    }
}
