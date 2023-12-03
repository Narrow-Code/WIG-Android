package wig.activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import wig.api.dto.ScanResponse
import wig.utils.StoreToken
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.google.zxing.BarcodeFormat
import kotlinx.coroutines.launch
import wig.R
import wig.api.dto.LocationResponse
import wig.api.dto.OwnershipResponse
import wig.databinding.CreateNewBinding
import wig.models.Location
import wig.models.Ownership
import wig.utils.BinManager
import wig.utils.OwnershipManager

private const val CAMERA_REQUEST_CODE = 101

@Suppress("DEPRECATION")
class Scanner : BaseActivity() {
    private lateinit var codeScanner: CodeScanner
    private var pageView = "items"
    private val handler = Handler()
    private val ownershipRowMap = mutableMapOf<Int, TableRow>()
    private val binsRowMap = mutableMapOf<Int, TableRow>()

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
        scannerBinding.shelvesButton.setOnClickListener { switchToShelvesView() }
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

        val nameView = TextView(this)
        nameView.text = name.substring(0 until 20.coerceAtMost(name.length))
        nameView.layoutParams = TableRow.LayoutParams(
            0, TableRow.LayoutParams.MATCH_PARENT, 1f)

        val locationView = TextView(this)
        locationView.text = location.substring(0 until 18.coerceAtMost(location.length))
        locationView.layoutParams = TableRow.LayoutParams(
            0, TableRow.LayoutParams.MATCH_PARENT, 1f)
        locationView.gravity = Gravity.CENTER

        val quantityView = TextView(this)
        quantityView.text = quantity.toString()
        quantityView.layoutParams = TableRow.LayoutParams(
            0, TableRow.LayoutParams.WRAP_CONTENT, 1f)
        quantityView.gravity = Gravity.END

        val buttonLayoutParams = TableRow.LayoutParams()
        buttonLayoutParams.width = resources.getDimensionPixelSize(R.dimen.button_width)
        buttonLayoutParams.height = resources.getDimensionPixelSize(R.dimen.button_height)

        val plusButton = Button(this)
        plusButton.text = "+"
        plusButton.layoutParams = buttonLayoutParams
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
        plusButton.gravity = Gravity.CENTER_VERTICAL
        minusButton.setOnClickListener {
            lifecycleScope.launch {
                val response = changeQuantity("decrement", 1, ownership.ownershipUID)
                if (response.success){
                    ownership.itemQuantity = response.ownership.itemQuantity
                    quantityView.text = ownership.itemQuantity.toString()
                }
            }
        }

        row.addView(nameView)
        row.addView(locationView)
        row.addView(quantityView)
        row.addView(minusButton)
        row.addView(plusButton)
        row.layoutParams = layoutParams
        ownershipRowMap[ownership.ownershipUID] = row

        // TODO set onclick listener to row

        return row
    }

    private fun createRowForBin(bin: Location): TableRow {
        val name = bin.locationName
        val location = bin.location?.locationName
        val type = bin.locationType

        val row = TableRow(this)
        val layoutParams = TableRow.LayoutParams(
            TableRow.LayoutParams.MATCH_PARENT,
            TableRow.LayoutParams.WRAP_CONTENT)

        val nameView = TextView(this)
        nameView.text = name.substring(0 until 20.coerceAtMost(name.length))
        nameView.layoutParams = TableRow.LayoutParams(
            0, TableRow.LayoutParams.WRAP_CONTENT, 1f)
        row.addView(nameView)

        if (location != null){
            val locationView = TextView(this)
            locationView.text = location.substring(0 until 18.coerceAtMost(location.length))
            locationView.layoutParams = TableRow.LayoutParams(
                0, TableRow.LayoutParams.WRAP_CONTENT, 1f)
            locationView.gravity = Gravity.CENTER
            row.addView(locationView)
        }

        val typeView = TextView(this)
        typeView.text = type.substring(0 until 10.coerceAtMost(type.length))
        typeView.layoutParams = TableRow.LayoutParams(
            0, TableRow.LayoutParams.WRAP_CONTENT, 1f)
        typeView.gravity = Gravity.END
        row.addView(typeView)

        row.layoutParams = layoutParams
        binsRowMap[bin.locationUID] = row

        // TODO set onclick listener to row

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
                binsRowMap.clear()
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
                    if (response.success){
                        Toast.makeText(this@Scanner, "Bin created", Toast.LENGTH_SHORT).show()
                        popup.dismiss()
                    }
                }
                "Bag" -> {
                    val response = createNewLocation("bag", name, locationQr)
                    if (response.success){
                        Toast.makeText(this@Scanner, "Bag created", Toast.LENGTH_SHORT).show()
                        popup.dismiss()
                    }
                }
                "Area" -> {
                    val response = createNewLocation("area", name, locationQr)
                    if (response.success){
                        Toast.makeText(this@Scanner, "Area created", Toast.LENGTH_SHORT).show()
                        popup.dismiss()
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
                if (binsRowMap.size == 1){
                    val binQR = BinManager.getAllBins()[0].locationQR
                    for(ownership in OwnershipManager.getAllOwnerships()) {
                        lifecycleScope.launch {
                            val response = setItemLocation(ownership.ownershipUID, binQR)
                            if (response.success){
                                clearButton()
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
            "shelves" -> {
                // TODO add shelves or remove shelves view
            }
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
    }

    private fun removeBinRow(uid: Int) {
        val tableLayout = scannerBinding.binsTableLayout
        val rowToRemove = binsRowMap[uid]
        rowToRemove?.let {
            tableLayout.removeView(it)
            binsRowMap.remove(uid)
            BinManager.removeBin(uid)
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

        handler.postDelayed({
            codeScanner.startPreview()
        }, 1000)
    }

    private fun populateBins(locationResponse: LocationResponse){
        val tableLayout = scannerBinding.binsTableLayout
        val bin = locationResponse.location

        if(!binsRowMap.containsKey(bin.locationUID)) {
            BinManager.addBin(bin)
            val row = createRowForBin(bin)
            setColorForRow(row, tableLayout.childCount)
            tableLayout.addView(row)
        }

        handler.postDelayed({
            codeScanner.startPreview()
        }, 1000)
    }

    private fun setColorForRow(row: TableRow, position: Int){
        val backgroundColor = if(position % 2 == 0){
            Color.BLACK
        } else {
            Color.DKGRAY
        }
        row.setBackgroundColor(backgroundColor)
    }

    private fun switchToBinsView() {
        scannerBinding.tableItemsTitles.visibility = View.INVISIBLE
        scannerBinding.tableShelvesTitles.visibility = View.INVISIBLE
        scannerBinding.itemsTable.visibility = View.INVISIBLE
        scannerBinding.shelvesTable.visibility = View.INVISIBLE
        scannerBinding.tableBinsTitles.visibility = View.VISIBLE
        scannerBinding.binsTable.visibility = View.VISIBLE
        pageView = "bins"
    }

    private fun switchToItemsView() {
        scannerBinding.tableBinsTitles.visibility = View.INVISIBLE
        scannerBinding.tableShelvesTitles.visibility = View.INVISIBLE
        scannerBinding.binsTable.visibility = View.INVISIBLE
        scannerBinding.shelvesTable.visibility = View.INVISIBLE
        scannerBinding.tableItemsTitles.visibility = View.VISIBLE
        scannerBinding.itemsTable.visibility = View.VISIBLE
        pageView = "items"
    }

    private fun switchToShelvesView() {
        scannerBinding.tableItemsTitles.visibility = View.INVISIBLE
        scannerBinding.tableBinsTitles.visibility = View.INVISIBLE
        scannerBinding.binsTable.visibility = View.INVISIBLE
        scannerBinding.itemsTable.visibility = View.INVISIBLE
        scannerBinding.tableShelvesTitles.visibility = View.VISIBLE
        scannerBinding.shelvesTable.visibility = View.VISIBLE
        pageView = "shelves"
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

    private fun codeScanner() {
        codeScanner = CodeScanner(this, scannerBinding.scannerView)

        codeScanner.apply {
            camera = CodeScanner.CAMERA_BACK
            formats = listOf(BarcodeFormat.UPC_A, BarcodeFormat.UPC_E, BarcodeFormat.EAN_13, BarcodeFormat.EAN_8, BarcodeFormat.QR_CODE)
            autoFocusMode = AutoFocusMode.SAFE
            scanMode = ScanMode.SINGLE
            isAutoFocusEnabled = true
            isFlashEnabled = false
            decodeCallback = DecodeCallback {
                runOnUiThread {
                    codeScanner.stopPreview()
                    if(it.barcodeFormat != BarcodeFormat.QR_CODE){
                        lifecycleScope.launch {
                            val response = scanBarcode(it.text)
                            populateItems(response)
                        }
                    } else {
                        lifecycleScope.launch {
                            val response = checkQR(it.text)
                            when (response.message) {
                                "NEW" -> {
                                    newEntry(it.text)
                                }
                                "LOCATION" -> {
                                    val locationResponse = scanQRLocation(it.text)
                                    populateBins(locationResponse)
                                }
                                "ITEM" -> {
                                    // TODO add item call here
                                    codeScanner.startPreview()
                                }
                            }
                        }
                    }
                }
            }
            errorCallback = ErrorCallback { runOnUiThread {} }
        }
        scannerBinding.scannerView.setOnClickListener {
            codeScanner.startPreview()
        }
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    private fun setupPermissions() {
        val permission : Int = ContextCompat.checkSelfPermission(this,
            Manifest.permission.CAMERA)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_REQUEST_CODE
            )
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray) {
        when (requestCode) {
            CAMERA_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "You need the camera permission to be able to use this app!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

