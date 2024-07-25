package wig.activities.loggedin

import android.os.Bundle
import android.widget.ExpandableListView
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import wig.activities.base.Settings
import wig.managers.BorrowersExpandableListAdapter
import wig.models.entities.Ownership
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
        checkedOutBinding.topMenu.icScanner.setOnClickListener {
            startActivityScanner()
            finish()
        }
        checkedOutBinding.topMenu.icSettings.setOnClickListener {
            startActivitySettings()
            finish()
        }
        checkedOutBinding.topMenu.icInventory.setOnClickListener {
            startActivityInventory()
            finish()
        }
        checkedOutBinding.returnAllButton.setOnClickListener { returnAllButton() }
    }

    private fun getInventory() {
        lifecycleScope.launch {
            val response = api.borrowerGetInventory()
            if (response.success) {
                borrowers = response.borrowers.toMutableList()
                adapter = BorrowersExpandableListAdapter(this@CheckedOut, this@CheckedOut, borrowers, api)
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
                val borrower = adapter.getGroup(groupPosition)
                checkInBorrower(borrower, groupPosition)
            } else {
                val childPosition = ExpandableListView.getPackedPositionChild(packedPosition)
                val ownership = adapter.getChild(groupPosition, childPosition)
                checkInInSingleOwnership(ownership, groupPosition, childPosition)
            }
            true
        }
    }

    private fun returnAllButton() {
        Alerts().returnAllConfirmation(this) { shouldDelete ->
            if (shouldDelete) {
                val ownerships: MutableList<String> = mutableListOf()
                for (borrower in borrowers) {
                    borrower.ownerships.map { ownerships.add(it.ownershipUID)}
                }
                lifecycleScope.launch {
                    val result = checkInOwnerships(ownerships)
                    if (result){
                        borrowers.clear()
                        adapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }

    private suspend fun checkInOwnerships(ownerships: MutableList<String>): Boolean {
        val checkOutRequest = CheckoutRequest(ownerships)
        return withContext(Dispatchers.IO) {
            val response = api.borrowerCheckIn(checkOutRequest)
            response.success
        }
    }


    private fun checkInBorrower(borrower: Borrowers, groupPosition: Int) {
        Alerts().returnBorrowerConfirmation(borrower.borrower, this) { shouldDelete ->
            if (shouldDelete) {
                val ownerships: MutableList<String> = mutableListOf()
                borrower.ownerships.map { ownerships.add(it.ownershipUID)}
                lifecycleScope.launch {
                    if (checkInOwnerships(ownerships)){
                        adapter.removeGroup(groupPosition)
                    }
                }
            }
        }
    }

    private fun checkInInSingleOwnership(ownership: Ownership, groupPosition: Int, childPosition: Int) {
        Alerts().returnSingleConfirmation(ownership, this) { shouldDelete ->
            if (shouldDelete) {
                val ownerships: MutableList<String> = mutableListOf()
                ownerships.add(ownership.ownershipUID)
                lifecycleScope.launch {
                    val result = checkInOwnerships(ownerships)
                    if(result){
                        adapter.removeChild(groupPosition, childPosition)
                    }
                }
            }
        }
    }
}