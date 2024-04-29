package wig.activities.base

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.media.RingtoneManager
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.activity.OnBackPressedCallback
import com.supersuman.apkupdater.ApkUpdater
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import wig.activities.loggedin.CheckedOut
import wig.activities.loggedout.EmailVerification
import wig.activities.loggedout.ForgotPassword
import wig.activities.loggedin.Inventory
import wig.activities.loggedout.Login
import wig.activities.loggedout.ResetPassword
import wig.activities.loggedin.Scanner
import wig.activities.loggedout.ServerSetup
import wig.activities.loggedin.Settings
import wig.activities.loggedout.Signup
import wig.managers.SettingsManager
import wig.utils.StoreSettings

// Activity sets up the Base settings and functions for the application
open class Activity : Bindings() {
    val coroutineScope = CoroutineScope(Dispatchers.Main)

    // disableBackPress disables the back press button
    protected fun disableBackPress() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {} })
    }

    // setScreenOrientation sets the screen orientation to portrait view
    @SuppressLint("SourceLockedOrientationActivity")
    protected fun setScreenOrientation() {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    // startActivityLogin starts Login activity
    protected fun startActivityLogin() {
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
        finish()
    }

    // startActivitySignup starts Signup activity
    protected fun startActivitySignup() {
        val intent = Intent(this, Signup::class.java)
        startActivity(intent)
        finish()
    }

    // startActivityServerSetup starts ServerSetup activity
    protected fun startActivityServerSetup() {
        val intent = Intent(this, ServerSetup::class.java)
        startActivity(intent)
        finish()
    }

    // startActivityForgotPassword starts ForgotPassword activity
    protected fun startActivityForgotPassword() {
        val intent = Intent(this, ForgotPassword::class.java)
        startActivity(intent)
        finish()
    }

    // startActivitySettingsLogin starts Inventory or Scanner activity based on settings selection
    protected fun startActivitySettingsLogin() {
        if(SettingsManager.getIsStartupOnScanner()) {
            startActivityScanner()
        } else {
            startActivityInventory()
        }
    }

    // startActivityScanner starts Scanner activity
    protected fun startActivityScanner() {
        val intent = Intent(this, Scanner::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        startActivity(intent)
    }

    // startActivitySettings starts Settings activity
    protected fun startActivitySettings() {
        val intent = Intent(this, Settings::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        startActivity(intent)
    }

    // startActivityInventory starts Inventory activity
    protected fun startActivityInventory() {
        val intent = Intent(this, Inventory::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        startActivity(intent)
    }

    // startActivityCheckedOut starts CheckedOut activity
    protected fun startActivityCheckedOut() {
        val intent = Intent(this, CheckedOut::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        startActivity(intent)
    }

    // startActivityResetPassword starts ResetPassword activity
    protected fun startActivityResetPassword() {
        val intent = Intent(this, ResetPassword::class.java)
        startActivity(intent)
        finish()
    }

    // startActivityEmailVerification starts EmailVerification activity
    protected fun startActivityEmailVerification() {
        val intent = Intent(this, EmailVerification::class.java)
        startActivity(intent)
        finish()
    }

    protected fun removeConfirmation(name: String, callback: (Boolean) -> Unit) {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Confirm Remove")
        alertDialogBuilder.setMessage("Are you sure you want to remove $name from queue?")

        alertDialogBuilder.setPositiveButton("REMOVE") { dialog, _ ->
            dialog.dismiss()
            callback(true)
        }

        alertDialogBuilder.setNegativeButton("CANCEL") { dialog, _ ->
            dialog.dismiss()
            callback(false)
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    protected fun addConfirmation(name: String, callback: (Boolean) -> Unit) {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Confirm Add")
        alertDialogBuilder.setMessage("Are you sure you want to add $name to queue?")

        alertDialogBuilder.setPositiveButton("Add") { dialog, _ ->
            dialog.dismiss()
            callback(true)
        }

        alertDialogBuilder.setNegativeButton("CANCEL") { dialog, _ ->
            dialog.dismiss()
            callback(false)
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    fun performVibration(context: Context) {
        val vibrator = context.getSystemService(Vibrator::class.java)
        vibrator?.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
            ?: run {
                // handle the case where vibration is not supported
            }
    }

    fun playScanSound(context: Context) {
        val ringtone = RingtoneManager.getRingtone(
            context,
            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        )
        ringtone.play()
    }

    fun checkForUpdates() {
        coroutineScope.launch {
            val updater = ApkUpdater(this@Activity, "https://github.com/WIGteam/WIG-Android/releases/latest")
            updater.threeNumbers = true
            if (updater.isInternetConnection() && updater.isNewUpdateAvailable() == true) {
                scannerBinding.topMenu.appName.setTextColor(Color.YELLOW)
                scannerBinding.topMenu.appName.setOnClickListener { updateButton(updater) }
            }
        }
    }

    private fun updateButton(updater: ApkUpdater) {
        updater.requestDownload()
    }

    suspend fun unpackSettings() = withContext(Dispatchers.IO){
        val vibrateFlow: Flow<Boolean?> = StoreSettings(this@Activity).getIsVibrateEnabled
        vibrateFlow.map { isVibrateEnabled ->
            if (isVibrateEnabled != null) {
                SettingsManager.setIsVibrateEnabled(isVibrateEnabled)
            }
        }.first()

        val soundFlow: Flow<Boolean?> = StoreSettings(this@Activity).getIsSoundEnabled
        soundFlow.map { isSoundEnabled ->
            if (isSoundEnabled != null) {
                SettingsManager.setIsSoundEnabled(isSoundEnabled)
            }
        }.first()

        val startupFlow: Flow<Boolean?> = StoreSettings(this@Activity).getIsStartupOnScanner
        startupFlow.map { isStartupOnScanner ->
            if (isStartupOnScanner != null) {
                SettingsManager.setIsStartupOnScanner(isStartupOnScanner)
            }
        }.first()

        val hostedFlow: Flow<Boolean?> = StoreSettings(this@Activity).getIsHosted
        hostedFlow.map { isHosted ->
            if (isHosted != null) {
                SettingsManager.setIsHosted(isHosted)
            }
        }.first()

        val hostnameFlow: Flow<String?> = StoreSettings(this@Activity).getHostname
        hostnameFlow.map { hostname ->
            if (hostname != null) {
                SettingsManager.setHostname(hostname)
            }
        }.first()

        val portNumberFlow: Flow<String?> = StoreSettings(this@Activity).getPort
        portNumberFlow.map { portNumber ->
            if (portNumber != null) {
                SettingsManager.setPortNumber(portNumber)
            }
        }.first()
    }
}