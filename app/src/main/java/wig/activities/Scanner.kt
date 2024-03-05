package wig.activities

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import wig.api.dto.ScanResponse
import wig.utils.StoreToken
import com.google.zxing.BarcodeFormat
import com.supersuman.githubapkupdater.Updater
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import wig.R
import wig.activities.bases.BaseCamera
import wig.api.dto.CheckoutRequest
import wig.api.dto.NewOwnershipRequest
import wig.databinding.CreateNewBinding
import wig.models.Location
import wig.models.Ownership
import wig.utils.LocationManager
import wig.utils.OwnershipManager

class Scanner : BaseCamera() {
    private var pageView = "items"
    private val ownershipRowMap = mutableMapOf<Int, TableRow>()
    private val locationRowMap = mutableMapOf<Int, TableRow>()
    private val updater = Updater(this, "https://github.com/WIGteam/WIG-Android")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        disableBackPress()
        setScreenOrientation()
        setScannerBindings()
        setupPermissions()
        codeScanner()
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        scannerBinding.locationsButton.setOnClickListener{ switchToLocationsView() }
        scannerBinding.itemsButton.setOnClickListener{ switchToItemsView() }
        scannerBinding.icSettings.setOnClickListener{ logout() }
        scannerBinding.clear.setOnClickListener { clearButton() }
        scannerBinding.place.setOnClickListener { placeQueueButton() }
        scannerBinding.add.setOnClickListener { newEntry() }
        scannerBinding.unpack.setOnClickListener {unpackButton()}
        scannerBinding.checkOut.setOnClickListener {checkoutButton()}
    }

    private fun checkoutButton() {
        codeScanner.stopPreview()
        lifecycleScope.launch {
                val borrowers = getBorrowers()

                var borrowerNames = borrowers.borrowers.map { it.borrowerName }.toTypedArray()
                borrowerNames = arrayOf("Self") + borrowerNames + "New"

                val dialogBuilder = AlertDialog.Builder(this@Scanner)
                dialogBuilder.setTitle("Checkout to:")

                dialogBuilder.setSingleChoiceItems(
                    ArrayAdapter(this@Scanner, android.R.layout.select_dialog_singlechoice, borrowerNames),
                    -1
                ) { dialog, which ->
                    var borrowerNum = 1
                    when (borrowerNames[which]) {
                        "New" -> {
                            showNewBorrowerDialog(this@Scanner) { borrowerName ->
                                lifecycleScope.launch {
                                    val response = createBorrowers(borrowerName)
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
                        "Self" -> {
                            borrowerNum = 2
                        }
                        else -> {
                            val selectedBorrower = borrowers.borrowers[which - 1]
                            borrowerNum = selectedBorrower.borrowerUID
                        }
                    }
                    val ownerships: MutableList<Int> = mutableListOf()
                    for (ownership in OwnershipManager.getAllOwnerships()){
                        ownerships.add(ownership.ownershipUID)
                    }
                    val request = CheckoutRequest(ownerships)
                    lifecycleScope.launch {
                        val response = checkout(borrowerNum, request)
                        if (response.success){
                            for (ownershipSuccess in response.ownerships){
                                ownershipRowMap.entries.forEach { (ownershipUID, row) ->
                                    if (ownershipUID == ownershipSuccess) {
                                        val locationView = (row.getChildAt(1) as LinearLayout).getChildAt(0) as TextView
                                        locationView.text = borrowerNames[which]
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
                    // Dismiss the dialog
                    dialog.dismiss()
                    codeScanner.startPreview()
                }

                // Create and show the AlertDialog
                val dialog = dialogBuilder.create()
                dialog.show()
        }
    }

    private fun showNewBorrowerDialog(context: Context, onPositiveButtonClick: (String) -> Unit) {
        val alertDialog = AlertDialog.Builder(context)
        alertDialog.setTitle("New Borrower")

        val inputEditText = EditText(context)
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        inputEditText.layoutParams = layoutParams
        alertDialog.setView(inputEditText)

        alertDialog.setPositiveButton("Create") { _, _ ->
            val borrowerName = inputEditText.text.toString()
            onPositiveButtonClick.invoke(borrowerName)
        }

        alertDialog.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }

        alertDialog.show()
    }

    private fun createRowForOwnership(ownership: Ownership): TableRow {
        var name = ownership.customItemName
        if (ownership.customItemName != ""){
            name = ownership.customItemName + " Checkout"
        }
        var location = ownership.location.locationName
        if (ownership.itemBorrower != 1){
            location = ownership.borrower.borrowerName
        }

        val quantity = ownership.itemQuantity
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
        locationLayout.gravity = Gravity.CENTER or Gravity.CENTER_VERTICAL

        val locationView = TextView(this)
        locationView.text = location.substring(0 until 25.coerceAtMost(location.length))
        locationView.gravity = Gravity.CENTER
        locationLayout.addView(locationView)

        val quantityLayout = LinearLayout(this)
        quantityLayout.layoutParams = TableRow.LayoutParams(
            0, TableRow.LayoutParams.MATCH_PARENT, 0.33f)
        quantityLayout.gravity = Gravity.END or Gravity.CENTER_VERTICAL

        val quantityView = TextView(this)
        quantityView.text = quantity.toString()
        quantityView.gravity = Gravity.END

        val buttonLayoutParams = TableRow.LayoutParams()
        buttonLayoutParams.width = resources.getDimensionPixelSize(R.dimen.button_width)
        buttonLayoutParams.height = resources.getDimensionPixelSize(R.dimen.button_height)

        val plusButton = Button(this)
        plusButton.text = "+"
        plusButton.layoutParams = buttonLayoutParams
        plusButton.gravity = Gravity.END
        plusButton.setOnClickListener {
            lifecycleScope.launch {
                val response = changeQuantity("increment", 1, ownership.ownershipUID)
                if (response.success){
                    ownership.itemQuantity = response.ownership.itemQuantity
                    quantityView.text = ownership.itemQuantity.toString()
                }
            }
        }

        val minusButton = Button(this)
        minusButton.text = "-"
        minusButton.layoutParams = buttonLayoutParams
        minusButton.gravity = Gravity.END
        minusButton.setOnClickListener {
            lifecycleScope.launch {
                val response = changeQuantity("decrement", 1, ownership.ownershipUID)
                if (response.success){
                    ownership.itemQuantity = response.ownership.itemQuantity
                    quantityView.text = ownership.itemQuantity.toString()
                }
            }
        }

        row.addView(nameLayout)
        row.addView(locationLayout)
        row.addView(quantityLayout)
        quantityLayout.addView(minusButton)
        quantityLayout.addView(quantityView)
        quantityLayout.addView(plusButton)
        row.layoutParams = layoutParams
        ownershipRowMap[ownership.ownershipUID] = row

        row.setOnLongClickListener {
            removeConfirmation(ownership.customItemName) { shouldDelete ->
                if (shouldDelete){ removeOwnershipRow(ownership.ownershipUID)}
            }
            true
        }

        return row
    }

    private fun createRowForLocation(location: Location): TableRow {
        val name = location.locationName
        val parent = location.location?.locationName

        val row = TableRow(this)
        val layoutParams = TableRow.LayoutParams(
            TableRow.LayoutParams.MATCH_PARENT,
            TableRow.LayoutParams.WRAP_CONTENT)

        val nameView = TextView(this)
        nameView.text = name.substring(0 until 20.coerceAtMost(name.length))
        nameView.layoutParams = TableRow.LayoutParams(
            0, TableRow.LayoutParams.WRAP_CONTENT, 1f)
        row.addView(nameView)

        val locationView = TextView(this)
        if (parent != null) {
            locationView.text = parent.substring(0 until 18.coerceAtMost(parent.length))
        } else {
            locationView.text = getString(R.string.no_location)
        }
        locationView.layoutParams = TableRow.LayoutParams(
            0, TableRow.LayoutParams.WRAP_CONTENT, 1f)
        locationView.gravity = Gravity.END
        row.addView(locationView)

        row.layoutParams = layoutParams
        locationRowMap[location.locationUID] = row

        row.setOnLongClickListener {
            removeConfirmation(location.locationName) { shouldDelete ->
                if (shouldDelete){ removeLocationRow(location.locationUID)}
            }
            true
        }

        return row
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
        for(location in LocationManager.getAllLocations()) {
            lifecycleScope.launch {
                val unpacked = unpackLocation(location.locationUID)
                for(ownership in unpacked.ownerships){
                    populateItem(ownership)
                }
                for(loc in unpacked.locations){
                    populateLocations(loc)
                }
            }
        }
    }

    private fun newEntry(qr: String) {
        codeScanner.stopPreview()

        val createNewBinding: CreateNewBinding = CreateNewBinding.inflate(layoutInflater)
        val popupDialog = Dialog(this)
        popupDialog.setContentView(createNewBinding.root)
        createNewBinding.qrCodeEditText.setText(qr)
        popupDialog.setOnDismissListener { codeScanner.startPreview() }

        val layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        popupDialog.window?.setLayout(layoutParams.width, layoutParams.height)

        createNewBinding.createButton.setOnClickListener{ createNewButton(createNewBinding, popupDialog) }
        createNewBinding.cancelButton.setOnClickListener{popupDialog.dismiss()
        }

        val spinnerPosition = if (pageView == "items") 0 else 1
        createNewBinding.typeSpinner.setSelection(spinnerPosition)

        popupDialog.show()
    }

    private fun newEntry(){
        codeScanner.stopPreview()

        val createNewBinding: CreateNewBinding = CreateNewBinding.inflate(layoutInflater)
        val popupDialog = Dialog(this)
        popupDialog.setContentView(createNewBinding.root)
        popupDialog.setOnDismissListener { codeScanner.startPreview() }

        val layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        popupDialog.window?.setLayout(layoutParams.width, layoutParams.height)
        createNewBinding.createButton.setOnClickListener{ createNewButton(createNewBinding, popupDialog) }
        createNewBinding.cancelButton.setOnClickListener{popupDialog.dismiss()
        }

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
                    val response = createNewLocation(name, qr)
                    if (response.success) {
                        Toast.makeText(this@Scanner, "Location created", Toast.LENGTH_SHORT).show()
                        populateLocations(response.location)
                        popup.dismiss()
                        switchToLocationsView()
                    }
                }
                "Item" -> {
                    val request = NewOwnershipRequest(qr, name)
                    val response = createNewOwnershipNoItem(request)
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
                                            val response = setItemLocation(ownership.ownershipUID, qr)
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

    private fun removeOwnershipRow(uid: Int) {
        val tableLayout = scannerBinding.itemsTableLayout
        val rowToRemove = ownershipRowMap[uid]
        rowToRemove?.let {
            tableLayout.removeView(it)
            ownershipRowMap.remove(uid)
            OwnershipManager.removeOwnership(uid)
        }
        for (i in 0 until tableLayout.childCount) {
            val row = tableLayout.getChildAt(i) as TableRow
            setColorForRow(row, i)
        }
    }

    private fun removeLocationRow(uid: Int) {
        val tableLayout = scannerBinding.locationTableLayout
        val rowToRemove = locationRowMap[uid]
        rowToRemove?.let {
            tableLayout.removeView(it)
            locationRowMap.remove(uid)
            LocationManager.removeLocation(uid)
        }
        for (i in 0 until tableLayout.childCount) {
            val row = tableLayout.getChildAt(i) as TableRow
            setColorForRow(row, i)
        }
    }

    private fun populateItems(postScanResponse: ScanResponse) {
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
        setColorForRow(row, tableLayout.childCount)
        tableLayout.addView(row)
    }

    private fun populateLocations(location: Location){
        val tableLayout = scannerBinding.locationTableLayout

        if(!locationRowMap.containsKey(location.locationUID)) {
            LocationManager.addLocation(location)
            val row = createRowForLocation(location)
            setColorForRow(row, tableLayout.childCount)
            tableLayout.addView(row)
        }
        coroutineScope.launch {delay(1000)
            codeScanner.startPreview()
        }
    }

    private fun setColorForRow(row: TableRow, position: Int){
        val backgroundColor = if(position % 2 == 0){
            Color.BLACK
        } else {
            Color.DKGRAY
        }
        row.setBackgroundColor(backgroundColor)
    }

    override suspend fun scanSuccess(code: String, barcodeFormat: BarcodeFormat){
        codeScanner.stopPreview()
        if(barcodeFormat != BarcodeFormat.QR_CODE){
            val response = scanBarcode(code)
            if (response.message == "429"){Toast.makeText(this@Scanner, "LIMIT REACHED", Toast.LENGTH_SHORT).show()}
            populateItems(response)
            switchToItemsView()
        } else {
            val response = checkQR(code)
            when (response.message) {
                "NEW" -> {
                    newEntry(code)
                }
                "LOCATION" -> {
                    val locationResponse = scanQRLocation(code)
                    populateLocations(locationResponse.location)
                    switchToLocationsView()
                }
                "ITEM" -> {
                    // TODO add item call here
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

    // TODO REMOVE THIS IS FOR TESTING LOG IN AND OUT UNTIL SETTINGS PAGE IS ADDED
    private fun logout() {
        // Delete token & UID
        lifecycleScope.launch {
            val storeToken = StoreToken(this@Scanner)
            storeToken.saveToken("")
        }
        startActivityLogin()
    }

}

