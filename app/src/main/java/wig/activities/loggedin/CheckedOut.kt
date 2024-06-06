package wig.activities.loggedin

import android.os.Bundle
import android.util.Log
import android.widget.ExpandableListView
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import wig.activities.base.Settings
import wig.managers.BorrowersExpandableListAdapter

class CheckedOut : Settings() {

    private lateinit var expandableListView: ExpandableListView
    private lateinit var adapter: BorrowersExpandableListAdapter

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
        // checkedOutBinding.returnAllButton.setOnClickListener { returnAllButton() }
    }

    // getBorrowedItems returns all of the borrowed items and their borrowers
    private fun getInventory() {
        lifecycleScope.launch {
            val response = api.borrowerGetInventory()
            if (response.success) {
                val borrowers = response.borrowers
                adapter = BorrowersExpandableListAdapter(this@CheckedOut, borrowers)
                expandableListView.setAdapter(adapter)
                expandableListView.expandGroup(0)
            }
        }
    }

    // returnAllButton handles the button click of Return All
    private fun returnAllButton() {
        // TODO
    }

}