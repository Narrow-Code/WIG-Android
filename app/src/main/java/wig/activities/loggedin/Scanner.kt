package wig.activities.loggedin

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.google.zxing.BarcodeFormat
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import wig.R
import wig.activities.base.Camera
import wig.models.requests.CheckoutRequest
import wig.models.requests.LocationEditRequest
import wig.models.requests.OwnershipEditRequest
import wig.models.responses.InventoryDTO
import wig.models.requests.OwnershipCreateRequest
import wig.models.responses.ScannerBarcodeResponse
import wig.models.requests.SearchRequest
import wig.databinding.CreateNewBinding
import wig.databinding.EditLocationBinding
import wig.databinding.EditOwnershipBinding
import wig.databinding.SearchBinding
import wig.models.entities.ItemViewModel
import wig.models.entities.Location
import wig.models.entities.Ownership
import wig.managers.LocationManager
import wig.managers.OwnershipManager
import wig.models.responses.borrowerGetAllResponse
import wig.utils.Alerts


class Scanner : Camera() {
    private var pageView = "items"
    private val ownershipRowMap = mutableMapOf<String, TableRow>()
    private val locationRowMap = mutableMapOf<String, TableRow>()
    private val searchRowMap = mutableMapOf<String, TableRow>()

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
    }

    private fun setOnClickListeners() {
        scannerBinding.locationsButton.setOnClickListener{ switchToLocationsView() }
        scannerBinding.itemsButton.setOnClickListener{ switchToItemsView() }
        scannerBinding.topMenu.icScanner.setOnClickListener { startActivityScanner() }
        scannerBinding.topMenu.icSettings.setOnClickListener { startActivitySettings() }
        scannerBinding.topMenu.icCheckedOut.setOnClickListener { startActivityCheckedOut() }
        scannerBinding.topMenu.icInventory.setOnClickListener { startActivityInventory() }
        scannerBinding.clear.setOnClickListener { clearButton() }
        scannerBinding.place.setOnClickListener { placeQueueButton() }
        scannerBinding.add.setOnClickListener { newEntry() }
        scannerBinding.unpack.setOnClickListener {unpackButton()}
        scannerBinding.checkOut.setOnClickListener {checkoutButton()}
        scannerBinding.search.setOnClickListener { searchButton() }
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

    private fun checkoutOwnerships(uuid: String, name: String) {
        val ownerships: MutableList<String> = mutableListOf()
        for (ownership in OwnershipManager.getAllOwnerships()){
            ownerships.add(ownership.ownershipUID)
        }
        val request = CheckoutRequest(ownerships)
        lifecycleScope.launch {
            val response = api.borrowerCheckout(uuid, request)
            if (response.success){
                for (ownershipSuccess in response.ownerships){
                    ownershipRowMap.entries.forEach { (ownershipUID, row) ->
                        if (ownershipUID == ownershipSuccess) {
                            val locationView = (row.getChildAt(1) as LinearLayout).getChildAt(0) as TextView
                            locationView.text = name
                        }
                    }
                }
                Toast.makeText(
                    this@Scanner,
                    "Checked out",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


    private fun checkoutNew(){
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

    private fun createLinearLayout(weight: Float, gravity: Int): LinearLayout {
        return LinearLayout(this).apply {
            layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, weight)
            this.gravity = gravity
        }
    }

    private fun createTextView(text: String): TextView {
        return TextView(this).apply {
            this.text = text
        }
    }

    private fun createButton(text: String, layoutParams: ViewGroup.LayoutParams, onClick: () -> Unit): Button {
        return Button(this).apply {
            this.text = text
            this.layoutParams = layoutParams
            gravity = Gravity.END
            setOnClickListener { onClick.invoke() }
        }
    }

    private fun ownershipOnClick(ownership: Ownership, layoutParams: TableRow.LayoutParams, row: TableRow) {
        codeScanner.stopPreview()

        val editOwnershipBinding: EditOwnershipBinding = EditOwnershipBinding.inflate(layoutInflater)
        val popupDialog = Dialog(this)
        popupDialog.setContentView(editOwnershipBinding.root)
        popupDialog.setOnDismissListener { codeScanner.startPreview() }

        val viewModel = ItemViewModel()
        viewModel.name = ownership.customItemName
        viewModel.qr = ownership.itemQR
        viewModel.description = ownership.customItemDescription
        viewModel.tags = ownership.itemTags
        editOwnershipBinding.viewModel = viewModel

        popupDialog.window?.setLayout(layoutParams.width, layoutParams.height)

        editOwnershipBinding.cancelButton.setOnClickListener{popupDialog.dismiss()}
        editOwnershipBinding.saveButton.setOnClickListener {
            val editOwnershipRequest =
                OwnershipEditRequest(
                    editOwnershipBinding.name.text.toString(),
                    "",
                    editOwnershipBinding.Note.text.toString(),
                    editOwnershipBinding.tags.text.toString(),
                    editOwnershipBinding.qr.text.toString())
            saveOwnershipButton(row, ownership.ownershipUID, editOwnershipRequest)
            popupDialog.dismiss()
        }
        popupDialog.show()
    }

    private fun plusButton(ownership: Ownership, quantityView: TextView) {
        lifecycleScope.launch {
            val response = api.ownershipQuantity("increment", 1, ownership.ownershipUID)
            if (response.success) {
                ownership.itemQuantity = response.ownership.itemQuantity
                quantityView.text = ownership.itemQuantity.toString()
            }
        }
    }

    private fun minusButton(ownership: Ownership, quantityView: TextView) {
        lifecycleScope.launch {
            val response = api.ownershipQuantity("decrement", 1, ownership.ownershipUID)
            if (response.success){
                ownership.itemQuantity = response.ownership.itemQuantity
                quantityView.text = ownership.itemQuantity.toString()
            }
        }
    }

    private fun saveOwnershipButton(row: TableRow, uid: String, editOwnershipRequest: OwnershipEditRequest) {
        lifecycleScope.launch {
            val response = api.ownershipEdit(editOwnershipRequest, uid)
            if (response.success){
                val ownershipView = (row.getChildAt(0) as LinearLayout).getChildAt(0) as TextView
                ownershipView.text = editOwnershipRequest.customItemName.substring(0 until 25.coerceAtMost(editOwnershipRequest.customItemName.length))
                OwnershipManager.setOwnershipName(uid, editOwnershipRequest.customItemName)
            }
        }
    }

    private fun saveLocationButton(row: TableRow, uid: String, editLocationRequest: LocationEditRequest) {
        lifecycleScope.launch {
            val response = api.locationEdit(editLocationRequest, uid)
            if (response.success){
                val locationView = row.getChildAt(0) as TextView
                locationView.text = editLocationRequest.locationName.substring(0 until 25.coerceAtMost(editLocationRequest.locationName.length))
                OwnershipManager.setOwnershipName(uid, editLocationRequest.locationName)
            }
        }
    }

    private fun locationOnClick(location: Location, layoutParams: TableRow.LayoutParams, row: TableRow) {
        codeScanner.stopPreview()

        val editLocationBinding: EditLocationBinding = EditLocationBinding.inflate(layoutInflater)
        val popupDialog = Dialog(this)
        popupDialog.setContentView(editLocationBinding.root)
        popupDialog.setOnDismissListener { codeScanner.startPreview() }

        val viewModel = ItemViewModel()
        viewModel.name = location.locationName
        viewModel.qr = location.locationQR
        viewModel.description = location.locationDescription
        viewModel.tags = location.locationTags
        editLocationBinding.viewModel = viewModel

        popupDialog.window?.setLayout(layoutParams.width, layoutParams.height)

        editLocationBinding.cancelButton.setOnClickListener{popupDialog.dismiss()}
        editLocationBinding.saveButton.setOnClickListener {
            val editLocationRequest =
                LocationEditRequest(
                    editLocationBinding.name.text.toString(),
                    editLocationBinding.Note.text.toString(),
                    editLocationBinding.tags.text.toString(),
                    editLocationBinding.qr.text.toString())
            saveLocationButton(row, location.locationUID, editLocationRequest)
            popupDialog.dismiss()
        }
        popupDialog.show() // TODO fix
    }


    private fun clearButton() {
        when (pageView) {
            "items" -> {
                val tableLayout = scannerBinding.itemsTableLayout
                tableLayout.removeAllViews()
                OwnershipManager.removeAllOwnerships()
                ownershipRowMap.clear()
            }
            "locations" -> {
                val tableLayout = scannerBinding.locationTableLayout
                tableLayout.removeAllViews()
                LocationManager.removeAllLocations()
                locationRowMap.clear()
            }
        }
    }

    private fun unpackButton() {
        for (location in LocationManager.getAllLocations()) {
            lifecycleScope.launch {
                val unpacked = api.locationUnpack(location.locationUID)
                unpackInventory(unpacked.inventory)
            }
        }
    }

    private fun unpackInventory(inventoryDTO: InventoryDTO){
        inventoryDTO.ownerships?.let { ownerships ->
            if (ownerships.isNotEmpty()) {
                for (ownership in ownerships) {
                    populateItem(ownership)
                }
            }
        }
        inventoryDTO.locations?.let { locations ->
            if (locations.isNotEmpty()) {
                for (location in locations) {
                    populateLocations(location.parent)
                    unpackInventory(location)
                }
            }
        }
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

        if(pageView == "items") {
            searchBinding.searchButton.setOnClickListener { searchOwnershipButton(searchBinding) }
        } else {
            searchBinding.searchButton.setOnClickListener { searchLocationButton(searchBinding) }

        }
        searchBinding.cancelButton.setOnClickListener{popupDialog.dismiss()}

        popupDialog.show()
    }

    private fun searchOwnershipButton(searchBinding: SearchBinding) {
        val name = searchBinding.nameSearchText.text.toString()
        val tags = searchBinding.tagsSearchText.text.toString()
        val tableLayout = searchBinding.searchTableLayout
        tableLayout.removeAllViews()
        searchRowMap.clear()
        if (name == "" && tags == ""){

            return
        }
        lifecycleScope.launch {
            val response = api.ownershipSearch(SearchRequest(name, tags))
            if (response.success) {
                for (ownership in response.ownership) {
                    val row = createRowForOwnershipSearch(ownership)
                    tableManager.setColorForRow(row, tableLayout.childCount)
                    tableLayout.addView(row)
                }
            }
        }
    }

    private fun searchLocationButton(searchBinding: SearchBinding) {
        val name = searchBinding.nameSearchText.text.toString()
        val tags = searchBinding.tagsSearchText.text.toString()
        val tableLayout = searchBinding.searchTableLayout
        tableLayout.removeAllViews()
        searchRowMap.clear()
        if (name == "" && tags == ""){
            return
        }
        lifecycleScope.launch {
            val response = api.locationSearch(SearchRequest(name, tags))
            if (response.success) {
                for (location in response.locations) {
                    val row = createRowForLocationSearch(location)
                    tableManager.setColorForRow(row, tableLayout.childCount)
                    tableLayout.addView(row)
                }
            }
        }
    }

    private fun createRowForOwnershipSearch(ownership: Ownership): TableRow {
        val name = ownership.customItemName

        var location = ownership.location.locationName
        if (ownership.borrower.borrowerName != "Default"){
            location = ownership.borrower.borrowerName
        }

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

        val locationLayout = LinearLayout(this)
        locationLayout.layoutParams = TableRow.LayoutParams(
            0, TableRow.LayoutParams.MATCH_PARENT, 0.33f)
        locationLayout.gravity = Gravity.END or Gravity.CENTER_VERTICAL

        val locationView = TextView(this)
        locationView.text = location.substring(0 until 25.coerceAtMost(location.length))
        locationView.gravity = Gravity.CENTER
        locationLayout.addView(locationView)

        val buttonLayoutParams = TableRow.LayoutParams()
        buttonLayoutParams.width = resources.getDimensionPixelSize(R.dimen.button_width)
        buttonLayoutParams.height = resources.getDimensionPixelSize(R.dimen.button_height)

        row.addView(nameLayout)
        row.addView(locationLayout)
        row.layoutParams = layoutParams
        searchRowMap[ownership.ownershipUID] = row

        row.setOnClickListener {
            Alerts().addConfirmation(ownership.customItemName, this) { shouldAdd ->
                if (shouldAdd){
                    if (!OwnershipManager.ownershipExists(ownership.ownershipUID)){
                        populateItem(ownership)}}
            }
        }

        return row
    }

    private fun createRowForLocationSearch(location: Location): TableRow {
        val name = location.locationName

        val parent = location.location?.locationName

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

        val locationLayout = LinearLayout(this)
        locationLayout.layoutParams = TableRow.LayoutParams(
            0, TableRow.LayoutParams.MATCH_PARENT, 0.33f)
        locationLayout.gravity = Gravity.END or Gravity.CENTER_VERTICAL

        val locationView = TextView(this)
        if (parent != null) {
            locationView.text = parent.substring(0 until 25.coerceAtMost(parent.length))
        }
        locationView.gravity = Gravity.CENTER
        locationLayout.addView(locationView)

        val buttonLayoutParams = TableRow.LayoutParams()
        buttonLayoutParams.width = resources.getDimensionPixelSize(R.dimen.button_width)
        buttonLayoutParams.height = resources.getDimensionPixelSize(R.dimen.button_height)

        row.addView(nameLayout)
        row.addView(locationLayout)
        row.layoutParams = layoutParams
        searchRowMap[location.locationUID] = row

        row.setOnClickListener {
            Alerts().addConfirmation(location.locationName, this) { shouldAdd ->
                if (shouldAdd){
                    if (!LocationManager.locationExists(location.locationUID)){
                        populateLocations(location)}}
            }
        }

        return row
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

        createNewBinding.createButton.setOnClickListener { createNewButton(createNewBinding, popupDialog) }
        createNewBinding.cancelButton.setOnClickListener { popupDialog.dismiss() }

        val spinnerPosition = if (pageView == "items") 0 else 1
        createNewBinding.typeSpinner.setSelection(spinnerPosition)

        popupDialog.show()
    }


    private fun createNewButton(createNewBinding: CreateNewBinding, popup: Dialog) {
        val typeSpinner = createNewBinding.typeSpinner
        val name = createNewBinding.nameEditText.text.toString()
        val qr = createNewBinding.qrCodeEditText.text.toString()
        if (name == "" || qr == ""){
            return
        }
        lifecycleScope.launch {
            when (typeSpinner.selectedItem?.toString() ?: "") {
                "Location" -> {
                    val response = api.locationCreate(name, qr)
                    if (response.success) {
                        Toast.makeText(this@Scanner, "Location created", Toast.LENGTH_SHORT).show()
                        populateLocations(response.location)
                        popup.dismiss()
                        switchToLocationsView()
                    }
                }
                "Item" -> {
                    val request = OwnershipCreateRequest(qr, name)
                    val response = api.ownershipCreate(request)
                    if (response.success) {
                        Toast.makeText(this@Scanner, "Ownership created", Toast.LENGTH_SHORT).show()
                        populateItem(response.ownership)
                        popup.dismiss()
                        switchToItemsView()
                    }
                }
            }
        }
    }

    private fun placeQueueButton() {
                if (locationRowMap.isNotEmpty() and ownershipRowMap.isNotEmpty()){
                    codeScanner.stopPreview()

                    val dialogBuilder = AlertDialog.Builder(this@Scanner)
                    dialogBuilder.setTitle("Place Queue in:")
                    val locations = LocationManager.getAllLocationNames()

                    dialogBuilder.setSingleChoiceItems(
                        ArrayAdapter(this@Scanner, android.R.layout.select_dialog_singlechoice, locations), -1)
                            { dialog, which ->
                            when (locations[which]) {
                                else -> {
                                    val selectedLocation = LocationManager.getAllLocations()[which]
                                    val qr = selectedLocation.locationQR

                                    for(ownership in OwnershipManager.getAllOwnerships()) {
                                        lifecycleScope.launch {
                                            val response = api.ownershipSetLocation(ownership.ownershipUID, qr)
                                            if (response.success){
                                                updateLocationForAllRows(LocationManager.getAllLocations()[which])
                                            } else{
                                                // TODO handle negative
                                            }
                                        }
                                    }
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

    private fun updateLocationForAllRows(location: Location) {
        ownershipRowMap.entries.forEach { (ownershipUID, row) ->
            val locationView = (row.getChildAt(1) as LinearLayout).getChildAt(0) as TextView
            locationView.text = location.locationName.substring(0 until 25.coerceAtMost(location.locationName.length))
            OwnershipManager.setOwnershipLocation(ownershipUID, location)
        }
    }

    private fun removeOwnershipRow(uid: String) {
        val tableLayout = scannerBinding.itemsTableLayout
        val rowToRemove = ownershipRowMap[uid]
        rowToRemove?.let {
            tableLayout.removeView(it)
            ownershipRowMap.remove(uid)
            OwnershipManager.removeOwnership(uid)
        }
        for (i in 0 until tableLayout.childCount) {
            val row = tableLayout.getChildAt(i) as TableRow
            tableManager.setColorForRow(row, i)
        }
    }

    private fun removeLocationRow(uid: String) {
        val tableLayout = scannerBinding.locationTableLayout
        val rowToRemove = locationRowMap[uid]
        rowToRemove?.let {
            tableLayout.removeView(it)
            locationRowMap.remove(uid)
            LocationManager.removeLocation(uid)
        }
        for (i in 0 until tableLayout.childCount) {
            val row = tableLayout.getChildAt(i) as TableRow
            tableManager.setColorForRow(row, i)
        }
    }

    private fun populateItems(postScanResponse: ScannerBarcodeResponse) {
        runOnUiThread {
            for (ownership in postScanResponse.ownership) {
                if (!ownershipRowMap.containsKey(ownership.ownershipUID)) {
                    populateItem(ownership)
                }
            }
        }
        coroutineScope.launch {delay(1000)
            codeScanner.startPreview()
        }
    }

    private fun populateItem(ownership: Ownership) {
        val tableLayout = scannerBinding.itemsTableLayout

        OwnershipManager.addOwnership(ownership)
        val row = createRowForOwnership(ownership)
        tableManager.setColorForRow(row, tableLayout.childCount)
        tableLayout.addView(row)
    }

    private fun populateLocations(location: Location){
        val tableLayout = scannerBinding.locationTableLayout

        if(!locationRowMap.containsKey(location.locationUID)) {
            LocationManager.addLocation(location)
            val row = createRowForLocation(location)
            tableManager.setColorForRow(row, tableLayout.childCount)
            tableLayout.addView(row)
        }
        coroutineScope.launch {delay(1000)
            codeScanner.startPreview()
        }
    }

    override suspend fun scanSuccess(code: String, barcodeFormat: BarcodeFormat){
        codeScanner.stopPreview()
        if(barcodeFormat != BarcodeFormat.QR_CODE){
            val response = api.scannerBarcode(code)
            if (response.message == "429"){Toast.makeText(this@Scanner, "LIMIT REACHED", Toast.LENGTH_SHORT).show()}
            populateItems(response)
            switchToItemsView()
        } else {
            val response = api.scannerCheckQR(code)
            when (response.message) {
                "NEW" -> {
                    newEntry(code)
                }
                "LOCATION" -> {
                    val locationResponse = api.scannerQRLocation(code)
                    populateLocations(locationResponse.location)
                    switchToLocationsView()
                }
                "OWNERSHIP" -> {
                    val ownershipResponse = api.scanQROwnership(code)
                    populateItem(ownershipResponse.ownership)
                    switchToItemsView()
                    codeScanner.startPreview()
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

    private fun createRowForOwnership(ownership: Ownership): TableRow {
        val name = if (ownership.customItemName == "") ownership.item.itemName else ownership.customItemName
        val location = if (ownership.borrower.borrowerName != "Default") ownership.borrower.borrowerName else ownership.location.locationName
        val quantity = ownership.itemQuantity

        val layoutParams = TableRow.LayoutParams(
            TableRow.LayoutParams.MATCH_PARENT,
            TableRow.LayoutParams.WRAP_CONTENT
        )

        val row = TableRow(this).apply {
            this.layoutParams = layoutParams
            setOnClickListener { ownershipOnClick(ownership, layoutParams, this) }
            setOnLongClickListener {
                Alerts().removeConfirmation(ownership.customItemName, this@Scanner) { shouldDelete ->
                    if (shouldDelete) removeOwnershipRow(ownership.ownershipUID)
                }
                true
            }
        }

        val nameLayout = createLinearLayout(0.34f, Gravity.START or Gravity.CENTER_VERTICAL)
        val nameView = createTextView(name.substring(0 until 25.coerceAtMost(name.length)))
        nameLayout.addView(nameView)

        val locationLayout = createLinearLayout(0.33f, Gravity.CENTER or Gravity.CENTER_VERTICAL)
        val locationView = createTextView(location.substring(0 until 25.coerceAtMost(location.length)))
        locationView.gravity = Gravity.CENTER
        locationLayout.addView(locationView)

        val quantityLayout = createLinearLayout(0.33f, Gravity.END or Gravity.CENTER_VERTICAL)
        val quantityView = createTextView(quantity.toString())
        quantityView.gravity = Gravity.END
        val buttonLayoutParams = TableRow.LayoutParams(
            resources.getDimensionPixelSize(R.dimen.button_width),
            resources.getDimensionPixelSize(R.dimen.button_height)
        )
        val plusButton = createButton("+", buttonLayoutParams) { plusButton(ownership, quantityView) }
        val minusButton = createButton("-", buttonLayoutParams) { minusButton(ownership, quantityView) }

        quantityLayout.addView(minusButton)
        quantityLayout.addView(quantityView)
        quantityLayout.addView(plusButton)

        row.addView(nameLayout)
        row.addView(locationLayout)
        row.addView(quantityLayout)
        ownershipRowMap[ownership.ownershipUID] = row

        return row
    }

    private fun createRowForLocation(location: Location): TableRow {
        val name = location.locationName
        val parent = location.location?.locationName
        val row = TableRow(this)
        val layoutParams = TableRow.LayoutParams(
            TableRow.LayoutParams.MATCH_PARENT,
            TableRow.LayoutParams.WRAP_CONTENT
        )

        val nameView = createTextView(name.substring(0 until 20.coerceAtMost(name.length)))
        nameView.layoutParams = TableRow.LayoutParams(
            0, TableRow.LayoutParams.WRAP_CONTENT, 1f
        )
        row.addView(nameView)

        val locationView = createTextView(
            parent?.substring(0 until 18.coerceAtMost(parent.length)) ?: getString(R.string.no_location)
        )
        locationView.layoutParams = TableRow.LayoutParams(
            0, TableRow.LayoutParams.WRAP_CONTENT, 1f
        )
        locationView.gravity = Gravity.END
        row.addView(locationView)

        row.layoutParams = layoutParams
        locationRowMap[location.locationUID] = row

        row.setOnLongClickListener {
            Alerts().removeConfirmation(location.locationName, this) { shouldDelete ->
                if (shouldDelete) removeLocationRow(location.locationUID)
            }
            true
        }

        row.setOnClickListener { locationOnClick(location, layoutParams, row) }

        return row
    }

}

