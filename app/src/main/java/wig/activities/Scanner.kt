package wig.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import android.view.View
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import wig.api.ScannerService
import wig.api.dto.ScanResponse
import wig.utils.StoreToken
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.google.zxing.BarcodeFormat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import wig.models.Ownership
import wig.utils.OwnershipManager

private const val CAMERA_REQUEST_CODE = 101

@Suppress("DEPRECATION")
class Scanner : BaseActivity() {
    private lateinit var codeScanner: CodeScanner
    private var pageView = "items"
    private val handler = Handler()
    private val service = ScannerService.create()
    private val ownershipRowMap = mutableMapOf<Int, TableRow>()

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
    }

    private suspend fun scanBarcode(barcode: String): ScanResponse = withContext(Dispatchers.IO){
        val posts = service.scan(barcode)
        posts
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
            0, TableRow.LayoutParams.WRAP_CONTENT, 1f)

        val locationView = TextView(this)
        locationView.text = location.substring(0 until 18.coerceAtMost(location.length))
        locationView.layoutParams = TableRow.LayoutParams(
            0, TableRow.LayoutParams.WRAP_CONTENT, 1f)
        locationView.gravity = Gravity.CENTER

        val quantityView = TextView(this)
        quantityView.text = quantity.toString()
        quantityView.layoutParams = TableRow.LayoutParams(
            0, TableRow.LayoutParams.WRAP_CONTENT, 1f)
        quantityView.gravity = Gravity.END

        row.addView(nameView)
        row.addView(locationView)
        row.addView(quantityView)
        row.layoutParams = layoutParams
        ownershipRowMap[ownership.ownershipUID] = row

        // TODO set onclick listener to row

        return row
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

    private fun populateItems(postScanResponse: ScanResponse){
        val tableLayout = scannerBinding.itemsTableLayout

        for (ownership in postScanResponse.ownership){
            if(!ownershipRowMap.containsKey(ownership.ownershipUID)) {
                OwnershipManager.addOwnership(ownership)
                val row = createRowForOwnership(ownership)
                setColorForRow(row, tableLayout.childCount)
                tableLayout.addView(row)
            }
        }

        handler.postDelayed({
            // Enable scanning after 5 seconds
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

