package wig.activities.loggedin

import android.os.Bundle
import android.widget.TableLayout
import wig.activities.base.Settings
import wig.models.responses.InventoryDTO

class Inventory : Settings() {

    // private lateinit var inventory: List<InventoryDTO>
    private lateinit var tableLayout: TableLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setScreenOrientation()
        setInventoryBindings()
        tableLayout = inventoryBinding.searchTableLayout
        setOnClickListeners()
        // getInventory()
    }

    private fun setOnClickListeners() {
        inventoryBinding.topMenu.icScanner.setOnClickListener { startActivityScanner() }
        inventoryBinding.topMenu.icSettings.setOnClickListener { startActivitySettings() }
        inventoryBinding.topMenu.icCheckedOut.setOnClickListener { startActivityCheckedOut() }
        inventoryBinding.topMenu.icInventory.setOnClickListener { startActivityInventory() }
    }

}
