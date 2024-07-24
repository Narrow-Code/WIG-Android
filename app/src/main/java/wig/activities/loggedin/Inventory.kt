package wig.activities.loggedin

import android.os.Bundle
import android.widget.ExpandableListView
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import wig.activities.base.Settings
import wig.managers.InventoryExpandableListAdapter
import wig.models.responses.InventoryDTO
import wig.utils.Alerts

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

                adapter = InventoryExpandableListAdapter(this@Inventory, inventory)
                expandableListView.setAdapter(adapter)

                setOnItemLongClickListener()
            }
        }
    }

    private fun setOnItemLongClickListener() {
        expandableListView.setOnItemLongClickListener { _, _, position, _ ->
            val packedPosition = expandableListView.getExpandableListPosition(position)
            val itemType = ExpandableListView.getPackedPositionType(packedPosition)
            val groupPosition = ExpandableListView.getPackedPositionGroup(packedPosition)
            if (itemType == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
                val ownership = adapter.getGroup(groupPosition)
                // TODO handle long click on Location
            } else {
                val childPosition = ExpandableListView.getPackedPositionChild(packedPosition)
                val ownership = adapter.getChild(groupPosition, childPosition)
                Alerts().deleteConfirmation(ownership.customItemName, this) { shouldDelete ->
                    if (shouldDelete) {
                        lifecycleScope.launch {
                            val result = api.deleteOwnership(ownership.ownershipUID)
                            if(result.success){
                                adapter.removeChild(groupPosition, childPosition)
                            }
                        }
                    }
                }

            }
            true
        }
    }

}
