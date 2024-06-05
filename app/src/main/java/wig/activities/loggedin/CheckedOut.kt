package wig.activities.loggedin

import android.os.Bundle
import android.view.Gravity
import android.widget.ExpandableListView
import android.widget.LinearLayout
import android.widget.TableLayout
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

class CheckedOut : Settings() {

    private lateinit var borrowers: List<Borrowers>
    private lateinit var tableLayout: ExpandableListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setScreenOrientation()
        setCheckedOutBindings()
        tableLayout = checkedOutBinding.searchTableLayout
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

    // returnAllButton handles the button click of Return All
    private fun returnAllButton() {
        // TODO
    }

    // populateTable populates the table with the list of borrowers
    private fun populateTable(borrowers: List<Borrowers>) {
        // TODO
    }

}