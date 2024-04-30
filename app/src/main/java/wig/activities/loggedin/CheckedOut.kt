package wig.activities.loggedin

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
import wig.utils.Alerts
import wig.utils.TableManager

class CheckedOut : Settings() {

    private val borrowerRowMap = mutableMapOf<String, TableRow>()
    private lateinit var borrowers: List<Borrowers>
    private val tableLayout = checkedOutBinding.searchTableLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setScreenOrientation()
        setCheckedOutBindings()
        setOnClickListeners()
        getInventory()
    }

    private fun setOnClickListeners() {
        checkedOutBinding.topMenu.icScanner.setOnClickListener { startActivityScanner() }
        checkedOutBinding.topMenu.icSettings.setOnClickListener { startActivitySettings() }
        checkedOutBinding.topMenu.icCheckedOut.setOnClickListener { startActivityCheckedOut() }
        checkedOutBinding.topMenu.icInventory.setOnClickListener { startActivityInventory() }
        checkedOutBinding.returnAllButton.setOnClickListener { returnAllButton() }
    }

    // getBorrowedItems returns all of the borrowed items and their borrowers
    private fun getInventory() {
        lifecycleScope.launch {
            val response = api.borrowerGetInventory()
            if (response.success) {
                borrowers = response.borrowers
                populateTable(borrowers)
            }
        }
    }

    // populateTable populates the table with the list of borrowers
    private fun populateTable(borrowers: List<Borrowers>) {
        borrowers.forEach { borrower ->
            val row = createRowForBorrower(borrower.borrower)
            TableManager().setColorForRow(row, tableLayout.childCount)
            setRowListeners(row, borrower)
            tableLayout.addView(row)
        }
        TableManager().resetRowColors(tableLayout)
    }

    private fun returnAllButton() {
        Alerts().returnAllConfirmation(this) { shouldDelete ->
            if (shouldDelete) {
                checkInBorrowers()
                clearBorrowersListAndRowMap()
            }
        }
    }

    private fun checkInBorrowers(){
        for (borrower in borrowers) {
            val ownerships = borrower.ownerships.map { it.ownershipUID }
            val checkOutRequest = CheckoutRequest(ownerships)
            lifecycleScope.launch {
                api.borrowerCheckIn(checkOutRequest)
            }
        }
    }

    private fun clearBorrowersListAndRowMap() {
        borrowers = emptyList()
        borrowerRowMap.clear()
        tableLayout.removeAllViews()
    }

    private fun checkInAllFromBorrower(borrower: Borrowers) {
        val ownerships = borrower.ownerships.map { it.ownershipUID }
        val checkOutRequest = CheckoutRequest(ownerships)
        lifecycleScope.launch {
            val response = api.borrowerCheckIn(checkOutRequest)
            if (response.success){
                removeOwnerships(borrower, ownerships)
            }
        }
    }

    private fun checkInSingleOwnership(ownership: Ownership, borrower: Borrowers) {
        Alerts().returnSingleConfirmation(ownership, this) { shouldDelete ->
            if (shouldDelete) {
                checkInOwnership(ownership, borrower)
            }
        }
    }

    // handleCheckIn handles the check-in process
    private fun checkInOwnership(ownership: Ownership, borrower: Borrowers) {
        val ownerships = listOf(ownership.ownershipUID) // Create a list with the single ownership UID
        val checkOutRequest = CheckoutRequest(ownerships)

        lifecycleScope.launch {
            val response = api.borrowerCheckIn(checkOutRequest)
            if (response.success) {
                removeOwnership(ownership, borrower)
            }
        }
    }

    // removeOwnershipAndRows will remove ownerships and their corresponding rows
    private fun removeOwnerships(borrower: Borrowers, ownerships: List<String>) {
        for (ownership in ownerships) {
            val ownershipToRemove = borrower.ownerships.find { it.ownershipUID == ownership }
            ownershipToRemove?.let { itRemove ->
                removeOwnership(itRemove, borrower)
            }
        }
    }

    // removeOwnershipAndRow removes the ownership and its corresponding row
    private fun removeOwnership(ownership: Ownership, borrower: Borrowers) {
        val ownershipToRemove = borrower.ownerships.find { it.ownershipUID == ownership.ownershipUID }
        ownershipToRemove?.let { itRemove ->
            borrower.ownerships.remove(itRemove)
            val rowToRemove = borrowerRowMap[ownership.ownershipUID]
            rowToRemove?.let {
                tableLayout.removeView(it)
                borrowerRowMap.remove(ownership.ownershipUID)
            }
        }
    }

    // setRowListeners sets the listeners for the rows being created
    private fun setRowListeners(row: TableRow, borrower: Borrowers) {
        row.setOnClickListener {
            borrowerClick(it as TableRow, borrower)
        }
        row.setOnLongClickListener {
            checkInBorrower(borrower)
        }
    }

    // returnBorrower alerts to return a single borrowers ownerships
    private fun checkInBorrower(borrower: Borrowers): Boolean {
        Alerts().returnBorrowerConfirmation(borrower.borrower, this) { shouldDelete ->
            if (shouldDelete) {
                checkInAllFromBorrower(borrower)
            }
        }
        return true
    }

    // borrowerClick handles the expanding and collapsing of a Borrower
    private fun borrowerClick(clickedRow: TableRow, borrowers: Borrowers) {
        val rowIndex = tableLayout.indexOfChild(clickedRow)
        val expandTextView = (clickedRow.getChildAt(0) as LinearLayout).getChildAt(1) as TextView

        if (expandTextView.text == " >") {
            expandBorrower(borrowers, rowIndex)
            expandTextView.text = " v"
        } else if (expandTextView.text == " v") {
            collapseBorrower(borrowers)
            expandTextView.text = " >"
        }
        TableManager().resetRowColors(tableLayout)
    }

    // collapseBorrower removes Ownerships of a Borrower from the table to collapse
    private fun collapseBorrower(borrowers: Borrowers) {
        for (ownership in borrowers.ownerships) {
            val rowToRemove = borrowerRowMap[ownership.ownershipUID]
            rowToRemove?.let {
                tableLayout.removeView(it)
                borrowerRowMap.remove(ownership.ownershipUID)
            }
        }
    }

    // expandBorrower adds ownerships to the table under a Borrower to expand
    private fun expandBorrower(borrowers: Borrowers, rowIndex: Int) {
        for (ownership in borrowers.ownerships) {
            val newRow = createRowForOwnership(ownership, borrowers)
            TableManager().setColorForRow(newRow, rowIndex + 1)
            tableLayout.addView(newRow, rowIndex + 1)
        }
    }

    // createRowForOwnership creates the row for the ownership to be added to the table
    private fun createRowForOwnership(ownership: Ownership, borrower: Borrowers): TableRow {
        val name = "        " + ownership.customItemName.substring(0, 25.coerceAtMost(ownership.customItemName.length))

        val row = TableRow(this)
        val layoutParams = TableRow.LayoutParams(
            TableRow.LayoutParams.MATCH_PARENT,
            TableRow.LayoutParams.WRAP_CONTENT
        )

        val nameLayout = LinearLayout(this)
        val nameLayoutParams = TableRow.LayoutParams(
            0, TableRow.LayoutParams.MATCH_PARENT, 0.34f
        )
        nameLayout.layoutParams = nameLayoutParams
        nameLayout.gravity = Gravity.START or Gravity.CENTER_VERTICAL

        val nameView = TextView(this)
        nameView.text = name

        nameLayout.addView(nameView)
        row.addView(nameLayout)

        row.layoutParams = layoutParams
        borrowerRowMap[ownership.ownershipUID] = row

        row.setOnClickListener { checkInSingleOwnership(ownership, borrower) }

        return row
    }

    // createRowForBorrower takes a Borrower and creates a row to be added to the table
    private fun createRowForBorrower(borrower: Borrower): TableRow {
        val name = borrower.borrowerName
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
        nameView.text = name.substring(0 until 25.coerceAtMost(name.length))
        nameLayout.addView(nameView)

        val expandView = TextView(this)
        expandView.text = expand
        nameLayout.addView(expandView)

        row.addView(nameLayout)
        borrowerRowMap[borrower.borrowerUID] = row

        return row
    }

}
