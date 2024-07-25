package wig.activities.loggedin

import android.os.Bundle
import android.widget.ExpandableListView
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import wig.activities.base.Settings
import wig.managers.InventoryExpandableListAdapter
import wig.models.responses.InventoryDTO

class Inventory : Settings() {

    private lateinit var inventory: InventoryDTO
    private lateinit var adapter: InventoryExpandableListAdapter
    private lateinit var expandableListView: ExpandableListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setScreenOrientation()
        setInventoryBindings()
        expandableListView = inventoryBinding.searchTableLayout
        setOnClickListeners()
        getInventory()
    }

    private fun setOnClickListeners() {
        inventoryBinding.topMenu.icScanner.setOnClickListener { startActivityScanner() }
        inventoryBinding.topMenu.icSettings.setOnClickListener { startActivitySettings() }
        inventoryBinding.topMenu.icCheckedOut.setOnClickListener { startActivityCheckedOut() }
        inventoryBinding.topMenu.icInventory.setOnClickListener { startActivityInventory() }
    }

    private fun getInventory() {
        lifecycleScope.launch {
            val response = api.locationGetInventory()
            if (response.success) {
                inventory = response.inventory

                adapter = InventoryExpandableListAdapter(this@Inventory, this@Inventory, inventory, api)
                expandableListView.setAdapter(adapter)
            }
        }
    }
}
