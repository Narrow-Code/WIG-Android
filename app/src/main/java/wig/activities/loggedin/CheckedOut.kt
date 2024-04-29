package wig.activities.loggedin

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import wig.activities.base.Settings
import wig.models.responses.Borrowers
import wig.models.requests.CheckoutRequest
import wig.models.entities.Borrower
import wig.models.entities.Ownership

class CheckedOut : Settings() {

    private val borrowerRowMap = mutableMapOf<String, TableRow>()
    private lateinit var borrowers: List<Borrowers>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setScreenOrientation()
        setCheckedOutBindings()
        setOnClickListeners()
        getBorrowedItems()
    }

    private fun setOnClickListeners() {
        checkedOutBinding.topMenu.icScanner.setOnClickListener { startActivityScanner() }
        checkedOutBinding.topMenu.icSettings.setOnClickListener { startActivitySettings() }
        checkedOutBinding.topMenu.icCheckedOut.setOnClickListener { startActivityCheckedOut() }
        checkedOutBinding.topMenu.icInventory.setOnClickListener { startActivityInventory() }
        checkedOutBinding.returnAllButton.setOnClickListener { returnAllButton() }
    }

    private fun returnAllButton() {
        returnAllConfirmation { shouldDelete ->
            if (shouldDelete) {
                for (borrower in borrowers) {
                    val ownerships = mutableListOf<String>()
                    for (ownership in borrower.ownerships) {
                        ownerships.add(ownership.ownershipUID)
                    }
                    val checkOutRequest = CheckoutRequest(ownerships)
                    lifecycleScope.launch {
                        checkIn(checkOutRequest)
                    }
                }
                borrowers = emptyList()
                borrowerRowMap.clear()
                val tableLayout = checkedOutBinding.searchTableLayout
                tableLayout.removeAllViews()
            }
        }
    }

    private fun returnAllFromBorrower(borrower: Borrowers) {
                val ownerships = mutableListOf<String>()
                for (ownership in borrower.ownerships) {
                    ownerships.add(ownership.ownershipUID)
                }
                val checkOutRequest = CheckoutRequest(ownerships)
                lifecycleScope.launch {
                    val response = checkIn(checkOutRequest)
                    if (response.success){
                        for (ownership in ownerships){
                        val ownershipToRemove = borrower.ownerships.find { it.ownershipUID == ownership }
                        ownershipToRemove?.let { itRemove ->
                            borrower.ownerships.remove(itRemove)
                            val tableLayout = checkedOutBinding.searchTableLayout
                            val rowToRemove = borrowerRowMap[ownership]
                            rowToRemove?.let {
                                tableLayout.removeView(it)
                                borrowerRowMap.remove(ownership)
                            }
                        }

                    }
            }
        }
    }

    private fun returnOneItem(ownership: Ownership, borrower: Borrowers) {
        returnSingleConfirmation(ownership) { shouldDelete ->
            if (shouldDelete) {
                val ownerships = mutableListOf<String>()
                ownerships.add(ownership.ownershipUID)
                val checkOutRequest = CheckoutRequest(ownerships)
                lifecycleScope.launch {
                    val response = checkIn(checkOutRequest)
                    if (response.success){
                        val ownershipToRemove = borrower.ownerships.find { it.ownershipUID == ownership.ownershipUID }
                        ownershipToRemove?.let { itRemove ->
                            borrower.ownerships.remove(itRemove)
                            val tableLayout = checkedOutBinding.searchTableLayout
                            val rowToRemove = borrowerRowMap[ownership.ownershipUID]
                            rowToRemove?.let {
                                tableLayout.removeView(it)
                                borrowerRowMap.remove(ownership.ownershipUID)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun getBorrowedItems() {
        lifecycleScope.launch {
            val response = getCheckedOutOwnerships()
            if (response.success) {
                borrowers = response.borrowers
                populateTable(borrowers)
            }
        }
    }

    private fun populateTable(borrowers: List<Borrowers>) {
        val tableLayout = checkedOutBinding.searchTableLayout
        for (borrower in borrowers) {
            val row = createRowForBorrower(borrower.borrower)
            setColorForRow(row, tableLayout.childCount)
            row.setOnClickListener { borrowerClick(it as TableRow, borrower) }
            row.setOnLongClickListener {
                returnBorrowerConfirmation(borrower.borrower) { shouldDelete ->
                    if (shouldDelete){ returnAllFromBorrower(borrower)}
                }
                true
            }
            tableLayout.addView(row)
        }
        resetRowColors(tableLayout)
    }

    private fun createRowForBorrower(borrower: Borrower): TableRow {
        val name = borrower.borrowerName
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

        row.addView(nameLayout)
        row.layoutParams = layoutParams
        borrowerRowMap[borrower.borrowerUID] = row

        return row
    }

    private fun setColorForRow(row: TableRow, position: Int){
        val backgroundColor = if(position % 2 == 0){
            Color.BLACK
        } else {
            Color.DKGRAY
        }
        row.setBackgroundColor(backgroundColor)
    }

    private fun borrowerClick(clickedRow: TableRow, borrowers: Borrowers){
        val tableLayout = checkedOutBinding.searchTableLayout
        val rowIndex = tableLayout.indexOfChild(clickedRow)

        val expand = (clickedRow.getChildAt(0) as LinearLayout).getChildAt(1) as TextView

        if (expand.text == " >") {
            for (i in borrowers.ownerships) {
                val newRow = createRowForOwnership(i, borrowers)
                setColorForRow(newRow, rowIndex + 1)
                tableLayout.addView(newRow, rowIndex + 1)
            }
            expand.text = " v"
            resetRowColors(tableLayout)
        }
        else if (expand.text == " v") {
            for (i in borrowers.ownerships) {
                val rowToRemove = borrowerRowMap[i.ownershipUID]
                rowToRemove?.let {
                    tableLayout.removeView(it)
                    borrowerRowMap.remove(i.ownershipUID)
                }
            }
            expand.text = " >"
            resetRowColors(tableLayout)        }
    }

    private fun createRowForOwnership(ownership: Ownership, borrower: Borrowers): TableRow {
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
        borrowerRowMap[ownership.ownershipUID] = row

        row.setOnClickListener{returnOneItem(ownership, borrower)}

        return row
    }

    private fun resetRowColors(tableLayout: LinearLayout) {
        for (i in 0 until tableLayout.childCount) {
            val row = tableLayout.getChildAt(i) as? TableRow
            row?.let { setColorForRow(it, i) }
        }
    }

    private fun returnAllConfirmation(callback: (Boolean) -> Unit) {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Confirm Return All")
        alertDialogBuilder.setMessage("Are you sure you want to return all Checked Out items to their original locations??")

        alertDialogBuilder.setPositiveButton("RETURN") { dialog, _ ->
            dialog.dismiss()
            callback(true)
        }

        alertDialogBuilder.setNegativeButton("CANCEL") { dialog, _ ->
            dialog.dismiss()
            callback(false)
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun returnSingleConfirmation(ownership: Ownership, callback: (Boolean) -> Unit) {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Confirm Return All")
        alertDialogBuilder.setMessage("Are you sure you want to return ${ownership.customItemName} to it's original locations?")

        alertDialogBuilder.setPositiveButton("RETURN") { dialog, _ ->
            dialog.dismiss()
            callback(true)
        }

        alertDialogBuilder.setNegativeButton("CANCEL") { dialog, _ ->
            dialog.dismiss()
            callback(false)
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun returnBorrowerConfirmation(borrower: Borrower, callback: (Boolean) -> Unit) {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Confirm Return Borrower")
        alertDialogBuilder.setMessage("Are you sure you want to return all of ${borrower.borrowerName}'s borrowed items to their original locations?")

        alertDialogBuilder.setPositiveButton("RETURN") { dialog, _ ->
            dialog.dismiss()
            callback(true)
        }

        alertDialogBuilder.setNegativeButton("CANCEL") { dialog, _ ->
            dialog.dismiss()
            callback(false)
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

}
