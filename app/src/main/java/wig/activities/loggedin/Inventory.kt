package wig.activities.loggedin

import android.os.Bundle
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import wig.activities.base.Settings
import wig.models.responses.InventoryDTO
import wig.models.entities.Ownership

class Inventory : Settings() {

    private lateinit var inventory: List<InventoryDTO>
    private val inventoryRowMap = mutableMapOf<String, TableRow>()
    private lateinit var tableLayout: TableLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setScreenOrientation()
        setInventoryBindings()
        tableLayout = inventoryBinding.searchTableLayout
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
                if(response.inventory.locations?.isNotEmpty() == true) {
                    inventory = response.inventory.locations
                    populateTableLocations(inventory)
                    response.inventory.ownerships?.let { populateTableOwnerships(it) }
                }
            }
        }
    }

    private fun populateTableLocations(inventory: List<InventoryDTO>) {
        for (location in inventory) {
            val row = createRowForLocation(location)
            tableManager.setColorForRow(row, tableLayout.childCount)
            row.setOnClickListener { locationClick(it as TableRow, location) }
            tableLayout.addView(row)
        }
        tableManager.resetRowColors(tableLayout)
    }

    private fun populateTableOwnerships(ownerships: MutableList<Ownership>){
            for (ownership in ownerships) {
                if (ownership.itemCheckedOut == "false") {
                    val newRow = createRowForOwnership(ownership)
                    tableLayout.addView(newRow)
                }
            }
        tableManager.resetRowColors(tableLayout)
    }

    private fun locationClick(clickedRow: TableRow, inventory: InventoryDTO) {
        val rowIndex = tableLayout.indexOfChild(clickedRow)
        val expand = (clickedRow.getChildAt(0) as LinearLayout).getChildAt(1) as TextView

        if (expand.text == " >") {
            expandLocation(inventory, rowIndex)
            expand.text = " v"
            tableManager.resetRowColors(tableLayout)
        } else if (expand.text == " v") {
            collapseLocation(inventory)
            expand.text = " >"
            tableManager.resetRowColors(tableLayout)
        }
    }

    private fun expandLocation(inventory: InventoryDTO, rowIndex: Int) {
        inventory.locations?.let { locations ->
            if (locations.isNotEmpty()) {
                for (location in locations) {
                    val newRow = createRowForLocation(location)
                    tableManager.setColorForRow(newRow, rowIndex + 1)
                    tableLayout.addView(newRow, rowIndex + 1)
                }
            }
        }
        inventory.ownerships?.let { ownerships ->
            if (ownerships.isNotEmpty()) {
                for (ownership in ownerships) {
                    if (ownership.itemCheckedOut == "false") {
                        val newRow = createRowForOwnership(ownership)
                        tableManager.setColorForRow(newRow, rowIndex + 1)
                        tableLayout.addView(newRow, rowIndex + 1)
                    }
                }
            }
        }
    }

    private fun collapseLocation(inventory: InventoryDTO) {
        inventory.locations?.forEach { location ->
            val rowToRemove = inventoryRowMap[location.parent.locationUID]
            rowToRemove?.let {
                tableLayout.removeView(it)
                inventoryRowMap.remove(location.parent.locationUID)
            }
        }
        inventory.ownerships?.forEach { ownership ->
            val rowToRemove = inventoryRowMap[ownership.ownershipUID]
            rowToRemove?.let {
                tableLayout.removeView(it)
                inventoryRowMap.remove(ownership.ownershipUID)
            }
        }
    }


    private fun createRowForOwnership(ownership: Ownership): TableRow {
        val name = "        " + ownership.customItemName.substring(0 until 25.coerceAtMost(ownership.customItemName.length))
        val row = TableRow(this)
        val layoutParams = TableRow.LayoutParams(
            TableRow.LayoutParams.MATCH_PARENT,
            TableRow.LayoutParams.WRAP_CONTENT)
        row.layoutParams = layoutParams

        val nameLayout = LinearLayout(this)
        nameLayout.layoutParams = TableRow.LayoutParams(
            0, TableRow.LayoutParams.MATCH_PARENT, 0.34f)
        nameLayout.gravity = Gravity.START or Gravity.CENTER_VERTICAL

        val nameView = TextView(this)
        nameView.text = name
        nameLayout.addView(nameView)

        row.addView(nameLayout)
        inventoryRowMap[ownership.ownershipUID] = row

        return row
    }

    private fun createRowForLocation(location: InventoryDTO): TableRow {
        val name = location.parent.locationName.substring(0, 25.coerceAtMost(location.parent.locationName.length))
        val expand = " >"

        val row = TableRow(this)
        val layoutParams = TableRow.LayoutParams(
            TableRow.LayoutParams.MATCH_PARENT,
            TableRow.LayoutParams.WRAP_CONTENT)
        row.layoutParams = layoutParams

        val nameLayout = LinearLayout(this)
        nameLayout.layoutParams = TableRow.LayoutParams(
            0, TableRow.LayoutParams.MATCH_PARENT, 0.34f)
        nameLayout.gravity = Gravity.START or Gravity.CENTER_VERTICAL

        val nameView = TextView(this)
        nameView.text = name
        nameLayout.addView(nameView)

        val expandView = TextView(this)
        expandView.text = expand
        nameLayout.addView(expandView)

        row.setOnClickListener { locationClick(it as TableRow, location) }

        row.addView(nameLayout)
        inventoryRowMap[location.parent.locationUID] = row

        return row
    }

}
