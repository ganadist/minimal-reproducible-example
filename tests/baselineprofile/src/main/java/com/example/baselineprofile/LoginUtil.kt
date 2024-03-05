package com.example.baselineprofile

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.ParcelFileDescriptor
import android.util.Log
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.BySelector
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject2
import androidx.test.uiautomator.Until

class LoginUtil(
    private val applicationId: String
) {
    private val instrumentation = InstrumentationRegistry.getInstrumentation()
    private val uiAutomation = instrumentation.uiAutomation
    private val device = UiDevice.getInstance(instrumentation)
    private val context = ApplicationProvider.getApplicationContext<Context>()

    private fun runCommand(command: String) {
        uiAutomation.executeShellCommand(command).use { pfd ->
            ParcelFileDescriptor.AutoCloseInputStream(pfd).use { inputStream ->
                val output = inputStream.bufferedReader().readText()
                Log.d(TAG, "runCommand: $command -> $output")
            }
        }
    }

    /**
     * Grant permissions to app for standalone instrumented tests project
     * which is using `com.android.test` plugin.
     *
     * params:
     * - permissions: permissions to grant
     *
     * This method will grant permission by adb shell.
     * For normal cases, use [androidx.test.rule.GrantPermissionRule] instead of this method.
     */
    fun grantPermissions(permissions: Array<String> = PERMISSIONS) {
        permissions.forEach { permission ->
            // some of permissions cannot be granted by
            // uiAutomation.grantRuntimePermission()
            runCommand("pm grant $applicationId $permission")
        }

        // exclude battery optimization
        runCommand("dumpsys deviceidle whitelist +$applicationId")
    }

    /**
     * Disable install verifier
     *
     * Install verifier can block instrumented tests,
     * and we need to disable it before perform tests
     */
    fun disableInstallVerifier() {
        DISABLE_VERIFIER_COMMANDS.forEach { command ->
            runCommand("settings put $command")
        }
    }

    private fun launchMain() {
        val intent = context.packageManager.getLaunchIntentForPackage(applicationId)
            ?: throw IllegalStateException("Cannot find launch intent for $applicationId")
        intent.apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        context.startActivity(intent)
        device.wait(
            Until.hasObject(By.pkg(applicationId).depth(0)),
            3 * 1000
        )
    }

   fun waitMainActivity(timeout: Long) {
       // FIXME
       // It looks this API cannot find `app_hello` resId from MainActivity
        device.wait(
            Until.hasObject(
                By.res(applicationId, "app_hello")
            ),
            timeout
        )
    }

    fun stopProcess() {
        runCommand("am force-stop $applicationId")
    }

    fun clearUserData() {
        runCommand("pm clear $applicationId")
    }

    fun proceed(
        username: String,
        password: String,
        timeout: Long
    ) {
        launchMain()
        Thread.sleep(2000)

        val parameters = mapOf(
            "username" to username,
            "password" to password
        )

        val url = Uri.Builder()
            .scheme("myapplication")
            .authority("login")
            .apply {
                parameters.forEach { (key, value) ->
                    appendQueryParameter(key, value)
                }
            }.build()

        val loginIndent = Intent(Intent.ACTION_VIEW, url).apply {
            setPackage(applicationId)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        context.startActivity(loginIndent)

        waitMainActivity(timeout)
    }

    companion object {
        /**
         * Initial permissions for App Startup
         */
        private val PERMISSIONS: Array<String> = mutableListOf(
            // Location permissions
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION,

            // excluding battery optimization
            android.Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS,
        ).apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                addAll(
                    listOf(
                        // Nearby permissions
                        android.Manifest.permission.BLUETOOTH_SCAN,
                        android.Manifest.permission.BLUETOOTH_ADVERTISE,
                        android.Manifest.permission.BLUETOOTH_CONNECT,
                    )
                )
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                addAll(
                    listOf(
                        android.Manifest.permission.NEARBY_WIFI_DEVICES,
                        android.Manifest.permission.POST_NOTIFICATIONS,
                    )
                )
            }
        }.toTypedArray()

        private val DISABLE_VERIFIER_COMMANDS = arrayOf(
            "secure install_non_market_apps 1",
            "secure package_verifier_user_consent 1",
            "global package_verifier_enable 0",
            "global verifier_verify_adb_installs 0",
        )

        private const val TAG = "LoginUtil"

        const val APPLICATION_ID = "com.example.myapplication"
    }
}