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

            val intent = Intent(this, MainActivity::class.java)
            intent.apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or
                        Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "authority:" + intent.data?.authority)

        if (intent.data?.authority == "login") {
            if (BuildConfig.ALLOW_DEEPLINK_LOGIN) {
                // handle login infomation
                Log.d(TAG, "Perform login deeplink")
                findViewById<Button>(R.id.login_button).performClick()
            } else {
                Log.e(TAG, "This binary cannot handle login deeplink")
            }
        }
    }

    companion object {
        private const val TAG = "LoginActivity"
    }
}
