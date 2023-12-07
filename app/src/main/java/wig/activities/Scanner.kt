package wig.activities

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import wig.api.dto.ScanResponse
import wig.utils.StoreToken
import com.google.zxing.BarcodeFormat
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import wig.R
import wig.activities.bases.BaseCamera
import wig.api.dto.LocationResponse
import wig.databinding.CreateNewBinding
import wig.models.Location
import wig.models.Ownership
import wig.utils.BinManager
import wig.utils.OwnershipManager

class Scanner : BaseCamera() {
    private var pageView = "items"
    private val ownershipRowMap = mutableMapOf<Int, TableRow>()
    private val locationRowMap = mutableMapOf<Int, TableRow>()

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
        scannerBinding.binsButton.setOnClickListener{ switchToBinsView() }
        scannerBinding.itemsButton.setOnClickListener{ switchToItemsView() }
        scannerBinding.icSettings.setOnClickListener{ logout() }
        scannerBinding.clear.setOnClickListener { clearButton() }
        scannerBinding.placeQueue.setOnClickListener { placeQueueButton() }
        scannerBinding.add.setOnClickListener { newEntry() }
    }

    private fun createRowForOwnership(ownership: Ownership): TableRow {
        val name = ownership.item.itemName
        val location = ownership.location.locationName
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
            deleteConfirmation(ownership.item.itemName) { shouldDelete ->
                if (shouldDelete){ removeBinRow(ownership.ownershipUID)}
            }
            true
        }

        return row
    }

    private fun createRowForLocation(location: Location): TableRow {
        val name = location.locationName
        val parent = location.location?.locationName
        val type = location.locationType

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
        locationView.gravity = Gravity.CENTER
        row.addView(locationView)

        val typeView = TextView(this)
        typeView.text = type.substring(0 until 10.coerceAtMost(type.length))
        typeView.layoutParams = TableRow.LayoutParams(
            0, TableRow.LayoutParams.WRAP_CONTENT, 1f)
        typeView.gravity = Gravity.END
        row.addView(typeView)

        row.layoutParams = layoutParams
        locationRowMap[location.locationUID] = row

        row.setOnLongClickListener {
            deleteConfirmation(location.locationName) { shouldDelete ->
                if (shouldDelete){ removeBinRow(location.locationUID)}
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
            "bins" -> {
                val tableLayout = scannerBinding.binsTableLayout
                tableLayout.removeAllViews()
                BinManager.removeAllBins()
                locationRowMap.clear()
            }
            "shelves" -> {
                // TODO add shelves or remove shelves view
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
        popupDialog.show()
    }

    private fun createNewButton(createNewBinding: CreateNewBinding, popup: Dialog) {
        val typeSpinner = createNewBinding.typeSpinner
        val name = createNewBinding.nameEditText.text.toString()
        val locationQr = createNewBinding.qrCodeEditText.text.toString()
        lifecycleScope.launch {
            when (typeSpinner.selectedItem?.toString() ?: "") {
                "Bin" -> {
                    val response = createNewLocation("bin", name, locationQr)
                    if (response.success) {
                        Toast.makeText(this@Scanner, "Bin created", Toast.LENGTH_SHORT).show()
                        populateBins(response)
                        popup.dismiss()
                        switchToBinsView()
                    }
                }
                "Bag" -> {
                    val response = createNewLocation("bag", name, locationQr)
                    if (response.success){
                        Toast.makeText(this@Scanner, "Bag created", Toast.LENGTH_SHORT).show()
                        populateBins(response)
                        popup.dismiss()
                        switchToBinsView()
                    }
                }
                "Area" -> {
                    val response = createNewLocation("area", name, locationQr)
                    if (response.success){
                        Toast.makeText(this@Scanner, "Area created", Toast.LENGTH_SHORT).show()
                        populateBins(response)
                        popup.dismiss()
                        switchToBinsView()
                    }
                }
                "Item" -> {

                }
                else -> {

                }
            }
        }
    }

    private fun placeQueueButton() {
        when (pageView) {
            "items" -> {
                if (locationRowMap.size == 1){
                    val location = BinManager.getAllBins()[0]
                    for(ownership in OwnershipManager.getAllOwnerships()) {
                        lifecycleScope.launch {
                            val response = setItemLocation(ownership.ownershipUID, location.locationQR)
                            if (response.success){
                                updateLocationForAllRows(location)
                            } else{
                                // TODO handle negative
                            }
                        }
                    }

                } else{
                    // TODO function if more locations
                }
            }
            "bins" -> {
                // TODO add bins functionality
            }
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

    private fun removeBinRow(uid: Int) {
        val tableLayout = scannerBinding.binsTableLayout
        val rowToRemove = locationRowMap[uid]
        rowToRemove?.let {
            tableLayout.removeView(it)
            locationRowMap.remove(uid)
            BinManager.removeBin(uid)
        }
        for (i in 0 until tableLayout.childCount) {
            val row = tableLayout.getChildAt(i) as TableRow
            setColorForRow(row, i)
        }
    }

    private fun populateItems(postScanResponse: ScanResponse) {
        runOnUiThread {
            val tableLayout = scannerBinding.itemsTableLayout

            for (ownership in postScanResponse.ownership) {
                if (!ownershipRowMap.containsKey(ownership.ownershipUID)) {
                    OwnershipManager.addOwnership(ownership)
                    val row = createRowForOwnership(ownership)
                    setColorForRow(row, tableLayout.childCount)
                    tableLayout.addView(row)
                }
            }
        }
        coroutineScope.launch {delay(1000)
            codeScanner.startPreview()
        }
    }

    private fun populateBins(locationResponse: LocationResponse){
        val tableLayout = scannerBinding.binsTableLayout
        val bin = locationResponse.location

        if(!locationRowMap.containsKey(bin.locationUID)) {
            BinManager.addBin(bin)
            val row = createRowForLocation(bin)
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
                    populateBins(locationResponse)
                    switchToBinsView()
                }
                "ITEM" -> {
                    // TODO add item call here
                    codeScanner.startPreview()
                }
            }
        }
    }

    private fun switchToBinsView() {
        scannerBinding.tableItemsTitles.visibility = View.INVISIBLE
        scannerBinding.itemsTable.visibility = View.INVISIBLE
        scannerBinding.tableBinsTitles.visibility = View.VISIBLE
        scannerBinding.binsTable.visibility = View.VISIBLE
        pageView = "bins"
    }

    private fun switchToItemsView() {
        scannerBinding.tableBinsTitles.visibility = View.INVISIBLE
        scannerBinding.binsTable.visibility = View.INVISIBLE
        scannerBinding.tableItemsTitles.visibility = View.VISIBLE
        scannerBinding.itemsTable.visibility = View.VISIBLE
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

