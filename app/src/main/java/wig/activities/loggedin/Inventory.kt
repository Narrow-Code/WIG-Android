package wig.activities.loggedin

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.widget.LinearLayout
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setScreenOrientation()
        setInventoryBindings()
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
                    populateTable(inventory)
                }
            }
        }
    }

    private fun populateTable(inventory: List<InventoryDTO>) {
        val tableLayout = inventoryBinding.searchTableLayout
        for (location in inventory) {
            val row = createRowForLocation(location)
            setColorForRow(row, tableLayout.childCount)
            row.setOnClickListener { locationClick(it as TableRow, location) }
            tableLayout.addView(row)
        }
        resetRowColors(tableLayout)
    }

    private fun setColorForRow(row: TableRow, position: Int){
        val backgroundColor = if(position % 2 == 0){
            Color.BLACK
        } else {
            Color.DKGRAY
        }
        row.setBackgroundColor(backgroundColor)
    }

    private fun resetRowColors(tableLayout: LinearLayout) {
        for (i in 0 until tableLayout.childCount) {
            val row = tableLayout.getChildAt(i) as? TableRow
            row?.let { setColorForRow(it, i) }
        }
    }

    private fun createRowForLocation(location: InventoryDTO): TableRow {
        val name = location.parent.locationName
        val expand = " >"

        val row = TableRow(this)
        val layoutParams = TableRow.LayoutParams(
            TableRow.LayoutParams.MATCH_PARENT,
            TableRow.LayoutParams.WRAP_CONTENT)

        val nameLayout = LinearLayout(this)
        nameLayout.layoutParams = TableRow.LayoutParams(
            0, TableRow.LayoutParams.MATCH_PARENT, 0.34f)
        nameLayout.gravity = Gravity.START or Gravity.CENTER_VERTICAL

        val nameView = TextView(this)
        nameView.text = name.substring(0 until 25.coerceAtMost(name.length))
        nameLayout.addView(nameView)

        val expandView = TextView(this)
        expandView.text = expand
        nameLayout.addView(expandView)

        row.setOnClickListener { locationClick(it as TableRow, location) }

        row.addView(nameLayout)
        row.layoutParams = layoutParams
        inventoryRowMap[location.parent.locationUID] = row

        return row
    }

    private fun locationClick(clickedRow: TableRow, inventory: InventoryDTO){
        val tableLayout = inventoryBinding.searchTableLayout
        val rowIndex = tableLayout.indexOfChild(clickedRow)

        val expand = (clickedRow.getChildAt(0) as LinearLayout).getChildAt(1) as TextView

        if (expand.text == " >") {
            inventory.locations?.let { locations ->
                if (locations.isNotEmpty()) {
                    for (i in locations) {
                        val newRow = createRowForLocation(i)
                        setColorForRow(newRow, rowIndex + 1)
                        tableLayout.addView(newRow, rowIndex + 1)
                    }
                }
            }
            inventory.ownerships?.let { ownerships ->
                if (ownerships.isNotEmpty()) {
                    for (i in ownerships) {
                        val newRow = createRowForOwnership(i)
                        setColorForRow(newRow, rowIndex + 1)
                        tableLayout.addView(newRow, rowIndex + 1)
                    }
                }
            }
            expand.text = " v"
            resetRowColors(tableLayout)
        }
        else if (expand.text == " v") {
            inventory.locations?.let { locations ->
                if (locations.isNotEmpty()) {
                    for (i in inventory.locations) {
                        val rowToRemove = inventoryRowMap[i.parent.locationUID]
                        rowToRemove?.let {
                            tableLayout.removeView(it)
                            inventoryRowMap.remove(i.parent.locationUID)
                        }
                    }
                }
            }
            inventory.ownerships?.let { ownerships ->
                if (ownerships.isNotEmpty()) {
                    for (i in inventory.ownerships) {
                        val rowToRemove = inventoryRowMap[i.ownershipUID]
                        rowToRemove?.let {
                            tableLayout.removeView(it)
                            inventoryRowMap.remove(i.ownershipUID)
                        }
                    }
                }
            }
            expand.text = " >"
            resetRowColors(tableLayout)        }
    }

    private fun createRowForOwnership(ownership: Ownership): TableRow {
        val name = "        " + ownership.customItemName
        val row = TableRow(this)
        val layoutParams = TableRow.LayoutParams(
            TableRow.LayoutParams.MATCH_PARENT,
            TableRow.LayoutParams.WRAP_CONTENT)

        val nameLayout = LinearLayout(this)
        nameLayout.layoutParams = TableRow.LayoutParams(
            0, TableRow.LayoutParams.MATCH_PARENT, 0.34f)
        nameLayout.gravity = Gravity.START or Gravity.CENTER_VERTICAL

        val nameView = TextView(this)
        nameView.text = name.substring(0 until 25.coerceAtMost(name.length))
        nameLayout.addView(nameView)

        row.addView(nameLayout)
        row.layoutParams = layoutParams
        inventoryRowMap[ownership.ownershipUID] = row

        return row
    }

}
