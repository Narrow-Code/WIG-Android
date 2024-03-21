package wig.activities

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import wig.activities.bases.BaseActivity
import wig.api.dto.Borrowers
import wig.models.Borrower
import wig.models.Ownership

class CheckedOut : BaseActivity() {

    private val borrowerRowMap = mutableMapOf<Int, TableRow>()

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
    }

    private fun getBorrowedItems() {
        lifecycleScope.launch {
            val response = getCheckedOutItems()
            if (response.success) {
                val borrowers = response.borrowers
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
        Log.d("TEST", expand.text.toString())

        if (expand.text == " >") {
            for (i in borrowers.ownerships) {
                val newRow = createRowForOwnership(i) // Implement this method to create a new row
                setColorForRow(newRow, rowIndex + 1)
                tableLayout.addView(newRow, rowIndex + 1) // Insert new row at index + 1
            }
            expand.text = " v"
            resetRowColors(tableLayout)
        }

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
        borrowerRowMap[ownership.ownershipUID] = row

        return row
    }

    private fun resetRowColors(tableLayout: LinearLayout) {
        for (i in 0 until tableLayout.childCount) {
            val row = tableLayout.getChildAt(i) as? TableRow
            row?.let { setColorForRow(it, i) }
        }
    }

}
