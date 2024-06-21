package wig.activities.loggedin

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import wig.activities.base.Settings
import wig.managers.SettingsManager
import wig.utils.StoreSettings
import wig.utils.StoreToken

class Settings : Settings() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setScreenOrientation()
        setSettingsBindings()
        setSettings()
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        settingsBinding.logoutButton.setOnClickListener { logout() }
        settingsBinding.topMenu.icScanner.setOnClickListener { startActivityScanner() }
        settingsBinding.topMenu.icSettings.setOnClickListener { startActivitySettings() }
        settingsBinding.topMenu.icCheckedOut.setOnClickListener { startActivityCheckedOut() }
        settingsBinding.topMenu.icInventory.setOnClickListener { startActivityInventory() }
        settingsBinding.vibrate.setOnClickListener { setVibrate() }
        settingsBinding.sound.setOnClickListener { setSound() }
        settingsBinding.typeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                lifecycleScope.launch {
                    val storeSettings = StoreSettings(this@Settings)
                    when (parent?.getItemAtPosition(position).toString()) {
                        "Scanner" -> {
                            storeSettings.saveIsStartupOnScanner(true)
                            SettingsManager.setIsStartupOnScanner(true)
                        }

                        "Inventory" -> {
                            storeSettings.saveIsStartupOnScanner(false)
                            SettingsManager.setIsStartupOnScanner(false)
                        }
                    }
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
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
