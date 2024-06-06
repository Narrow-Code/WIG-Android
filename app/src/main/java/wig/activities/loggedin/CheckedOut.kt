package wig.activities.loggedin

import android.os.Bundle
import android.widget.ExpandableListView
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import wig.activities.base.Settings
import wig.managers.BorrowersExpandableListAdapter
import wig.models.requests.CheckoutRequest
import wig.models.responses.Borrowers
import wig.utils.Alerts

class CheckedOut : Settings() {

    private lateinit var expandableListView: ExpandableListView
    private lateinit var adapter: BorrowersExpandableListAdapter
    private lateinit var borrowers: MutableList<Borrowers>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setScreenOrientation()
        setCheckedOutBindings()
        expandableListView = checkedOutBinding.searchTableLayout
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
                borrowers = response.borrowers.toMutableList()
                adapter = BorrowersExpandableListAdapter(this@CheckedOut, borrowers)
                expandableListView.setAdapter(adapter)
            }
        }
    }

    // returnAllButton handles the button click of Return All
    private fun returnAllButton() {
        Alerts().returnAllConfirmation(this) { shouldDelete ->
            if (shouldDelete) {
                checkInBorrowers()
            }
        }
    }

    // checkInBorrowers checks in all of the borrowers
    private fun checkInBorrowers(){
        val ownerships: MutableList<String> = mutableListOf()
        for (borrower in borrowers) {
            borrower.ownerships.map { ownerships.add(it.ownershipUID) }
        }
        val checkOutRequest = CheckoutRequest(ownerships)
        lifecycleScope.launch {
            val response = api.borrowerCheckIn(checkOutRequest)
            if (response.success){
                borrowers.clear()
                adapter.notifyDataSetChanged()
            }
        }
    }

}