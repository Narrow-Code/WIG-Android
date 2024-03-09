package wig.activities

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import wig.activities.bases.BaseActivity
import wig.utils.SettingsManager
import wig.utils.StoreSettings
import wig.utils.StoreToken

class Settings : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        disableBackPress()
        setScreenOrientation()
        setSettingsBindings()
        setSettings()
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        settingsBinding.logoutButton.setOnClickListener { logout() }
        settingsBinding.topMenu.icScanner.setOnClickListener { startActivityScanner() }
        settingsBinding.vibrate.setOnClickListener { setVibrate() }
        settingsBinding.sound.setOnClickListener { setSound() }
        settingsBinding.typeSpinner.onItemClickListener
    }

    private fun setVibrate() {
        lifecycleScope.launch {
            val storeSettings = StoreSettings(this@Settings)
            storeSettings.saveIsVibrateEnabled(settingsBinding.vibrate.isChecked)
            SettingsManager.setIsVibrateEnabled(settingsBinding.vibrate.isChecked)
        }
    }

    private fun setSound() {
        lifecycleScope.launch {
            val storeSettings = StoreSettings(this@Settings)
            storeSettings.saveIsSoundEnabled(settingsBinding.sound.isChecked)
            SettingsManager.setIsSoundEnabled(settingsBinding.sound.isChecked)
        }
    }

    private fun setSettings() {
        settingsBinding.vibrate.isChecked = SettingsManager.getIsVibrateEnabled()
        settingsBinding.sound.isChecked = SettingsManager.getSoundEnabled()
        val i = if(SettingsManager.getIsStartupOnScanner()){0} else {1}
        settingsBinding.typeSpinner.setSelection(i)
    }

    private fun logout() {
        // Delete token & UID
        lifecycleScope.launch {
            val storeToken = StoreToken(this@Settings)
            storeToken.saveToken("")
        }
        startActivityLogin()
    }
}
