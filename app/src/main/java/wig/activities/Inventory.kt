package wig.activities

import android.os.Bundle
import wig.activities.bases.BaseActivity

class Inventory : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        disableBackPress()
        setScreenOrientation()
        setInventoryBindings()
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        inventoryBinding.topMenu.icScanner.setOnClickListener { startActivityScanner() }
        inventoryBinding.topMenu.icSettings.setOnClickListener { startActivitySettings() }
        inventoryBinding.topMenu.icCheckedOut.setOnClickListener { startActivityCheckedOut() }
        inventoryBinding.topMenu.icInventory.setOnClickListener { startActivityInventory() }
    }

}
