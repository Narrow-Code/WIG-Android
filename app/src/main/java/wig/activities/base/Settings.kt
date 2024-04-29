package wig.activities.base

import android.annotation.SuppressLint
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

// Settings sets up the Base settings and functions for the application
open class Settings : Bindings() {
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

    // performVibration will set off the vibration notification
    fun performVibration(context: Context) {
        val vibrator = context.getSystemService(Vibrator::class.java)
        vibrator?.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
            ?: run {
                // handle the case where vibration is not supported
            }
    }

    // playScanSound will set off the sound notification
    fun playScanSound(context: Context) {
        val ringtone = RingtoneManager.getRingtone(
            context,
            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        )
        ringtone.play()
    }

    // checkForUpdates will check github for most recent release and set appName color to reflect availability
    fun checkForUpdates() {
        coroutineScope.launch {
            val updater = ApkUpdater(this@Settings, "https://github.com/WIGteam/WIG-Android/releases/latest")
            updater.threeNumbers = true
            if (updater.isInternetConnection() && updater.isNewUpdateAvailable() == true) {
                scannerBinding.topMenu.appName.setTextColor(Color.YELLOW)
                scannerBinding.topMenu.appName.setOnClickListener { updateButton(updater) }
            }
        }
    }

    // updateButton will download the latest release from github releases
    private fun updateButton(updater: ApkUpdater) {
        updater.requestDownload()
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

    // unpackSettings will retrieve all of the saved settings from the DataStore
    suspend fun unpackSettings() = withContext(Dispatchers.IO){
        // vibration settings
        val vibrateFlow: Flow<Boolean?> = StoreSettings(this@Settings).getIsVibrateEnabled
        vibrateFlow.map { isVibrateEnabled ->
            if (isVibrateEnabled != null) {
                SettingsManager.setIsVibrateEnabled(isVibrateEnabled)
            }
        }.first()

        // sound settings
        val soundFlow: Flow<Boolean?> = StoreSettings(this@Settings).getIsSoundEnabled
        soundFlow.map { isSoundEnabled ->
            if (isSoundEnabled != null) {
                SettingsManager.setIsSoundEnabled(isSoundEnabled)
            }
        }.first()

        // startup settings
        val startupFlow: Flow<Boolean?> = StoreSettings(this@Settings).getIsStartupOnScanner
        startupFlow.map { isStartupOnScanner ->
            if (isStartupOnScanner != null) {
                SettingsManager.setIsStartupOnScanner(isStartupOnScanner)
            }
        }.first()

        // self hosted settings
        val hostedFlow: Flow<Boolean?> = StoreSettings(this@Settings).getIsHosted
        hostedFlow.map { isHosted ->
            if (isHosted != null) {
                SettingsManager.setIsHosted(isHosted)
            }
        }.first()

        // hostname for self hosted server
        val hostnameFlow: Flow<String?> = StoreSettings(this@Settings).getHostname
        hostnameFlow.map { hostname ->
            if (hostname != null) {
                SettingsManager.setHostname(hostname)
            }
        }.first()

        // portNumber for self hosted server
        val portNumberFlow: Flow<String?> = StoreSettings(this@Settings).getPort
        portNumberFlow.map { portNumber ->
            if (portNumber != null) {
                SettingsManager.setPortNumber(portNumber)
            }
        }.first()
    }
}