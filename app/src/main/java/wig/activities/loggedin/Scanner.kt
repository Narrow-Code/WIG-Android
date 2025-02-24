package wig.activities.loggedin

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.os.StrictMode
import androidx.recyclerview.widget.LinearLayoutManager
import android.os.StrictMode.ThreadPolicy
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.google.zxing.BarcodeFormat
import kotlinx.coroutines.launch
import wig.R
import wig.activities.base.Camera
import wig.api.API
import wig.databinding.CreateNewBinding
import wig.databinding.SearchBinding
import wig.managers.LocationAdapter
import wig.models.entities.Ownership
import wig.managers.OwnershipAdapter
import wig.managers.SearchLocationAdapter
import wig.managers.SearchOwnershipAdapter
import wig.models.entities.Location
import wig.models.requests.CheckoutRequest
import wig.models.requests.OwnershipCreateRequest
import wig.models.requests.SearchRequest
import wig.models.responses.InventoryDTO
import wig.models.responses.borrowerGetAllResponse
import wig.utils.Alerts

class Scanner : Camera() {
    private var pageView = "items"
    private lateinit var ownershipRecyclerView: RecyclerView
    private lateinit var locationRecyclerView: RecyclerView
    private lateinit var searchRecyclerView: RecyclerView
    private lateinit var ownershipAdapter: OwnershipAdapter
    private lateinit var locationAdapter: LocationAdapter
    private lateinit var searchOwnershipAdapter: SearchOwnershipAdapter
    private lateinit var searchLocationAdapter: SearchLocationAdapter
    private val ownershipList = mutableListOf<Ownership>()
    private val locationList = mutableListOf<Location>()
    private val searchOwnershipList = mutableListOf<Ownership>()
    private val searchLocationList = mutableListOf<Location>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setScreenOrientation()
        setScannerBindings()
        setupPermissions()
        codeScanner()
        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        checkForUpdates()
        setOnClickListeners()
        switchToItemsView()

        ownershipRecyclerView = findViewById(R.id.items_table_recycler_view)
        ownershipAdapter = OwnershipAdapter(ownershipList, this, API(), this)
        ownershipRecyclerView.adapter = ownershipAdapter
        ownershipRecyclerView.layoutManager = LinearLayoutManager(this)

        locationRecyclerView = findViewById(R.id.location_table_recycler_view)
        locationAdapter = LocationAdapter(locationList, this, API(), this)
        locationRecyclerView.adapter = locationAdapter
        locationRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun setOnClickListeners() {
        scannerBinding.locationsButton.setOnClickListener { switchToLocationsView() }
        scannerBinding.itemsButton.setOnClickListener { switchToItemsView() }
        scannerBinding.topMenu.icScanner.setOnClickListener { startActivityScanner() }
        scannerBinding.topMenu.icSettings.setOnClickListener { startActivitySettings() }
        scannerBinding.topMenu.icCheckedOut.setOnClickListener { startActivityCheckedOut() }
        scannerBinding.topMenu.icInventory.setOnClickListener { startActivityInventory() }
        scannerBinding.clear.setOnClickListener { clearButton() }
        scannerBinding.place.setOnClickListener { placeQueueButton() }
        scannerBinding.add.setOnClickListener { newEntry() }
        scannerBinding.unpack.setOnClickListener { unpackButton() }
        scannerBinding.checkOut.setOnClickListener { checkoutButton() }
        scannerBinding.search.setOnClickListener { searchButton() }
    }

    override suspend fun scanSuccess(code: String, barcodeFormat: BarcodeFormat) {
        codeScanner.stopPreview()
        if (barcodeFormat != BarcodeFormat.QR_CODE) {
            val response = api.scannerBarcode(code)
            if (response.message == "429") {
                Toast.makeText(this@Scanner, "LIMIT REACHED", Toast.LENGTH_SHORT).show()
            }
            for (ownership in response.ownership){
                ownershipAdapter.addOwnership(ownership)
            }
            switchToItemsView()
            codeScanner.startPreview()
        } else {
            val response = api.scannerCheckQR(code)
            when (response.message) {
                "NEW" -> {
                    newEntry(code)
                }

                "LOCATION" -> {
                    val locationResponse = api.scannerQRLocation(code)
                    locationAdapter.addLocation(locationResponse.location)
                    switchToLocationsView()
                }

                "OWNERSHIP" -> {
                    val ownershipResponse = api.scanQROwnership(code)
                    ownershipAdapter.addOwnership(ownershipResponse.ownership)
                    switchToItemsView()
                    codeScanner.startPreview()
                }
            }
        }
    }

    private fun newEntry(qr: String? = null) {
        codeScanner.stopPreview()

        val createNewBinding: CreateNewBinding = CreateNewBinding.inflate(layoutInflater)
        val popupDialog = Dialog(this)
        popupDialog.setContentView(createNewBinding.root)
        qr?.let { createNewBinding.qrCodeEditText.setText(it) }
        popupDialog.setOnDismissListener { codeScanner.startPreview() }

        val layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        popupDialog.window?.setLayout(layoutParams.width, layoutParams.height)

        createNewBinding.createButton.setOnClickListener {
            createNewButton(
                createNewBinding,
                popupDialog
            )
        }
        createNewBinding.cancelButton.setOnClickListener { popupDialog.dismiss() }

        val spinnerPosition = if (pageView == "items") 0 else 1
        createNewBinding.typeSpinner.setSelection(spinnerPosition)

        popupDialog.show()
    }

    private fun createNewButton(createNewBinding: CreateNewBinding, popup: Dialog) {
        val typeSpinner = createNewBinding.typeSpinner
        val name = createNewBinding.nameEditText.text.toString()
        val qr = createNewBinding.qrCodeEditText.text.toString()
        if (name == "" || qr == "") {
            return
        }
        lifecycleScope.launch {
            when (typeSpinner.selectedItem?.toString() ?: "") {
                "Location" -> {
                    val response = api.locationCreate(name, qr)
                    if (response.success) {
                        Toast.makeText(this@Scanner, "Location created", Toast.LENGTH_SHORT).show()
                        locationAdapter.addLocation(response.location)
                        popup.dismiss()
                        switchToLocationsView()
                    }
                }

                "Item" -> {
                    val request = OwnershipCreateRequest(qr, name)
                    val response = api.ownershipCreate(request)
                    if (response.success) {
                        Toast.makeText(this@Scanner, "Ownership created", Toast.LENGTH_SHORT).show()
                        ownershipAdapter.addOwnership(response.ownership)
                        popup.dismiss()
                        switchToItemsView()
                    }
                }
            }
        }
    }

    private fun checkoutButton() {
        codeScanner.stopPreview()
        lifecycleScope.launch {
            val borrowers = api.borrowerGetAll()
            checkoutPrompt(borrowers)
        }
    }

    private fun checkoutPrompt(borrowers: borrowerGetAllResponse) {
        val borrowerNames = arrayOf("Self") +
                borrowers.borrowers.map { it.borrowerName }.toTypedArray() +
                "New"

        val dialogBuilder = AlertDialog.Builder(this@Scanner)
        dialogBuilder.setTitle("Checkout to:")

        dialogBuilder.setSingleChoiceItems(
            ArrayAdapter(this@Scanner, android.R.layout.select_dialog_singlechoice, borrowerNames),
            -1
        ) { dialog, which ->
            var borrowerUUID = "Default"
            when (borrowerNames[which]) {
                "New" -> {
                    checkoutNew()
                }

                "Self" -> {
                    borrowerUUID = "22222222-2222-2222-2222-222222222222"
                }

                else -> {
                    val selectedBorrower = borrowers.borrowers[which - 1]
                    borrowerUUID = selectedBorrower.borrowerUID
                }
            }
            checkoutOwnerships(borrowerUUID, borrowerNames[which])
            // Dismiss the dialog
            dialog.dismiss()
            codeScanner.startPreview()
        }
        // Create and show the AlertDialog
        val dialog = dialogBuilder.create()
        dialog.show()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun checkoutOwnerships(uuid: String, name: String) {
        val ownerships: MutableList<String> = mutableListOf()
        for (ownership in ownershipList) {
            ownerships.add(ownership.ownershipUID)
        }
        val request = CheckoutRequest(ownerships)
        lifecycleScope.launch {
            val response = api.borrowerCheckout(uuid, request)
            if (response.success) {
                for (ownershipSuccess in response.ownerships) {
                    ownershipList.forEach { (ownershipUID) ->
                        if (ownershipUID == ownershipSuccess) {
                            for (ownership in ownershipList) {
                                if (ownership.ownershipUID == ownershipUID) {
                                    ownership.borrower.borrowerName = name // TODO fix so it changes entire borrower
                                    ownership.itemCheckedOut = "true"
                                }
                            }
                        }
                    }
                }
                ownershipAdapter.notifyDataSetChanged()
                Toast.makeText(
                    this@Scanner,
                    "Checked out",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun checkoutNew() {
        Alerts().showNewBorrowerDialog(this@Scanner) { borrowerName ->
            lifecycleScope.launch {
                val response = api.borrowerCreate(borrowerName)
                if (response.success) {
                    Toast.makeText(
                        this@Scanner,
                        "Created: $borrowerName",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                checkoutButton()
            }
        }
    }

    private fun clearButton() {
        if (pageView == "items") {
            ownershipAdapter.clearOwnerships()
        } else if (pageView == "locations") {
            locationAdapter.clearLocations()
        }
    }

    private fun unpackButton() {
        for (location in locationList) {
            lifecycleScope.launch {
                val unpacked = api.locationUnpack(location.locationUID)
                unpackInventory(unpacked.inventory)
            }
        }
    }

    private fun unpackInventory(inventoryDTO: InventoryDTO) {
        inventoryDTO.ownerships?.let { ownerships ->
            if (ownerships.isNotEmpty()) {
                for (ownership in ownerships) {
                    ownershipAdapter.addOwnership(ownership)
                }
            }
        }
        inventoryDTO.locations?.let { locations ->
            if (locations.isNotEmpty()) {
                for (location in locations) {
                    locationAdapter.addLocation(location.parent)
                    unpackInventory(location)
                }
            }
        }
    }

    private fun placeQueueButton() {
        if (locationList.isNotEmpty() and ownershipList.isNotEmpty()) {
            codeScanner.stopPreview()

            val dialogBuilder = AlertDialog.Builder(this@Scanner)
            dialogBuilder.setTitle("Place Queue in:")
            val locations = locationAdapter.getAllLocationNames()

            dialogBuilder.setSingleChoiceItems(
                ArrayAdapter(this@Scanner, android.R.layout.select_dialog_singlechoice, locations),
                -1
            )
            { dialog, which ->
                when (locations[which]) {
                    else -> {
                        val selectedLocation = locationList[which]

                        setLocation(selectedLocation)
                    }
                }
                // Dismiss the dialog
                dialog.dismiss()
                codeScanner.startPreview()
            }

            // Create and show the AlertDialog
            val dialog = dialogBuilder.create()
            dialog.show()
        }
    }

    private fun setLocation(selectedLocation: Location) {
        val uid = selectedLocation.locationUID

        for (ownership in ownershipList) {
            lifecycleScope.launch {
                val response = api.ownershipSetLocation(ownership.ownershipUID, uid)
                if (response.success) {
                    updateLocationForAllRows(selectedLocation)
                } else {
                    // TODO handle negative
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateLocationForAllRows(location: Location) {
        for (ownership in ownershipList) {
            ownership.location = location
        }
        ownershipAdapter.notifyDataSetChanged()
    }

    private fun searchButton() {
        codeScanner.stopPreview()

        val searchBinding: SearchBinding = SearchBinding.inflate(layoutInflater)
        val popupDialog = Dialog(this)
        popupDialog.setContentView(searchBinding.root)
        popupDialog.setOnDismissListener { codeScanner.startPreview() }

        val layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        popupDialog.window?.setLayout(layoutParams.width, layoutParams.height)

        if (pageView == "items") {
            searchRecyclerView = searchBinding.searchTableRecyclerView
            searchOwnershipAdapter = SearchOwnershipAdapter(searchOwnershipList, this, ownershipAdapter)
            searchRecyclerView.adapter = searchOwnershipAdapter
            searchRecyclerView.layoutManager = LinearLayoutManager(this)

            searchBinding.searchButton.setOnClickListener { searchOwnershipButton(searchBinding) }
        } else {
            searchRecyclerView = searchBinding.searchTableRecyclerView
            searchLocationAdapter = SearchLocationAdapter(searchLocationList, this, locationAdapter)
            searchRecyclerView.adapter = searchLocationAdapter
            searchRecyclerView.layoutManager = LinearLayoutManager(this)

            searchBinding.searchButton.setOnClickListener { searchLocationButton(searchBinding) }
        }
        searchBinding.cancelButton.setOnClickListener { popupDialog.dismiss() }

        popupDialog.show()
    }

    private fun searchOwnershipButton(searchBinding: SearchBinding) {
        val name = searchBinding.nameSearchText.text.toString()
        val tags = searchBinding.tagsSearchText.text.toString()
        val tableLayout = searchBinding.searchTableRecyclerView
        tableLayout.removeAllViews()
        searchOwnershipList.clear()
        if (name == "" && tags == "") {
            return
        }
        lifecycleScope.launch {
            val response = api.ownershipSearch(SearchRequest(name, tags))
            if (response.success) {
                for (ownership in response.ownership) {
                    searchOwnershipAdapter.addOwnership(ownership)
                }
            }
        }
    }

    private fun searchLocationButton(searchBinding: SearchBinding) {
        val name = searchBinding.nameSearchText.text.toString()
        val tags = searchBinding.tagsSearchText.text.toString()
        val tableLayout = searchBinding.searchTableRecyclerView
        tableLayout.removeAllViews()
        searchOwnershipList.clear()
        if (name == "" && tags == "") {
            return
        }
        lifecycleScope.launch {
            val response = api.locationSearch(SearchRequest(name, tags))
            if (response.success) {
                for (location in response.locations) {
                    searchLocationAdapter.addLocation(location)
                }
            }
        }
    }

    private fun switchToLocationsView() {
        scannerBinding.tableItemsTitles.visibility = View.INVISIBLE
        scannerBinding.itemsTable.visibility = View.INVISIBLE
        scannerBinding.tableLocationTitles.visibility = View.VISIBLE
        scannerBinding.locationsTable.visibility = View.VISIBLE
        scannerBinding.place.visibility = View.INVISIBLE
        scannerBinding.unpack.visibility = View.VISIBLE
        pageView = "locations"
    }

    private fun switchToItemsView() {
        scannerBinding.tableLocationTitles.visibility = View.INVISIBLE
        scannerBinding.locationsTable.visibility = View.INVISIBLE
        scannerBinding.tableItemsTitles.visibility = View.VISIBLE
        scannerBinding.itemsTable.visibility = View.VISIBLE
        scannerBinding.place.visibility = View.VISIBLE
        scannerBinding.unpack.visibility = View.INVISIBLE
        pageView = "items"
    }
}