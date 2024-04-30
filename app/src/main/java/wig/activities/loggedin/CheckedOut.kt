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
import wig.tables.TableManager

class CheckedOut : Settings() {

    private val borrowerRowMap = mutableMapOf<String, TableRow>()
    private lateinit var borrowers: List<Borrowers>
    private val tableLayout = checkedOutBinding.searchTableLayout

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
        Alerts().returnAllConfirmation(this) { shouldDelete ->
            if (shouldDelete) {
                processBorrowers()
                clearBorrowersListAndRowMap()
            }
        }
    }

    private fun processBorrowers(){
        for (borrower in borrowers) {
            val ownerships = borrower.ownerships.map { it.ownershipUID }
            val checkOutRequest = CheckoutRequest(ownerships)
            lifecycleScope.launch {
                borrowerCheckIn(checkOutRequest)
            }
        }
    }

    private fun clearBorrowersListAndRowMap() {
        borrowers = emptyList()
        borrowerRowMap.clear()
        tableLayout.removeAllViews()
    }

    private fun returnAllFromBorrower(borrower: Borrowers) {
        val ownerships = borrower.ownerships.map { it.ownershipUID }
        val checkOutRequest = CheckoutRequest(ownerships)
        lifecycleScope.launch {
            val response = borrowerCheckIn(checkOutRequest)
            if (response.success){
                removeOwnershipAndRows(borrower, ownerships)
            }
        }
    }

    private fun returnOneItem(ownership: Ownership, borrower: Borrowers) {
        Alerts().returnSingleConfirmation(ownership, this) { shouldDelete ->
            if (shouldDelete) {
                handleCheckIn(ownership, borrower)
            }
        }
    }

    // handleCheckIn handles the check-in process
    private fun handleCheckIn(ownership: Ownership, borrower: Borrowers) {
        val ownerships = listOf(ownership.ownershipUID) // Create a list with the single ownership UID
        val checkOutRequest = CheckoutRequest(ownerships)

        lifecycleScope.launch {
            val response = borrowerCheckIn(checkOutRequest)
            if (response.success) {
                removeOwnershipAndRow(ownership, borrower)
            }
        }
    }

    // removeOwnershipAndRows will remove ownerships and their corresponding rows
    private fun removeOwnershipAndRows(borrower: Borrowers, ownerships: List<String>) {
        for (ownership in ownerships) {
            val ownershipToRemove = borrower.ownerships.find { it.ownershipUID == ownership }
            ownershipToRemove?.let { itRemove ->
                removeOwnershipAndRow(itRemove, borrower)
            }
        }
    }

    // removeOwnershipAndRow removes the ownership and its corresponding row
    private fun removeOwnershipAndRow(ownership: Ownership, borrower: Borrowers) {
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

    // getBorrowedItems returns all of the borrowed items and their borrowers
    private fun getBorrowedItems() {
        lifecycleScope.launch {
            val response = borrowerGetInventory()
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

    // setRowListeners sets the listeners for the rows being created
    private fun setRowListeners(row: TableRow, borrower: Borrowers) {
        row.setOnClickListener {
            borrowerClick(it as TableRow, borrower)
        }
        row.setOnLongClickListener {
            handleReturnBorrower(borrower)
        }
    }

    // handleReturnBorrower alerts to return a single borrowers ownerships
    private fun handleReturnBorrower(borrower: Borrowers): Boolean {
        Alerts().returnBorrowerConfirmation(borrower.borrower, this) { shouldDelete ->
            if (shouldDelete) {
                returnAllFromBorrower(borrower)
            }
        }
        return true
    }

    // borrowerClick handles the expanding and collapsing of a Borrower
    private fun borrowerClick(clickedRow: TableRow, borrowers: Borrowers) {
        val rowIndex = tableLayout.indexOfChild(clickedRow)
        val expandTextView = (clickedRow.getChildAt(0) as LinearLayout).getChildAt(1) as TextView

        if (expandTextView.text == " >") {
            addOwnershipRows(borrowers, rowIndex)
            expandTextView.text = " v"
        } else if (expandTextView.text == " v") {
            collapseBorrower(borrowers)
            expandTextView.text = " >"
        }
        TableManager().resetRowColors(tableLayout)
    }

    // addOwnershipRows adds an ownership to a row in the table at the right index
    private fun addOwnershipRows(borrowers: Borrowers, rowIndex: Int) {
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

        row.setOnClickListener { returnOneItem(ownership, borrower) }

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
