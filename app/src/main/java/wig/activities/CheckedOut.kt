package wig.activities

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import wig.activities.bases.BaseActivity
import wig.utils.SettingsManager
import wig.utils.StoreSettings
import wig.utils.StoreToken

class CheckedOut : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        disableBackPress()
        setScreenOrientation()
        setCheckedOutBindings()
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        checkedOutBinding.topMenu.icScanner.setOnClickListener { startActivityScanner() }
        checkedOutBinding.topMenu.icSettings.setOnClickListener { startActivitySettings() }
    }

}
