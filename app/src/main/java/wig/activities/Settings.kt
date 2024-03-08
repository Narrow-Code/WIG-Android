package wig.activities

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import wig.activities.bases.BaseActivity
import wig.utils.StoreToken

class Settings : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        disableBackPress()
        setScreenOrientation()
        setSettingsBindings()
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        settingsBinding.logoutButton.setOnClickListener { logout() }
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